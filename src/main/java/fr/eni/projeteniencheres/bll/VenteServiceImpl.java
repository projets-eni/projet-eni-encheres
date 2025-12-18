package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.*;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Categorie;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class VenteServiceImpl implements VenteService {

    // Appel aux SERVICES (BLL) et à son propre repository (DAL)
    private final RetraitService retraitService;
    private final CategorieService categorieService;
    private final UtilisateurService utilisateurService;
    private final ArticleVenduService articleVenduService;

    public VenteServiceImpl(RetraitService retraitService, CategorieService categorieService, UtilisateurService utilisateurService, ArticleVenduService articleVenduService) {
        this.retraitService = retraitService;
        this.categorieService = categorieService;
        this.utilisateurService = utilisateurService;
        this.articleVenduService = articleVenduService;
    }

    public NouvelleVenteDto initFormulaireNouvelleVente(String email) {

        Utilisateur vendeur = utilisateurService.findUtilisateurByEmail(email);

        // Par défaut, si non renseigné, le retrait se fait à l'adresse du vendeur
        // Par défaut, la date de début d'enchère = maintenant et durée de l'enchère = 7 jours

        NouvelleVenteDto dto = new NouvelleVenteDto();
        dto.setRue(vendeur.getRue());
        dto.setCodePostal(vendeur.getCodePostal());
        dto.setVille(vendeur.getVille());
        LocalDateTime maintenant = LocalDateTime.now().withSecond(0).withNano(0);
        dto.setDateDebutEncheres(maintenant);
        dto.setDateFinEncheres(maintenant.plusDays(7));

        return dto;
    }

    @Transactional
    @Override
    public ArticleVendu creerNouvelleVente(NouvelleVenteDto dto, String email) {

        // récupération des attributs simples
        ArticleVendu nouvelarticle = new ArticleVendu();
        BeanUtils.copyProperties(dto, nouvelarticle);

        // récupération des attributs issus de relations FK
        Utilisateur vendeur = utilisateurService.findUtilisateurByEmail(email);
        Categorie categorie = categorieService.afficherCategoryParId(dto.getNoCategorie());
        nouvelarticle.setCategorie(categorie);
        nouvelarticle.setVendeur(vendeur);

        // Etat de la vente au départ = Non commencée
        nouvelarticle.setEtatVente("Non commencée");

        // Par défaut, si non renseigné, le retrait se fait à l'adresse du vendeur
        Retrait retraitSaisie = new Retrait();
        BeanUtils.copyProperties(dto, retraitSaisie);
        nouvelarticle.setRetrait(retraitSaisie);
        if (retraitSaisie.getRue().isEmpty() || retraitSaisie.getVille().isEmpty() || retraitSaisie.getCodePostal().isEmpty()) {
            Retrait retraitChezVendeur = new Retrait(vendeur.getRue(), vendeur.getCodePostal(), vendeur.getVille());
            nouvelarticle.setRetrait(retraitChezVendeur);
        }

//        // Par défaut, la date de début d'enchère = maintenant et durée de l'enchère = 7 jours
//        if (dto.getDateDebutEncheres() != null && dto.getDateFinEncheres() != null) {
//            nouvelarticle.setDateDebutEncheres(dto.getDateDebutEncheres());
//            nouvelarticle.setDateFinEncheres(dto.getDateFinEncheres());
//        } else {
//            nouvelarticle.setDateDebutEncheres(LocalDateTime.now());
//            nouvelarticle.setDateFinEncheres(LocalDateTime.now().plusDays(7));
//        }

        // on appelle le service qui enregistre l'article en BDD
        ArticleVendu articleSaved = articleVenduService.ajoutArticle(nouvelarticle);
        long idArticle = articleSaved.getNoArticle();

        // on appelle le service qui enregistre le retrait associé au nouvel article en BDD
        articleSaved.getRetrait().setNoArticle(idArticle);
        Retrait retraitSaved = retraitService.ajoutRetrait(articleSaved.getRetrait());
        articleSaved.setRetrait(retraitSaved);

        return articleSaved;
    }

}