package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.ArticleVenduService;
import fr.eni.projeteniencheres.bll.interfaces.EnchereService;
import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Utilisateur;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
