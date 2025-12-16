package fr.eni.projeteniencheres.exception;

public class UtilisateurNotFoundByIdException extends RuntimeException{
    public UtilisateurNotFoundByIdException(long id) {
        super("L'utilisateur non trouvé" + id);
    }
}
