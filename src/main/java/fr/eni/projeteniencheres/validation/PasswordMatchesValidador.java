package fr.eni.projeteniencheres.validation;

import fr.eni.projeteniencheres.dto.UtilisateurFormDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidador implements ConstraintValidator<PasswordMatches,Object> {


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // laisser les autres @NotBlank gérer ça
        }

        UtilisateurFormDTO dto = (UtilisateurFormDTO) value;
        String mdp = dto.getMotDePasse();
        String confirm = dto.getConfirmationMotDePasse();

        if (mdp == null || confirm == null) {
            return true; // ou false si tu veux forcer la présence des deux
        }

        boolean matches = mdp.equals(confirm);
        if (!matches) {
            // Optionnel : rattacher le message au champ confirmationMotDePasse
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Les deux mots de passe doivent coïncider"
                    ).addPropertyNode("confirmationMotDePasse")
                    .addConstraintViolation();
        }
        return matches;
    }
}




