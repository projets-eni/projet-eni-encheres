package fr.eni.projeteniencheres.bo;

public enum EtatVente {

    ATTENTE("Non commencée"),
    ACTIVE("En cours"),
    FIN("Terminée");

    private String label;

    EtatVente(String label) {
        this.label = label;
    }
}
