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
import java.util.Arrays;
import java.util.List;
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
        return findEnCours();
    }

    @Override
    public List<ArticleVendu> findEnCours() {
        return articleVenduRepository.findEnCours();
    }

    @Override
    public ArticleVendu afficherArticleParId(int id) {
        return null;
    }

    @Override
    public ArticleVendu ajoutArticle(ArticleVendu article) {
        return articleVenduRepository.ajoutArticle(article);
    }

    @Override
    public ArticleVendu modifierArticle(ArticleVendu article) {
        return articleVenduRepository.modifierArticle(article.getNoArticle(), article);
    }

    @Override
    public void supprimerArticle(ArticleVendu article) {
    }

    @Override
    @Scheduled(fixedRate = 30000)
    public void terminerArticle(ArticleVendu article) {

    }


//    @Override
//    public List<ArticleVendu> findByAcquereur(Utilisateur utilisateur) {
//        List<Integer> acquisitions = ((List<Integer>) enchereRepository.findByAcquereur(utilisateur)
//                .stream().map(e -> { return e.getArticleVendu().getNoArticle(); }).distinct());
//        List<ArticleVendu> articles = articleVenduRepository.findById(acquisitions);
//        return articles;
//    }

//    @Override
//    public List<ArticleVendu> findByEncherisseur(Utilisateur utilisateur) {
//        List<Integer> encheres = ((List<Integer>) enchereRepository.findByEncherisseur(utilisateur)
//                .stream().map(e -> { return e.getArticleVendu().getNoArticle(); }).distinct());
//        List<ArticleVendu> articles = articleVenduRepository.findById(encheres);
//        return articles;
//    }

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

        // à compléter avec les filtres

        return articles ;
    }

}