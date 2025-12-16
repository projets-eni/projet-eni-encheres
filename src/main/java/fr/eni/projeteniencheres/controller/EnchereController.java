package fr.eni.projeteniencheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class EnchereController {

    @GetMapping({"/", "/accueil"})
    public String firsPage(){
        return "view-creer-vente"; // en attendant d'avoir une page d'accueil
    }

    @GetMapping("/nouvelle-vente")
    public String nouvelleVente(Model model) {
        return "view-creer-vente";
    }

    @GetMapping("/encheres")
    public String listeEncheres(Model model) {
        return "view-liste-encheres";
    }
}
