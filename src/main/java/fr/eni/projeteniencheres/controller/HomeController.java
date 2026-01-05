package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    private final UtilisateurService utilisateurService;

    public HomeController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

//    @GetMapping({"/", "/encheres"})
//    public String accueil(Model model, Principal principal) {
//
//        if (principal != null) {
//            String pseudo = principal.getName();
//            model.addAttribute("pseudo", pseudo);
//            System.out.println("principal : " + principal.getName());
//        } else {
//            System.out.println("Aucun utilisateur connecté");
//        }
//        return "view-liste-encheres";
//    }
}
