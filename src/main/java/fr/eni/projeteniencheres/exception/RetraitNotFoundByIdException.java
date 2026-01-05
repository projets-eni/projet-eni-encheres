package fr.eni.projeteniencheres.exception;

public class RetraitNotFoundByIdException extends RuntimeException {
    public RetraitNotFoundByIdException(long id) {
        super("Retrait non trouvé" + id);
    }
}
