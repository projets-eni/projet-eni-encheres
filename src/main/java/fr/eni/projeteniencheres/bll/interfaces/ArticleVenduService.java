package fr.eni.projeteniencheres.bll.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;

import java.util.List;

public interface ArticleVenduService {
    List<ArticleVendu> afficherArticles();
    ArticleVendu afficherArticleParId(int id);
    ArticleVendu ajoutArticle(ArticleVendu article);
    ArticleVendu modifierArticle(ArticleVendu article);
    void supprimerArticle(ArticleVendu article);
}
