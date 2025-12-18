package fr.eni.projeteniencheres.exception;

public class UtilisateurNotFoundByPseudoException extends RuntimeException{
    public UtilisateurNotFoundByPseudoException(String pseudo) {
        super("L'utilisateur non trouvé" + pseudo);
    }
}
