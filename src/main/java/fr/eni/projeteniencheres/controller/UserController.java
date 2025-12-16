package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

private UtilisateurService userService;

@Autowired
public UserController(UtilisateurService userService) {
	this.userService = userService;
}

@GetMapping("/inscription")
public String showRegistrationForm(Model model) {

    if (!model.containsAttribute("user")) {
        model.addAttribute("user", new User());
    }

    return "inscription";
}



}
