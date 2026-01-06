package fr.eni.projeteniencheres.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    private int noUtilisateur;
    private String pseudo;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String rue;
    private String codePostal;
    private String ville;
    private String motDePasse;
    private long credit;
    private boolean administrateur;
    List<ArticleVendu> acquisitions;
    List<ArticleVendu> ventes;
    List<Enchere> encheres;

    public Utilisateur() {
    }

    public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse, long credit, boolean administrateur, List<ArticleVendu> acquisitions, List<ArticleVendu> ventes, List<Enchere> encheres) {
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.motDePasse = motDePasse;
        this.credit = credit;
        this.administrateur = administrateur;
        this.acquisitions = new ArrayList<>();
        this.ventes = new ArrayList<>();
        this.encheres = new ArrayList<>();
    }

    public Utilisateur(int noUtilisateur, String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse, long credit, boolean administrateur, List<ArticleVendu> acquisitions, List<ArticleVendu> ventes, List<Enchere> encheres) {
        this.noUtilisateur = noUtilisateur;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.motDePasse = motDePasse;
        this.credit = credit;
        this.administrateur = administrateur;
        this.acquisitions = acquisitions;
        this.ventes = ventes;
        this.encheres = encheres;
    }

    public int getNoUtilisateur() {
        return noUtilisateur;
    }

    public void setNoUtilisateur(int noUtilisateur) {
        this.noUtilisateur = noUtilisateur;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public boolean isAdministrateur() {
        return administrateur;
    }

    public void setAdministrateur(boolean administrateur) {
        this.administrateur = administrateur;
    }

    public List<ArticleVendu> getAcquisitions() {
        return acquisitions;
    }

    public void setAcquisitions(List<ArticleVendu> acquisitions) {
        this.acquisitions = acquisitions;
    }

    public List<ArticleVendu> getVentes() {
        return ventes;
    }

    public void setVentes(List<ArticleVendu> ventes) {
        this.ventes = ventes;
    }

    public List<Enchere> getEncheres() {
        return encheres;
    }

    public void setEncheres(List<Enchere> encheres) {
        this.encheres = encheres;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return noUtilisateur == that.noUtilisateur && credit == that.credit && administrateur == that.administrateur && Objects.equals(pseudo, that.pseudo) && Objects.equals(nom, that.nom) && Objects.equals(prenom, that.prenom) && Objects.equals(email, that.email) && Objects.equals(telephone, that.telephone) && Objects.equals(rue, that.rue) && Objects.equals(codePostal, that.codePostal) && Objects.equals(ville, that.ville) && Objects.equals(motDePasse, that.motDePasse) && Objects.equals(acquisitions, that.acquisitions) && Objects.equals(ventes, that.ventes) && Objects.equals(encheres, that.encheres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noUtilisateur, pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, administrateur, acquisitions, ventes, encheres);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "noUtilisateur=" + noUtilisateur +
                ", pseudo='" + pseudo + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", rue='" + rue + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", credit=" + credit +
                ", administrateur=" + administrateur +
                ", acquisitions=" + acquisitions +
                ", ventes=" + ventes +
                ", encheres=" + encheres +
                '}';
    }
}
