package fr.eni.projeteniencheres.dal.interfaces;

import fr.eni.projeteniencheres.bo.Retrait;

public interface RetraitRepository {
    Retrait ajoutRetrait(Retrait retrait);
    Retrait afficherRetraitParId(int noArticle);
}
