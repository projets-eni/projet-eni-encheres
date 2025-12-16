package fr.eni.projeteniencheres.dal.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;

import java.util.List;

public interface ArticleVenduRepository {
    List<ArticleVendu> afficherArticles();
    ArticleVendu afficherArticleParId(int id);
    ArticleVendu ajoutArticle(ArticleVendu article);
    ArticleVendu modifierArticle(ArticleVendu article);
    void supprimerArticle(ArticleVendu article);
}
