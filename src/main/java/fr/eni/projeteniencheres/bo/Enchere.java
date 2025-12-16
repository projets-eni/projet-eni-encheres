package fr.eni.projeteniencheres.bo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Enchere implements Serializable {

    private static final long serialVersionUID = 1L ;
    private long noEnchere;
    private LocalDateTime dateEnchere;
    private int montantEnchere;
    private ArticleVendu articleVendu;
    private Utilisateur acheteur;

    public Enchere() {
    }

    public Enchere(LocalDateTime dateEnchere, int montantEnchere, ArticleVendu articleVendu, Utilisateur acheteur) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.articleVendu = articleVendu;
        this.acheteur = acheteur;
    }

    public Enchere(long noEnchere, LocalDateTime dateEnchere, int montantEnchere, ArticleVendu articleVendu, Utilisateur acheteur) {
        this.noEnchere = noEnchere;
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.articleVendu = articleVendu;
        this.acheteur = acheteur;
    }

    public long getNoEnchere() {
        return noEnchere;
    }

    public void setNoEnchere(long noEnchere) {
        this.noEnchere = noEnchere;
    }

    public LocalDateTime getDateEnchere() {
        return dateEnchere;
    }

    public void setDateEnchere(LocalDateTime dateEnchere) {
        this.dateEnchere = dateEnchere;
    }

    public int getMontantEnchere() {
        return montantEnchere;
    }

    public void setMontantEnchere(int montantEnchere) {
        this.montantEnchere = montantEnchere;
    }

    public ArticleVendu getArticleVendu() {
        return articleVendu;
    }

    public void setArticleVendu(ArticleVendu articleVendu) {
        this.articleVendu = articleVendu;
    }

    public Utilisateur getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(Utilisateur acheteur) {
        this.acheteur = acheteur;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Enchere enchere = (Enchere) o;
        return noEnchere == enchere.noEnchere && montantEnchere == enchere.montantEnchere && Objects.equals(dateEnchere, enchere.dateEnchere) && Objects.equals(articleVendu, enchere.articleVendu) && Objects.equals(acheteur, enchere.acheteur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noEnchere, dateEnchere, montantEnchere, articleVendu, acheteur);
    }

    @Override
    public String toString() {
        return "Enchere{" +
                "noEnchere=" + noEnchere +
                ", dateEnchere=" + dateEnchere +
                ", montantEnchere=" + montantEnchere +
                ", articleVendu=" + articleVendu +
                ", acheteur=" + acheteur +
                '}';
    }
}
