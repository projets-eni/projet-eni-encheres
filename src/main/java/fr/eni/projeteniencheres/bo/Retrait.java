package fr.eni.projeteniencheres.bo;

import java.io.Serializable;
import java.util.Objects;

public class Retrait implements Serializable {

    private static final long serialVersionUID = 1L;
    private long noArticle;
    private String rue;
    private String codePostal;
    private String ville;
    private boolean estRetire;

    public Retrait() {
    }

    public Retrait(String rue, String codePostal, String ville, boolean estRetire) {
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.estRetire = estRetire;
    }

    public long getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(long noArticle) {
        this.noArticle = noArticle;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public boolean isEstRetire() {
        return estRetire;
    }

    public void setEstRetire(boolean estRetire) {
        this.estRetire = estRetire;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Retrait retrait = (Retrait) o;
        return noArticle == retrait.noArticle && estRetire == retrait.estRetire && Objects.equals(rue, retrait.rue) && Objects.equals(codePostal, retrait.codePostal) && Objects.equals(ville, retrait.ville);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noArticle, rue, codePostal, ville, estRetire);
    }

    @Override
    public String toString() {
        return "Retrait{" +
                "noArticle=" + noArticle +
                ", rue='" + rue + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                ", estRetire=" + estRetire +
                '}';
    }
}
