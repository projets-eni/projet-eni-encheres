package fr.eni.projeteniencheres.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * pour catcher les exception issues de l'appli et renvoyer vers une page d'erreur
 */

//@ControllerAdvice
public class EnchereControllerAdvice  {

    Logger logger = LoggerFactory.getLogger(EnchereControllerAdvice.class);

    @ExceptionHandler(value = RuntimeException.class)
    public String handleError(HttpServletRequest  res, Exception e) throws Exception {
        logger.error(e.getMessage(), e.getCause());
        return "error";
    }


}
