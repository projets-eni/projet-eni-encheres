package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.ArticleVenduService;
import fr.eni.projeteniencheres.bll.interfaces.EnchereService;
import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Enchere;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;
import fr.eni.projeteniencheres.dto.RechercheDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EnchereController {

    private final ArticleVenduService articleVenduService;
    private final EnchereService enchereService;
    private final UtilisateurService utilisateurService;

    public EnchereController(ArticleVenduService articleVenduService, EnchereService enchereService, UtilisateurService utilisateurService) {
        this.articleVenduService = articleVenduService;
        this.enchereService = enchereService;
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/vente/{noArticle}/encherir")
    public String encherir(Authentication authentication, @PathVariable int noArticle, @RequestParam("nouveauMontantEnchere") int nouveauMontantEnchere) {

        ArticleVendu article = articleVenduService.findById(noArticle);
        Utilisateur acheteur = utilisateurService.findUtilisateurByPseudo(authentication.getName());

        // appel de la méthode placerEnchère à insérer
//        enchereService.placer(new Enchere(LocalDateTime.now(),nouveauMontantEnchere,article,acheteur));

        return "redirect:/vente/" + noArticle; // redirection ou vue ?
    }

    @GetMapping({"/","/encheres"})
    public String listeEncheres(Authentication authentication, Model modele, HttpSession session) {

        // Récupère depuis la session SI EXISTANT
        RechercheDto recherche = (RechercheDto) session.getAttribute("recherche");
        // Si pas en session → crée une nouvelle vide
        if (recherche == null) {
            recherche = articleVenduService.initRecherche();
        }
        modele.addAttribute("recherche", recherche);    // Envoi dans le modele
        session.setAttribute("recherche", recherche);             // Sauvegarde en session pour persistance

        // Affichage des achats / ventes
//        List<ArticleVendu> articles = articleVenduService.afficherArticles();
        String pseudo = "";
        if (authentication != null && authentication.getName() != null) {
            pseudo = authentication.getName();
        }
        List<ArticleVendu> articles = articleVenduService.rechercher(recherche, pseudo);

        modele.addAttribute("articles", articles);

        return "view-liste-encheres";
    }

    @PostMapping("/encheres")
    public String rechercher(Authentication authentication, @ModelAttribute("recherche") RechercheDto dto, Model modele, HttpSession session){

        // SAUVEGARDE les critères MODIFIÉS en session
        session.setAttribute("recherche", dto);

        // Recherche et affichage des cards remplissant les critères de sélection
        String pseudo = "";
        if (authentication != null && authentication.getName() != null) {
            pseudo = authentication.getName();
        }
        List<ArticleVendu> articles = articleVenduService.rechercher(dto, pseudo);
        modele.addAttribute("articles", articles);

        return "redirect:/encheres"; // ← PAS DE REDIRECT (car pas de modification de données (idempotent))
    }

}
