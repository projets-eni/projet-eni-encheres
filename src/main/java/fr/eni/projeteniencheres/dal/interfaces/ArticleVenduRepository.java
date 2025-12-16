package fr.eni.projeteniencheres.dal.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.EtatVente;
import fr.eni.projeteniencheres.bo.Utilisateur;

import java.util.List;

public interface ArticleVenduRepository {

    public List<ArticleVendu> findAll();

    public List<ArticleVendu> findByEtatEtUtilisateur(EtatVente etat, Utilisateur utilisateur);

    public ArticleVendu findById(int id);

    public ArticleVendu save(ArticleVendu vente);

    public void delete(ArticleVendu vente);

}
