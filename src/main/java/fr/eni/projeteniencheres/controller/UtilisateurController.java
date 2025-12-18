package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dto.UtilisateurAffichageDTO;
import fr.eni.projeteniencheres.dto.UtilisateurFormDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UtilisateurController {


private final UtilisateurService utilisateurService;

public UtilisateurController(UtilisateurService utilisateurService) {
	this.utilisateurService = utilisateurService;
}

@GetMapping("/inscription")
public String affichageFormInscription(Model model) {

    if (!model.containsAttribute("utilisateurFormDTO")) {
        model.addAttribute("utilisateurDTO", new UtilisateurFormDTO());
    }

    return "inscription";
}

@PostMapping("/inscription")
public String inscription(@Valid @ModelAttribute("utilisateurFormDTO") UtilisateurFormDTO utilisateurFormDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttr) {

    if (result.hasErrors()) {
        redirectAttr.addFlashAttribute( "org.springframework.validation.BindingResult.utilisateurFormDTO", result);
        redirectAttr.addFlashAttribute("utilisateurFormDTO", utilisateurFormDTO);
        return "redirect:/inscription";
    }

    utilisateurService.createUtilisateur(utilisateurFormDTO);

    return "redirect:/accueil";
}

    @GetMapping("/connexion")
    public String acces(Model model) {
        return "connexion";
    }

//    @PostMapping("/connexion")
//    public String connexion(@RequestParam String pseudo,
//                            @RequestParam String motDePasse,
//                            RedirectAttributes redirectAttr) {
//        System.out.println("appel méthode connexion");
//        Utilisateur utilisateur = utilisateurService.findUtilisateurByPseudo(pseudo);
//        System.out.println("utilisateur: " + utilisateur);
//        return "accueil";
//    }

    @GetMapping("/profil")
    //(name= "id") = nom dans la requête http
    // Thymeleaf va générer l'url avec l'id correspondant à l'utilisateur et
    // le place dans la variable identifiant
    //le controller injecte cette inscription dans le model puis le passe à la vue
    public String afficherUnUtilisateur(@RequestParam(name="noUtilisateur") long identifiant, Model model) {

        //méthode findUtilisateurAffichageById() pour pouvoir utiliser un DTO pour l'affichage des données utilisateur
        UtilisateurAffichageDTO utilisateurAffichageDTO = utilisateurService.findUtilisateurAffichageById(identifiant);
        System.out.println(utilisateurAffichageDTO);

        model.addAttribute("utilisateur", utilisateurAffichageDTO);

        return "profilUtilisateur";
    }

    @PostMapping("/profil/modifier")
    public String modifierProfil(){
    return"modifierProfilUtilisateur";
    }


}
