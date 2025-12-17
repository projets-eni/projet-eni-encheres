package fr.eni.projeteniencheres.dal.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Enchere;
import fr.eni.projeteniencheres.bo.Utilisateur;

import java.util.List;

public interface EnchereRepository {

    public List<Enchere> findByVente(ArticleVendu vente);

    public List<Enchere> findByEncherisseur(Utilisateur utilisateur);

    public List<Enchere> findByAcquereur(Utilisateur utilisateur);


}
