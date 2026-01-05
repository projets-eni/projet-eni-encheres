package fr.eni.projeteniencheres.bll.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;

public interface VenteService {

    ArticleVendu creerNouvelleVente(NouvelleVenteDto dto, String pseudo);
    NouvelleVenteDto initFormulaireNouvelleVente(String pseudo);
//    NouvelleVenteDto afficherVenteParNoArticle(int noArticle); -- à supprimer ?
    NouvelleVenteDto initFormulaireModifierVente(String pseudo, int noArticle);
    ArticleVendu modifierVente(int noArticle, NouvelleVenteDto dto, String pseudo);
    ArticleVendu annulerVente(int noArticle, String pseudo);
}
