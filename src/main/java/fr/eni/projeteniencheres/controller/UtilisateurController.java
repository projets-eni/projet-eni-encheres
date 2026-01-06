package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dto.UtilisateurAffichageDTO;
import fr.eni.projeteniencheres.dto.UtilisateurFormDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    return "redirect:/encheres";
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
    public String afficherUnUtilisateur(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        //méthode findUtilisateurAffichageById() pour pouvoir utiliser un DTO pour l'affichage des données utilisateur
        UtilisateurAffichageDTO utilisateurAffichageDTO = utilisateurService.findUtilisateurAffichageByPseudo(userDetails.getUsername());
        System.out.println(utilisateurAffichageDTO);

        model.addAttribute("utilisateur", utilisateurAffichageDTO);

        return "profilUtilisateur";
    }

    // Pour permettre le lien vers la page d'affichage du profil du vendeur sur la page détails de la vente
    @GetMapping("/profil/{pseudo}")
    public String profil(@PathVariable("pseudo") String pseudo, Model model) {
        Utilisateur utilisateur = utilisateurService.findUtilisateurByPseudo(pseudo);
        model.addAttribute("utilisateur", utilisateur);
        return "profilUtilisateur";
    }

    @PostMapping("/profil/modifier")
    public String modifierProfil(){
    return "modifierProfilUtilisateur";
    }


    @PostMapping("/profil/supprimer")
    public String supprimerCompte(@AuthenticationPrincipal UserDetails userDetails,
                                  HttpServletRequest request, HttpServletResponse response,
                                  Model model){
        long getUserId = utilisateurService.findUtilisateurByPseudo(userDetails.getUsername()).getNoUtilisateur();
        utilisateurService.deleteUtilisateurById(getUserId);

        // Déconnexion Spring Security manuelle
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);

        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/encheres";

        // Pour aller plus loin ...
//        return "redirect:/login?compte-supprime"; -> on pourrait imaginer modifier
        // A mettre dans la méthode GET mappée sur la route /login : ... permettrait d'afficher un message à l'utilisateur
//        @GetMapping("/login")
//        public String login(@RequestParam(value = "compte-supprime", required = false) String message,
//                Model model) {
//            if ("compte-supprime".equals(message)) {
//                model.addAttribute("success", "Compte supprimé avec succès. Vous pouvez créer un nouveau compte.");
//            }
//            return "login";
//        }
    }


}
