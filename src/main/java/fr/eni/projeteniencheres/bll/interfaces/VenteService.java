package fr.eni.projeteniencheres.bll.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;

public interface VenteService {

    ArticleVendu creerNouvelleVente(NouvelleVenteDto dto, String emailVendeur);
    NouvelleVenteDto initFormulaireNouvelleVente(String email);
    NouvelleVenteDto afficherVenteParNoArticle(int noArticle);

}
