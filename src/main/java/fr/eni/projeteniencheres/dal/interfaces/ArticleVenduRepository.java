package fr.eni.projeteniencheres.dal.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.EtatAchat;
import fr.eni.projeteniencheres.bo.EtatVente;
import fr.eni.projeteniencheres.bo.Utilisateur;

import java.util.List;

public interface ArticleVenduRepository {

    public List<ArticleVendu> findAll();

    /**
     * Liste des ventes pour un état ou un utilisateur
     * @param etat  EtatVente  ou null
     * @param utilisateur   Utilisateur ou null
     * @return
     */
    public List<ArticleVendu> findByEtatEtVendeur(EtatVente etat, Utilisateur utilisateur);

    /**
     * Liste des articles remportés pour un urilisateur
     * @param etat          EtatAchat
     * @param utilisateur   Utilisateur ou null
     * @return
     */
    public List<ArticleVendu> findByAcquereur(EtatAchat etat, Utilisateur utilisateur);

    public ArticleVendu findById(int id);

    public ArticleVendu save(ArticleVendu vente);

    public void delete(ArticleVendu vente);

}
