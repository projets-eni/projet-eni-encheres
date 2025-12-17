package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.ArticleVenduService;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import fr.eni.projeteniencheres.dal.interfaces.RetraitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleVenduServiceImpl implements ArticleVenduService {

    private final ArticleVenduRepository articleVenduRepository;
    private final RetraitRepository retraitRepository;
    public ArticleVenduServiceImpl(ArticleVenduRepository articleVenduRepository, RetraitRepository retraitRepository) {
        this.articleVenduRepository = articleVenduRepository;
        this.retraitRepository = retraitRepository;
    }

    @Override
    public List<ArticleVendu> afficherArticles() {
        return List.of();
    }

    @Override
    public ArticleVendu afficherArticleParId(int id) {
        return null;
    }

    @Override
    public ArticleVendu ajoutArticle(ArticleVendu article) {

        // Etat de la vente au départ = Non commencée
        article.setEtatVente("Non commencée");

        // Par défaut, si non renseigné, le retrait se fait à l'adresse du vendeur
//        if (article.getRetrait() == null){
//            String rueVendeur = article.getVendeur().getRue();
//            String cpVendeur = article.getVendeur().getCodePostal();
//            String villeVendeur = article.getVendeur().getVille();
//            Retrait retraitALAdresseDuVendeur = new Retrait();
//            retraitALAdresseDuVendeur.setRue(rueVendeur);
//            retraitALAdresseDuVendeur.setCodePostal(cpVendeur);
//            retraitALAdresseDuVendeur.setVille(villeVendeur);
//            article.setRetrait(retraitALAdresseDuVendeur);
//        }
        return articleVenduRepository.ajoutArticle(article);
    }

    @Override
    public ArticleVendu modifierArticle(ArticleVendu article) {
        return null;
    }

    @Override
    public void supprimerArticle(ArticleVendu article) {

    }
}
