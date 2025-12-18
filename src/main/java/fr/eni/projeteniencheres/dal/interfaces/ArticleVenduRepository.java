package fr.eni.projeteniencheres.dal.interfaces;

import fr.eni.projeteniencheres.bo.*;

import java.util.List;

public interface ArticleVenduRepository {

    public List<ArticleVendu> findAll();

    public List<ArticleVendu> findEnCours();

    public List<ArticleVendu> findTermines();

    /**
     * Liste des ventes pour un état ou un utilisateur
     * @param etat  EtatVente  ou null
     * @param utilisateur   Utilisateur ou null
     * @return
     */
    public List<ArticleVendu> findByEtatEtVendeur(EtatVente etat, Utilisateur utilisateur);

    public ArticleVendu findById(int id);

    public List<ArticleVendu> findById(List<Integer> ids);

    public ArticleVendu save(ArticleVendu vente);

    public void delete(ArticleVendu vente);

    public Enchere placerEnchere(Enchere enchere);

}
