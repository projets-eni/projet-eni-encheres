package fr.eni.projeteniencheres.dto;

import jakarta.validation.constraints.Null;

import javax.annotation.Nullable;

public record RechercheRecord(

        String kw,
        Integer idCategorie,

        Integer achatOuverts,
        Integer achatMesEncheres,
        Integer achatMesAcquis,

        Integer venteOuvertes,
        Integer venteAttentes,
        Integer venteTermines
) {
}
