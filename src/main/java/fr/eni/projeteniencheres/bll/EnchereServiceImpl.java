package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.ArticleVenduService;
import fr.eni.projeteniencheres.bll.interfaces.EnchereService;
import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Enchere;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dal.interfaces.EnchereRepository;
import fr.eni.projeteniencheres.exception.EnchereImpossible;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnchereServiceImpl implements EnchereService {

    private final UtilisateurService utilisateurService;
    private final ArticleVenduService articleVenduService;
    private EnchereRepository enchereRepository;

    public EnchereServiceImpl(EnchereRepository enchereRepository, UtilisateurService utilisateurService, ArticleVenduService articleVenduService) {
        this.enchereRepository = enchereRepository;
        this.utilisateurService = utilisateurService;
        this.articleVenduService = articleVenduService;
    }

    @Override
    public Enchere getById(int id) {
        return null;
    }

    @Override
    public List<Enchere> getByArticleVendu(ArticleVendu articleVendu) {
        List<Enchere> encheresAll = enchereRepository.findByVente(articleVendu);

        List<Utilisateur> acquereurs = encheresAll.stream()            // Transformer List en Stream
                .map(enchere -> enchere.getAcheteur())        // TRANSFORMER : Enchere → Utilisateur
                .distinct()                                            // ÉLIMINER DOUBLONS (Marie, Marie → Marie)
                .sorted(Comparator.comparing(Utilisateur::getPseudo))  // TRIER alphabétiquement
                .collect(Collectors.toList());                         // TERMINER : Conversion en List

        List<Enchere> encheresBest = encheresAll.stream()
                .collect(Collectors.groupingBy(                                             // GROUPER comme un dictionnaire
                        Enchere::getAcheteur,                                               // Clé = acquéreur
                        Collectors.maxBy(Comparator.comparing(Enchere::getMontantEnchere))  // Valeur = MAX enchère
                ))                                                                          // Résultat : Map<Utilisateur, Optional<Enchere>>
                .values()                       // Prendre SEULEMENT les valeurs (Optionals) -> on obtient un tableau d'Optionnal<Enchere>
                .stream()                       // Retourner en Stream -> on obtient un stream de tableau d'Optionnal<Enchere>
                .map(Optional::get)             // Extraire l'Enchere de chaque Optional -> on obtient un stream d'Encheres
                .sorted(Comparator.comparing(Enchere::getMontantEnchere).reversed())  // TRIER par montant ↓
                .collect(Collectors.toList());  // TERMINER : Conversion en List

        return encheresBest ;
    }

    @Override
    public Enchere placer(Enchere enchere) {
        Enchere nouvelle = null;
        Integer res = this.enchereRepository.placer(enchere);
        if (res > 0) {
            nouvelle = this.enchereRepository.findById(res);
        } else {
            String msg = "Enchère impossible : ";
            switch(res)
                {
                case -1:
                    msg += "vendeur inconnu";
                    break;
                case -2:
                    msg += "vente fermée";
                    break;
                case -3:
                    msg += "vous êtes le vendeur";
                    break;
                case -4:
                    msg += "enchérisseur inconnu";
                    break;
                case -5:
                    msg += "vous êtes déjà le meilleur enchérisseur";
                    break;
                case -6:
                    msg += "crédit insuffisant";
                    break;
                default:
                    msg += "raison inconnue";
                }
                throw new EnchereImpossible(msg);
        }
        return nouvelle;
    }

    @Transactional
    @Override
    public Enchere encherir(int noArticle, String pseudo, int nouveauMontantEnchere) {
        // Vérification que l'acheteur a bien le solde requis
        // Vérification que le montant de l'offre est > la dernière offre

//        Enchere enchere = enchereRepository.encherir(noArticle, pseudo, nouveauMontantEnchere);
        return null ;
    }

    @Override
    public Enchere getMaDerniereOffre(String username, int noArticle) {
        ArticleVendu article = articleVenduService.findById(noArticle);
        return enchereRepository.getLastOffreByArticleAndUserName(article, username);
    }

    @Override
    public Enchere getMeilleureOffreByArticle(int noArticle) {
        ArticleVendu article = articleVenduService.findById(noArticle);
        return enchereRepository.getMeilleureOffreByArticle(article);
    }
}
