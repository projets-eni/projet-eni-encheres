package fr.eni.projeteniencheres.controller;

import fr.eni.projeteniencheres.bll.interfaces.*;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VenteController {

    Logger logger = LoggerFactory.getLogger(VenteController.class);
    private final VenteService venteService;

    public VenteController(VenteService venteService) {
        this.venteService = venteService;
    }

    @GetMapping("/encheres")
    public String listeEncheres(Model modele) {
        return "view-liste-encheres";
    }

    @GetMapping({"/nouvelle-vente"})
    public String afficherPageNouvelleVente(Authentication authentication,
                                            Model modele) {

        // 1ère visite : ajoute formulaire VIDE
        if(!modele.containsAttribute("nouvelleVente")){
            String email = authentication.getName();
//            String email = "martin.dupond@yahoo.fr" ;
            modele.addAttribute("nouvelleVente", venteService.initFormulaireNouvelleVente(email));
        }
        // Pour les visites suivantes = erreur validation (POST → GET) → true → On garde les données saisies !
        return "view-creer-vente";
    }

    @PostMapping("/nouvelle-vente")
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
        String email = authentication.getName();
        venteService.creerNouvelleVente(dto, authentication.getName());
//        String email = "martin.dupond@yahoo.fr";
        venteService.creerNouvelleVente(dto, email);
        logger.info("Sauvegarde article {}", dto.getNoCategorie());

        return "redirect:/encheres";
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