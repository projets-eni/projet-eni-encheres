package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dto.UtilisateurDTO;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UtilisateurController {

private UtilisateurService utilisateurService;

@Autowired
public UtilisateurController(UtilisateurService utilisateurService) {
	this.utilisateurService = utilisateurService;
}

@GetMapping("/inscription")
public String affichageInscription(Model model) {

    if (!model.containsAttribute("utilisateurDTO")) {
        model.addAttribute("utilisateurDTO", new UtilisateurDTO());
    }

    return "inscription";
}

@PostMapping("/inscription")
public String inscription(@Valid @ModelAttribute("utilisateurDTO") UtilisateurDTO utilisateurDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttr) {

    if (result.hasErrors()) {
        redirectAttr.addFlashAttribute( "org.springframework.validation.BindingResult.utilisateurDTO", result);
        redirectAttr.addFlashAttribute("utilisateurDTO", utilisateurDTO);
        return "redirect:/inscription";
    }
    Utilisateur utilisateur = new Utilisateur();
    BeanUtils.copyProperties(utilisateurDTO, utilisateur);

    try {
        utilisateurService.createUtilisateur(utilisateur);
    } catch (RuntimeException ex) {
        redirectAttr.addFlashAttribute("erreur", ex.getMessage());
    }

    return "redirect:/inscription";
}


}
