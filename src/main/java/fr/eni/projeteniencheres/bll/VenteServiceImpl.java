package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.*;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Categorie;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dal.ArticleVenduRepositoryImpl;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Override
    public NouvelleVenteDto initFormulaireNouvelleVente(String pseudo) {

        Utilisateur vendeur = utilisateurService.findUtilisateurByPseudo(pseudo);

        // Par défaut, si non renseigné, le retrait se fait à l'adresse du vendeur
        // Par défaut, la date de début d'enchère = commence dans 10 minutes et durée de l'enchère = 7 jours

        NouvelleVenteDto dto = new NouvelleVenteDto();
        dto.setRue(vendeur.getRue());
        dto.setCodePostal(vendeur.getCodePostal());
        dto.setVille(vendeur.getVille());
        LocalDateTime maintenant = LocalDateTime.now().plusMinutes(10);
        dto.setDateDebutEncheres(maintenant);
        dto.setDateFinEncheres(maintenant.plusDays(7));

        return dto;
    }

    @Override
    public NouvelleVenteDto initFormulaireModifierVente(String pseudo, int noArticle) {

        Utilisateur vendeur = utilisateurService.findUtilisateurByPseudo(pseudo);
        ArticleVendu article = articleVenduService.findById(noArticle);

        NouvelleVenteDto dto = new NouvelleVenteDto();
        dto.setNomArticle(article.getNomArticle());
        dto.setDescription(article.getDescription());
        dto.setNoCategorie((int) article.getCategorie().getNoCategorie());
        dto.setIdImage(article.getIdImage());
        dto.setPrixInitial(article.getPrixInitial());
        dto.setDateDebutEncheres(article.getDateDebutEncheres());
        dto.setDateFinEncheres(article.getDateFinEncheres());
        dto.setRue(article.getRetrait().getRue());
        dto.setCodePostal(article.getRetrait().getCodePostal());
        dto.setVille(article.getRetrait().getVille());

        return dto;
    }

    @Transactional
    @Override
    public ArticleVendu modifierVente(int noArticle, NouvelleVenteDto dto, String pseudo) {

        // récupération des attributs simples
        ArticleVendu nouvelarticle = new ArticleVendu();
        BeanUtils.copyProperties(dto, nouvelarticle);

        // récupération des attributs issus de relations FK
        Utilisateur vendeur = utilisateurService.findUtilisateurByPseudo(pseudo);
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

        // on appelle le service qui modifie l'article en BDD
        ArticleVendu articleSaved = articleVenduService.modifierArticle(noArticle, nouvelarticle);

        // on appelle le service qui modifie le retrait associé au nouvel article en BDD
        Retrait retraitSaved = retraitService.modifierRetrait(noArticle, retraitSaisie);
        articleSaved.setRetrait(retraitSaved);

        return articleSaved;

    }

    @Override
    public ArticleVendu annulerVente(int noArticle, String pseudo) {

        ArticleVendu articleToDelete = articleVenduService.findById(noArticle);
        articleToDelete.setEtatVente("Annulée");
        // on appelle le service qui modifie l'article en BDD
        ArticleVendu articleSaved = articleVenduService.modifierArticle(noArticle, articleToDelete);
        // pas besoin de modifier le retrait, il va persister en BDD
        // on peut envisager : appeler une procédure stockée qui supprimer les ventes annulées - garder un historique de X ans seulement

        return articleSaved;
    }

    @Transactional
    @Override
    public ArticleVendu creerNouvelleVente(NouvelleVenteDto dto, String pseudo) {

        // récupération des attributs simples
        ArticleVendu nouvelarticle = new ArticleVendu();
        BeanUtils.copyProperties(dto, nouvelarticle);

        // récupération des attributs issus de relations FK
        Utilisateur vendeur = utilisateurService.findUtilisateurByPseudo(pseudo);
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