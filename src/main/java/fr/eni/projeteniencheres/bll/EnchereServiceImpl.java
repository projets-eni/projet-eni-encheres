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

import java.util.List;

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
        return List.of();
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
