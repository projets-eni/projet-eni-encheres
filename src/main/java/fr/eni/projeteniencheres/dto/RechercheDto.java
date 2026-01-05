package fr.eni.projeteniencheres.dto;

public class RechercheDto {

    private String keywords;
    private int noCategorie;
    private Integer achatOuVente;
    private Boolean encheresOuvertes ;
    private Boolean mesEncheres ;
    private Boolean mesEncheresRemportees;
    private Boolean mesVentesEnCours ;
    private Boolean mesVentesNonDebutees ;
    private Boolean mesVentesTerminees ;

    public RechercheDto(String keywords, int noCategorie, Integer achatOuVente, Boolean encheresOuvertes, Boolean mesEncheres,
                        Boolean mesEncheresRemportees, Boolean mesVentesEnCours, Boolean mesVentesNonDebutees, Boolean mesVentesTerminees) {
        this.keywords = keywords;
        this.noCategorie = noCategorie;
        this.achatOuVente = achatOuVente;
        this.encheresOuvertes = encheresOuvertes;
        this.mesEncheres = mesEncheres;
        this.mesEncheresRemportees = mesEncheresRemportees;
        this.mesVentesEnCours = mesVentesEnCours;
        this.mesVentesNonDebutees = mesVentesNonDebutees;
        this.mesVentesTerminees = mesVentesTerminees;
    }

    public String getKeywords() {
        return keywords;
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    public int getNoCategorie() {
        return noCategorie;
    }
    public void setNoCategorie(int noCategorie) {
        this.noCategorie = noCategorie;
    }
    public Integer getAchatOuVente() {
        return achatOuVente;
    }
    public void setAchatOuVente(Integer achatOuVente) {
        this.achatOuVente = achatOuVente;
    }
    public Boolean getEncheresOuvertes() {
        return encheresOuvertes;
    }
    public void setEncheresOuvertes(Boolean encheresOuvertes) {
        this.encheresOuvertes = encheresOuvertes;
    }
    public Boolean getMesEncheres() {
        return mesEncheres;
    }
    public void setMesEncheres(Boolean mesEncheres) {
        this.mesEncheres = mesEncheres;
    }
    public Boolean getMesEncheresRemportees() {
        return mesEncheresRemportees;
    }
    public void setMesEncheresRemportees(Boolean mesEncheresRemportees) {
        this.mesEncheresRemportees = mesEncheresRemportees;
    }
    public Boolean getMesVentesEnCours() {
        return mesVentesEnCours;
    }
    public void setMesVentesEnCours(Boolean mesVentesEnCours) {
        this.mesVentesEnCours = mesVentesEnCours;
    }
    public Boolean getMesVentesNonDebutees() {
        return mesVentesNonDebutees;
    }
    public void setMesVentesNonDebutees(Boolean mesVentesNonDebutees) {
        this.mesVentesNonDebutees = mesVentesNonDebutees;
    }
    public Boolean getMesVentesTerminees() {
        return mesVentesTerminees;
    }
    public void setMesVentesTerminees(Boolean mesVentesTerminees) {
        this.mesVentesTerminees = mesVentesTerminees;
    }
}