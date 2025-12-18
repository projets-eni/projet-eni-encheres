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

    @Transactional
    @Override
    public ArticleVendu creerNouvelleVente(NouvelleVenteDto dto, String email) {

        // récupération des attributs simples
        ArticleVendu nouvelarticle = new ArticleVendu();
        BeanUtils.copyProperties(dto, nouvelarticle);

        // récupération des attributs issus de relations FK
        Utilisateur vendeur = utilisateurService.findUtilisateurByEmail(email);
        email = "martin.dupond@yahoo.fr";
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

    public Retrait preparerAdresseVendeur(String email){
        // Par défaut, si non renseigné, le retrait se fait à l'adresse du vendeur
        Retrait adresseVendeur = new Retrait(
                    utilisateurService.findUtilisateurByEmail(email).getRue(),
                    utilisateurService.findUtilisateurByEmail(email).getCodePostal(),
                    utilisateurService.findUtilisateurByEmail(email).getVille());
        return adresseVendeur;
    }
}
