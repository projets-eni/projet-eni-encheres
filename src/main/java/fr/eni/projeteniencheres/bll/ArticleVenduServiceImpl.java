package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.ArticleVenduService;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleVenduServiceImpl implements ArticleVenduService {

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
        return null;
    }

    @Override
    public ArticleVendu modifierArticle(ArticleVendu article) {
        return null;
    }

    @Override
    public void supprimerArticle(ArticleVendu article) {

    }
}
