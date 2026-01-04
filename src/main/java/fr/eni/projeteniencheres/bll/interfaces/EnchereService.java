package fr.eni.projeteniencheres.bll.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Enchere;
import fr.eni.projeteniencheres.exception.EnchereImpossible;

import java.util.List;

public interface EnchereService {

    Enchere getById(int id);

    List<Enchere> getByArticleVendu(ArticleVendu articleVendu);

    Enchere placer(Enchere enchere) throws EnchereImpossible;

    Enchere getMaDerniereOffre(String username, int id);

    Enchere getMeilleureOffreByArticle(int id);

}
