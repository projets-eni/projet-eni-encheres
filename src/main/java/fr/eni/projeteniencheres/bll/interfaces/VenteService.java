package fr.eni.projeteniencheres.bll.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;

public interface VenteService {

    ArticleVendu creerNouvelleVente(NouvelleVenteDto dto, String pseudo);
    NouvelleVenteDto initFormulaireNouvelleVente(String pseudo);
    NouvelleVenteDto afficherVenteParNoArticle(int noArticle);

}
