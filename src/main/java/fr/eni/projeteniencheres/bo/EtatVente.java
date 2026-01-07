package fr.eni.projeteniencheres.bo;

public enum EtatVente {

    ATTENTE("Non commencée"),
    ACTIVE("En cours"),
    FIN("Terminée"),
    ANNULE("Annulée");

    private String label;

    EtatVente(String label) {
        this.label = label;
    }
}
