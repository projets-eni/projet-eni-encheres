package fr.eni.projeteniencheres.exception;

public class UtilisateurNotFoundByPseudoOrEmailException extends RuntimeException{
    public UtilisateurNotFoundByPseudoOrEmailException(String pseudoOuEmail) {
        super("Aucun utilisateur trouvé pour le pseudo ou email : " + pseudoOuEmail);
    }
}
