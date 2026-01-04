package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.ArticleVenduService;
import fr.eni.projeteniencheres.bll.interfaces.RetraitService;
import fr.eni.projeteniencheres.bo.*;
import fr.eni.projeteniencheres.dal.ArticleVenduRepositoryImpl;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import fr.eni.projeteniencheres.dal.interfaces.EnchereRepository;
import fr.eni.projeteniencheres.dal.interfaces.RetraitRepository;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;
import fr.eni.projeteniencheres.dto.RechercheDto;
import fr.eni.projeteniencheres.exception.EtatVenteErreur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleVenduServiceImpl implements ArticleVenduService {

    private final ArticleVenduRepository articleVenduRepository;
    private final RetraitService retraitService;
    private EnchereRepository enchereRepository;

    private Logger logger = LoggerFactory.getLogger(ArticleVenduServiceImpl.class);

    public ArticleVenduServiceImpl(ArticleVenduRepository articleVenduRepository, RetraitService retraitService, EnchereRepository enchereRepository) {
        this.articleVenduRepository = articleVenduRepository;
        this.retraitService = retraitService;
        this.enchereRepository = enchereRepository;
    }

    @Override
    public List<ArticleVendu> afficherArticles() {
        return articleVenduRepository.findAll();
    }

    @Override
    public ArticleVendu ajoutArticle(ArticleVendu article) {
        return articleVenduRepository.ajoutArticle(article);
    }

    @Override
    public ArticleVendu modifierArticle(int noArticle, ArticleVendu article) {
        return articleVenduRepository.modifierArticle(noArticle, article);
    }

    @Override
    public void supprimerArticle(ArticleVendu article) {
    }

    @Override
    @Scheduled(fixedRate = 30000)
    public void terminerArticle(ArticleVendu article) {

    }


    @Override
    public List<ArticleVendu> findByAcquereur(Utilisateur utilisateur) {
        List<Integer> acquisitions = ((List<Integer>) enchereRepository.findByAcquereur(utilisateur)
                .stream().map(e -> { return e.getArticleVendu().getNoArticle(); }).distinct());
        List<ArticleVendu> articles = articleVenduRepository.findById(acquisitions);
        return articles;
    }

    @Override
    public List<ArticleVendu> findByEncherisseur(Utilisateur utilisateur) {
        List<Integer> encheres = ((List<Integer>) enchereRepository.findByEncherisseur(utilisateur)
                .stream().map(e -> { return e.getArticleVendu().getNoArticle(); }).distinct());
        List<ArticleVendu> articles = articleVenduRepository.findById(encheres);
        return articles;
    }

    @Override
    public List<ArticleVendu> filter(List<ArticleVendu> articles, EtatVente etat, Categorie cat, String keywords) {

        LocalDateTime now = LocalDateTime.now();

        switch (etat) {
            case ATTENTE:
                articles = articles.stream().filter(a -> a.getDateDebutEncheres().isAfter(now))
                        .collect(Collectors.toList());
                break;

            case FIN:
                articles = articles.stream().filter(a -> a.getDateFinEncheres().isBefore(now))
                        .collect(Collectors.toList());
                break;

            case ACTIVE:
                articles = articles.stream().filter(a -> {
                    return a.getDateFinEncheres().isAfter(now) && a.getDateDebutEncheres().isBefore(now);
                }).collect(Collectors.toList());
            default:
        }
        if(cat != null) {
            articles = articles.stream().filter(a -> a.getCategorie().equals(cat)).collect(Collectors.toList());
        }
        if (!keywords.isEmpty() && keywords.length() >= 3) {
            articles = articles.stream().filter(a -> {
                return a.getNomArticle().contains(keywords) || a.getDescription().contains(keywords);
            }).collect(Collectors.toList());
        }
        return articles;
    }

    @Scheduled(fixedRate = 950)
    @Override
    public void updaterEtat(ArticleVendu article) {
        logger.info("UpdaterEtat -- début");
        try {
            Integer affectedVentes = articleVenduRepository.terminerVente(article);
            logger.info("UpdaterEtat -- ventes modifiées :  " + affectedVentes);
        } catch (RuntimeException e) {
            logger.error("UpdaterEtat -- " + e.getMessage(), e);
        }
        logger.info("UpdaterEtat -- fin");
    }

    @Override
    public ArticleVendu findById(int id) {
        return articleVenduRepository.findById(id);
    }

    @Override
    public RechercheDto initRecherche() {
        RechercheDto recherche = new RechercheDto("", 0, 1, true
                , true, true, false, false, false) ;
        return recherche;
    }

    @Override
    public List<ArticleVendu> rechercher(RechercheDto dto, String pseudo) {

        List<ArticleVendu> articles = articleVenduRepository.findAll();
        List<ArticleVendu> result =  new ArrayList<ArticleVendu>();

        if (!pseudo.isEmpty() && dto.getAchatOuVente() != null) {

            if (dto.getAchatOuVente() == 1) {

                // Filtre sur les ENCHERES OUVERTES = articles dont je ne suis pas le vendeur ET statut = En cours
                List<ArticleVendu> listeEncheresOuvertes = new ArrayList<ArticleVendu>();
                if (dto.getEncheresOuvertes()) {
                    listeEncheresOuvertes = articles.stream().filter(article ->
                            article.getEtatVente().equals("En cours") && !article.getVendeur().getPseudo().equals(pseudo)).toList();
                }

                // Filtre sur MES ENCHERES = articles dont je suis dans la liste des acheteurs potentiels
                List<ArticleVendu> listeMesEncheres = new ArrayList<ArticleVendu>();
                if (dto.getMesEncheres()) {
                    listeMesEncheres = articles.stream().filter(article ->
                            article.getEncheres() != null &&
                                    article.getEncheres().stream().anyMatch(enchere -> enchere.getAcheteur().getPseudo().equals(pseudo))).toList();
                }

                // Filtre sur MES ENCHERES REMPORTEES = articles dont je suis dans l'acquéreur
                List<ArticleVendu> listeMesEncheresRemportees = new ArrayList<ArticleVendu>();
                if (dto.getMesEncheresRemportees()) {
                    listeMesEncheresRemportees = articles.stream().filter(article -> {
                                if (article.getEncheres() == null) return false;
                                Optional<Enchere> meilleureEnchere = article.getEncheres().stream().max(Comparator.comparingInt(Enchere::getMontantEnchere));
                                return meilleureEnchere.isPresent() && meilleureEnchere.get().getAcheteur().getPseudo().equals(pseudo);
                            })
                            .toList();
                }

                articles = Stream.of(listeEncheresOuvertes, listeMesEncheres, listeMesEncheresRemportees)
                        .flatMap(List::stream)
                        .distinct()
                        .toList();
            }

            if (dto.getAchatOuVente() == 2) {

                // Filtre sur MES VENTES EN COURS = articles dont je suis le vendeur ET statut = En cours
                List<ArticleVendu> listeMesVentesEnCours = new ArrayList<ArticleVendu>();
                if (dto.getMesVentesEnCours()) {
                    listeMesVentesEnCours = articles.stream().filter(article ->
                            article.getEtatVente().equals("En cours") && article.getVendeur().getPseudo().equals(pseudo)).toList();
                }

                // Filtre sur mes ventes non débutées = articles dont je suis le vendeur ET statut = En cours
                List<ArticleVendu> listeMesVentesNonDebutees = new ArrayList<ArticleVendu>();
                if (dto.getMesVentesNonDebutees()) {
                    listeMesVentesNonDebutees = articles.stream().filter(article ->
                            article.getEtatVente().equals("Non commencée") && article.getVendeur().getPseudo().equals(pseudo)).toList();
                }

                // Filtre sur mes ventes terminées = articles dont je suis le vendeur ET statut = Terminée
                List<ArticleVendu> listeMesVentesTerminees = new ArrayList<ArticleVendu>();
                if (dto.getMesVentesTerminees()) {
                    listeMesVentesTerminees = articles.stream().filter(article ->
                            article.getEtatVente().equals("Terminée") && article.getVendeur().getPseudo().equals(pseudo)).toList();
                }

                articles = Stream.of(listeMesVentesEnCours, listeMesVentesNonDebutees, listeMesVentesTerminees)
                        .flatMap(List::stream)
                        .distinct()
                        .toList();
            }
        }

        // Filtre sur la catégorie
        if (dto.getNoCategorie() > 0){
            articles = articles.stream().filter(article ->
                    article.getCategorie().getNoCategorie() == dto.getNoCategorie()).toList();
        }

        // Filtre sur les mots clés
        if (!dto.getKeywords().isEmpty()){
            articles = articles.stream().filter(article ->
                    article.getNomArticle().toLowerCase().contains(dto.getKeywords())).toList();
        }

        return articles ;
    }

}