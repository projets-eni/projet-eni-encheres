package fr.eni.projeteniencheres.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Objects;


public class NouvelleVenteDto {

    @NotBlank(message = "Le nom d'article obligatoire")
    @Size(min = 1, max = 30)
    private String nomArticle;

    @NotBlank(message = "La description obligatoire")
    @Size(min = 1, max = 300)
    private String description;

    @NotEmpty(message = "La catégorie est obligatoire")
    private int noCategorie;

    @NotBlank(message = "L'image est obligatoire")
    private String idImage ;

    @NotNull(message = "Le prix initial est obligatoire")
    @Min(value = 1, message = "Le prix doit être positif")
    @Max(value = 1000000, message = "Prix maximum dépassé")
    private int prixInitial;

    @NotNull(message = "La date de début est obligatoire")
    @FutureOrPresent(message = "La date de début doit être postérieure ou égale à la date du jour")
    private LocalDateTime dateDebutEncheres;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime dateFinEncheres;

    @AssertTrue(message = "La date de fin doit être postérieure à la date de début")
    private boolean isDateFinValide() {
        return dateFinEncheres != null &&
                !dateFinEncheres.isAfter(dateDebutEncheres);
    }

    @NotNull(message = "Le vendeur est obligatoire")
    private long idVendeur;

    @NotBlank(message = "La rue est obligatoire")
    private String rue ;

    @NotBlank(message = "Le code postal est obligatoire")
    @Pattern(regexp = "^[0-9\\-\\s]+$", message = "Format de code postal invalide")
    private String codePostal ;

    @NotBlank(message = "La ville est obligatoire")
    @Size(min = 1, max = 50)
    private String ville ;

    public NouvelleVenteDto() {
    }
    public NouvelleVenteDto(String nomArticle, String description, int noCategorie, String idImage, int prixInitial, LocalDateTime dateDebutEncheres, LocalDateTime dateFinEncheres, long idVendeur, String rue, String codePostal, String ville) {
        this.nomArticle = nomArticle;
        this.description = description;
        this.noCategorie = noCategorie;
        this.idImage = idImage;
        this.prixInitial = prixInitial;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.idVendeur = idVendeur;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public String getNomArticle() {
        return nomArticle;
    }
    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getNoCategorie() {
        return noCategorie;
    }
    public void setNoCategorie(int noCategorie) {
        this.noCategorie = noCategorie;
    }
    public String getIdImage() {
        return idImage;
    }
    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }
    public int getPrixInitial() {
        return prixInitial;
    }
    public void setPrixInitial(int prixInitial) {
        this.prixInitial = prixInitial;
    }
    public LocalDateTime getDateDebutEncheres() {
        return dateDebutEncheres;
    }
    public void setDateDebutEncheres(LocalDateTime dateDebutEncheres) {
        this.dateDebutEncheres = dateDebutEncheres;
    }
    public LocalDateTime getDateFinEncheres() {
        return dateFinEncheres;
    }
    public void setDateFinEncheres(LocalDateTime dateFinEncheres) {
        this.dateFinEncheres = dateFinEncheres;
    }
    public long getIdVendeur() {
        return idVendeur;
    }
    public void setIdVendeur(long idVendeur) {
        this.idVendeur = idVendeur;
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

    @Override
    public String toString() {
        return "NouvelleVenteDto{" +
                "nomArticle='" + nomArticle + '\'' +
                ", description='" + description + '\'' +
                ", noCategorie=" + noCategorie +
                ", idImage='" + idImage + '\'' +
                ", prixInitial=" + prixInitial +
                ", dateDebutEncheres=" + dateDebutEncheres +
                ", dateFinEncheres=" + dateFinEncheres +
                ", idVendeur=" + idVendeur +
                ", rue='" + rue + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NouvelleVenteDto that)) return false;
        return noCategorie == that.noCategorie && prixInitial == that.prixInitial && idVendeur == that.idVendeur && Objects.equals(nomArticle, that.nomArticle) && Objects.equals(description, that.description) && Objects.equals(idImage, that.idImage) && Objects.equals(dateDebutEncheres, that.dateDebutEncheres) && Objects.equals(dateFinEncheres, that.dateFinEncheres) && Objects.equals(rue, that.rue) && Objects.equals(codePostal, that.codePostal) && Objects.equals(ville, that.ville);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomArticle, description, noCategorie, idImage, prixInitial, dateDebutEncheres, dateFinEncheres, idVendeur, rue, codePostal, ville);
    }

}
