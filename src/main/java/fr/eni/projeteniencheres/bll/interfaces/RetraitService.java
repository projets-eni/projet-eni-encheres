package fr.eni.projeteniencheres.bll.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Retrait;

public interface RetraitService {
    Retrait ajoutRetrait(Retrait retrait);
    Retrait modifierRetrait(int noArticle, Retrait retrait);
    Retrait afficherRetraitParId(int noArticle);
}
