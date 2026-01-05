package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.*;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Enchere;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import java.time.ZoneId;

@Controller
public class VenteController {

    Logger logger = LoggerFactory.getLogger(VenteController.class);
    private final VenteService venteService;
    private final ArticleVenduService articleVenduService ;
    private final EnchereService enchereService;

    public VenteController(VenteService venteService, ArticleVenduService articleVenduService, EnchereService enchereService) {
        this.venteService = venteService;
        this.enchereService = enchereService;
        this.articleVenduService = articleVenduService;
    }

    @GetMapping({"/vente/creer"})
    public String afficherPageNouvelleVente(Authentication authentication,
                                            Model modele) {

        // 1ère visite : ajoute formulaire VIDE
        if(!modele.containsAttribute("nouvelleVente")){
            String email = authentication.getName();
            modele.addAttribute("nouvelleVente", venteService.initFormulaireNouvelleVente(email));
        }
        // Pour les visites suivantes = erreur validation (POST → GET) → true → On garde les données saisies !
        return "view-creer-vente";
    }

    @PostMapping("/vente/creer")
    public String creerNouvelleVente(Authentication authentication,
                                     @Valid @ModelAttribute("nouvelleVente") NouvelleVenteDto dto,
                                     BindingResult resultat, RedirectAttributes redirectAttr) {
        if(resultat.hasErrors()) {
            logger.warn("Erreurs validation : {}", resultat.getAllErrors());
//            for (var err : resultat.getAllErrors()){
//                System.out.println(err.getObjectName());
//                System.out.println(err.getDefaultMessage());
//            }
            return "view-creer-vente"; // Flash attributes auto-gérés par Spring
        }
        String pseudo = authentication.getName();
        ArticleVendu article = venteService.creerNouvelleVente(dto, pseudo);
        logger.info("Sauvegarde article {}", dto.getNoCategorie());

        return "redirect:/vente/" + article.getNoArticle();
    }

    @GetMapping("/vente/{noArticle}")
    public String afficherDetailsVente(@PathVariable int noArticle, Authentication authentication,
                                       Model modele) {

        // Récupérer les infos de l'article depuis le service et ajout au modèle pour la vue
        ArticleVendu article = articleVenduService.findById(noArticle);
        modele.addAttribute("article", article);

        // Remonter maProposition à la vue car elle n'est pas contenue dans l'objet article et ajout au modèle
        Enchere monOffre = enchereService.getMaDerniereOffre(authentication.getName(),noArticle);
        modele.addAttribute("monOffre", monOffre);

        // Meilleure offre
        Enchere meilleureOffre = enchereService.getMeilleureOffreByArticle(noArticle);
        modele.addAttribute("meilleureOffre", meilleureOffre);

        // Remonter la liste des encheres
        List<Enchere> encheres = enchereService.getByArticleVendu(article);
//        Collections.reverse(encheres); // inverser l’ordre pour afficher de la + récente à la + ancienne
        modele.addAttribute("encheres", encheres);

        // countdowndate
        long timestamp = article.getDateFinEncheres()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        modele.addAttribute("dateFinTimestamp", timestamp);

        return "view-details-vente";
    }

    @GetMapping("/vente/{noArticle}/modifier")
    public String affichePageModificationDetailsVente(@PathVariable int noArticle, Authentication authentication,
                                                      Model modele) {

        // 1er passage : init formulaire avec les données enregistrées en BDD
        if(!modele.containsAttribute("nouvelleVente")){
            String email = authentication.getName();
            modele.addAttribute("nouvelleVente", venteService.initFormulaireModifierVente(email, noArticle));
        }
        // Pour les visites suivantes = erreur validation (POST → GET) → true → On garde les données saisies !
        return "view-creer-vente";
    }

    @PostMapping("/vente/{noArticle}/modifier")
    public String modifierDetailsVente(@PathVariable int noArticle,
                                       Authentication authentication,
                                       @Valid @ModelAttribute("nouvelleVente") NouvelleVenteDto dto,
                                       BindingResult resultat, RedirectAttributes redirectAttr) {
        if(resultat.hasErrors()) {
            return "view-creer-vente"; // Flash attributes auto-gérés par Spring
        }

        String pseudo = authentication.getName();
        ArticleVendu article = venteService.modifierVente(noArticle,dto, pseudo);

        return "redirect:/vente/" + noArticle ;
    }

    @PostMapping("/vente/{noArticle}/annuler")
    public String annulerVente(@PathVariable int noArticle,
                               Authentication authentication) {

        String pseudo = authentication.getName();
        ArticleVendu article = venteService.annulerVente(noArticle, pseudo);

        return "redirect:/vente/" + noArticle ;
    }

}