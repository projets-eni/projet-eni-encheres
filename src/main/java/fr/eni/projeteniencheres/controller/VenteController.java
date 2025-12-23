package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.*;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Enchere;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;

@Controller
public class VenteController {

    Logger logger = LoggerFactory.getLogger(VenteController.class);
    private final VenteService venteService;
    private final ArticleVenduService articleVenduService ;
    private final EnchereService enchereService;

    public VenteController(VenteService venteService, ArticleVenduService articleVenduService, EnchereService enchereService) {
        this.venteService = venteService;
        this.articleVenduService = articleVenduService;
        this.enchereService = enchereService;
    }

    @GetMapping("/encheres")
    public String listeEncheres(Model modele) {
        return "view-liste-encheres";
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
        venteService.creerNouvelleVente(dto, pseudo);
        logger.info("Sauvegarde article {}", dto.getNoCategorie());

        return "redirect:/encheres";
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

        return "view-details-vente";
    }

//    @PostMapping("/uploadImage")
//    public String uploadImage(@RequestParam("file") MultipartFile file,
//                              @ModelAttribute("monObjet") NouvelleVenteDto nouvelleVenteDto) throws IOException {
//
//        if (!file.isEmpty()) {
//            // 1. Sauvegarde physique
//            String fileName = file.getOriginalFilename();
//            Path uploadDir = Paths.get("src/main/resources/static/uploads");
//            Files.createDirectories(uploadDir);
//            Files.copy(file.getInputStream(),
//                    uploadDir.resolve(fileName),
//                    StandardCopyOption.REPLACE_EXISTING);
//
//            // 2. Met la valeur dans le champ de ton bean
//            String url = "/" + fileName;   // ou juste fileName, selon ton besoin
//            nouvelleVenteDto.setIdImage(url);
//        }
//
//        // 3. Revenir sur la page avec l’objet rempli
//        return "view-creer-vente";
//    }

}