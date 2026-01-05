package fr.eni.projeteniencheres.validation;

import fr.eni.projeteniencheres.validation.PasswordMatchesValidador;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidador.class)
public @interface PasswordMatches {

    String message() default "Les mots de passe doivent coïncider";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
