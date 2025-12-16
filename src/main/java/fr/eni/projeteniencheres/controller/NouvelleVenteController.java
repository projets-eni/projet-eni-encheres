package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.ArticleVenduService;
import fr.eni.projeteniencheres.bll.interfaces.CategorieService;
import fr.eni.projeteniencheres.bll.interfaces.RetraitService;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Categorie;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class NouvelleVenteController {

    private final CategorieService categorieService;
    private final ArticleVenduService articleVenduService;
    private final RetraitService retraitService;

    public NouvelleVenteController(CategorieService categorieService,
                                   ArticleVenduService articleVenduService,
                                   RetraitService retraitService) {
        this.categorieService = categorieService;
        this.articleVenduService = articleVenduService;
        this.retraitService = retraitService;
    }

    @GetMapping({"/", "/accueil"})
    public String firsPage(){
        return "view-creer-vente"; // en attendant d'avoir une page d'accueil
    }

    @GetMapping("/nouvelle-vente")
    public String creerVente(Model model) {
        if(!model.containsAttribute("nouvelleVente")){
            model.addAttribute("nouvelleVente", new NouvelleVenteDto());
        }
        return "view-creer-vente";
    }

    @GetMapping("/encheres")
    public String listeEncheres(Model model) {
        return "view-liste-encheres";
    }

    @Transactional
    @PostMapping("/nouvelle-vente")
    public String enregistrerNouvelleVente(Authentication authentication,
                                           @Valid @ModelAttribute("nouvelleVente") NouvelleVenteDto nouvelleVenteDto,
                                           BindingResult resultat,
                                           Model modele, RedirectAttributes redirectAttr) {

//        if(resultat.hasErrors()) {
//            redirectAttr.addFlashAttribute( "org.springframework.validation.BindingResult.film", resultat);
//            redirectAttr.addFlashAttribute("nouvelleVente", nouvelleVenteDto);
//            return "redirect:/nouvelle-vente";
//        }

        // On crée un nouvel article
        ArticleVendu nouvelarticle = new ArticleVendu();
        BeanUtils.copyProperties(nouvelleVenteDto, nouvelarticle);

        // Création de l'objet Catégorie
        Categorie categorie = categorieService.afficherCategoryParId(nouvelleVenteDto.getIdCategorie());

        // service qui enregistre l'article en BDD
        ArticleVendu articleBDD = articleVenduService.ajoutArticle(nouvelarticle);
        long idArticle = articleBDD.getNoArticle();

        // Création de l'objet Retrait
        Retrait retrait = new Retrait();
        BeanUtils.copyProperties(nouvelleVenteDto, retrait);
        retrait.setNoArticle(idArticle);

        // Service qui enregistre l'objet retrait en BDD
        retraitService.ajoutRetrait(retrait);

        return "redirect:/encheres" + idArticle;
    }
}
