package fr.eni.projeteniencheres.bll.interfaces;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Categorie;
import fr.eni.projeteniencheres.bo.EtatVente;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dto.RechercheDto;

import java.util.List;

public interface ArticleVenduService {
    List<ArticleVendu> afficherArticles();
    ArticleVendu ajoutArticle(ArticleVendu article);
    ArticleVendu modifierArticle(int noArticle, ArticleVendu article);
    void supprimerArticle(ArticleVendu article);

    void terminerArticle(ArticleVendu article);


    /**
     * Liste des ventes remportées par l'utilisateur
     * @param utilisateur
     * @return
     */
    List<ArticleVendu> findByAcquereur(Utilisateur utilisateur);

    /**
     * Liste des ventes auquelles l'utilisateur a enchéri
     * @param utilisateur
     * @return
     */
    List<ArticleVendu>  findByEncherisseur(Utilisateur utilisateur);

    /**
     * Filtre une liste de vente par état, catégorie et/ou nom de l'article
     * @param articles  Liste des ventes
     * @param etat      EtatVente
     * @param cat       Categorie
     * @param keywords  Expression à chercher dans titre & description
     * @return
     */
    List<ArticleVendu> filter(List<ArticleVendu> articles, EtatVente etat, Categorie cat, String keywords);

    /**
     * Mise à jour des statuts des ventes
     * @see EtatVente pour les status
     * @param article
     */
    void updaterEtat(ArticleVendu article);

    ArticleVendu findById(int id);

    RechercheDto initRecherche();

    List<ArticleVendu> rechercher(RechercheDto dto, String pseudo);

}
