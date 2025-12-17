package fr.eni.projeteniencheres.controller;

import com.google.errorprone.annotations.Var;
import fr.eni.projeteniencheres.bll.interfaces.ArticleVenduService;
import fr.eni.projeteniencheres.bll.interfaces.CategorieService;
import fr.eni.projeteniencheres.bll.interfaces.RetraitService;
import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Categorie;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class NouvelleVenteController {

    private final UtilisateurService utilisateurService;
    Logger logger = LoggerFactory.getLogger(NouvelleVenteController.class);

    private final CategorieService categorieService;
    private final ArticleVenduService articleVenduService;
    private final RetraitService retraitService;

    public NouvelleVenteController(CategorieService categorieService,
                                   ArticleVenduService articleVenduService,
                                   RetraitService retraitService, UtilisateurService utilisateurService) {
        this.categorieService = categorieService;
        this.articleVenduService = articleVenduService;
        this.retraitService = retraitService;
        this.utilisateurService = utilisateurService;
    }

//    @GetMapping({"/", "/accueil"})
//    public String firsPage(){
//        return "view-creer-vente"; // en attendant d'avoir une page d'accueil
//    }

    @GetMapping("/encheres")
    public String listeEncheres(Model modele) {
        return "view-liste-encheres";
    }

    @GetMapping({"/", "/nouvelle-vente"})
    public String creerVente(Authentication authentication, Model modele) {
        if(!modele.containsAttribute("nouvelleVente")){
            modele.addAttribute("nouvelleVente", new NouvelleVenteDto());
            // CODE TEMPORAIRE !!!
//        String email = authentication.getName();
            String email = "martin.dupond@yahoo.fr" ;
            // -------------------------------------- //
            Utilisateur vendeur = utilisateurService.findUtilisateurByEmail(email);
            Retrait retrait = new Retrait(vendeur.getRue(), vendeur.getCodePostal(), vendeur.getVille());
            modele.addAttribute("adresseVendeur", retrait);
        }

        // Création de l'objet Vendeur -> l'id n'est pas récupéré dans le DTO mais via Authentication qui hérite de Principal !
        // -------------------------------------- //



//        modele.addAttribute("categories", categorieService.afficherCategories());
        return "view-creer-vente";
    }

    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("file") MultipartFile file,
                              @ModelAttribute("monObjet") NouvelleVenteDto nouvelleVenteDto) throws IOException {

        if (!file.isEmpty()) {
            // 1. Sauvegarde physique
            String fileName = file.getOriginalFilename();
            Path uploadDir = Paths.get("src/main/resources/static/uploads");
            Files.createDirectories(uploadDir);
            Files.copy(file.getInputStream(),
                    uploadDir.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            // 2. Met la valeur dans le champ de ton bean
            String url = "/" + fileName;   // ou juste fileName, selon ton besoin
            nouvelleVenteDto.setIdImage(url);
        }

        // 3. Revenir sur la page avec l’objet rempli
        return "view-creer-vente";
    }


    @Transactional
    @PostMapping("/nouvelle-vente")
    public String enregistrerNouvelleVente(Authentication authentication,
                                           @Valid @ModelAttribute("nouvelleVente") NouvelleVenteDto nouvelleVenteDto,
                                           BindingResult resultat,
                                           Model modele, RedirectAttributes redirectAttr) {

        if(resultat.hasErrors()) {
            for (var err : resultat.getAllErrors()){
                System.out.println(err.getObjectName());
                System.out.println(err.getDefaultMessage());
            }
            /* DEBUT A commenter ! -> inutile si l'on redirige vers une vue */
//            redirectAttr.addFlashAttribute( "org.springframework.validation.BindingResult.nouvelleVente", resultat);
//            redirectAttr.addFlashAttribute("nouvelleVente", nouvelleVenteDto);
//            modele.addAttribute("nouvelleVente", nouvelleVenteDto);
            /* FIN A commenter */
            return "view-creer-vente";
        }

        // On crée un nouvel article
        ArticleVendu nouvelarticle = new ArticleVendu();
        BeanUtils.copyProperties(nouvelleVenteDto, nouvelarticle);

        // Création de l'objet Catégorie
        Categorie categorie = categorieService.afficherCategoryParId(nouvelleVenteDto.getNoCategorie());
        nouvelarticle.setCategorie(categorie);

        // Création de l'objet Vendeur -> l'id n'est pas récupéré dans le DTO mais via Authentication qui hérite de Principal !
        // -------------------------------------- //
        // CODE TEMPORAIRE !!!
//        String email = authentication.getName();
        String email = "martin.dupond@yahoo.fr" ;
        // -------------------------------------- //
        Utilisateur vendeur = utilisateurService.findUtilisateurByEmail(email);
        nouvelarticle.setVendeur(vendeur);

        // service qui enregistre l'article en BDD
        ArticleVendu articleBDD = articleVenduService.ajoutArticle(nouvelarticle);
        long idArticle = articleBDD.getNoArticle();

        // Création de l'objet Retrait
        Retrait retrait = new Retrait();
        BeanUtils.copyProperties(nouvelleVenteDto, retrait);
        retrait.setNoArticle(idArticle);

        // Service qui enregistre l'objet retrait en BDD
        retraitService.ajoutRetrait(retrait);

        return "redirect:/encheres";
    }
}
