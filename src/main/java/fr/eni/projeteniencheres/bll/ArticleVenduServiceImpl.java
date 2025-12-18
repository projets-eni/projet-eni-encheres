package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.ArticleVenduService;
import fr.eni.projeteniencheres.bll.interfaces.RetraitService;
import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import fr.eni.projeteniencheres.dal.interfaces.RetraitRepository;
import fr.eni.projeteniencheres.dto.NouvelleVenteDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleVenduServiceImpl implements ArticleVenduService {

    private final ArticleVenduRepository articleVenduRepository;
    private final RetraitService retraitService;

    public ArticleVenduServiceImpl(ArticleVenduRepository articleVenduRepository, RetraitService retraitService) {
        this.articleVenduRepository = articleVenduRepository;
        this.retraitService = retraitService;
    }

    @Override
    public List<ArticleVendu> afficherArticles() {
        return List.of();
    }

    @Override
    public ArticleVendu afficherArticleParId(int id) {
        return null;
    }

    @Override
    public ArticleVendu ajoutArticle(ArticleVendu article) {
        return articleVenduRepository.ajoutArticle(article);
    }

    @Override
    public ArticleVendu modifierArticle(ArticleVendu article) {
        return null;
    }

    @Override
    public void supprimerArticle(ArticleVendu article) {
    }
}