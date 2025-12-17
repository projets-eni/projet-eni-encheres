package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.ArticleVenduService;
import fr.eni.projeteniencheres.bo.*;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import fr.eni.projeteniencheres.dal.interfaces.EnchereRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleVenduServiceImpl implements ArticleVenduService {

    private ArticleVenduRepository articleVenduRepository;
    private EnchereRepository enchereRepository;

    public ArticleVenduServiceImpl(ArticleVenduRepository articleVenduRepository, EnchereRepository enchereRepository) {
        this.articleVenduRepository = articleVenduRepository;
        this.enchereRepository = enchereRepository;
    }

    @Override
    public List<ArticleVendu> findByAcquereur(Utilisateur utilisateur) {
        List<Integer> acquisitions = ((List<Integer>) enchereRepository.findByAcquereur(utilisateur)
                .stream().map(e -> { return e.getArticleVendu().getNoArticle(); }).distinct());
        List<ArticleVendu> articles = articleVenduRepository.findById(acquisitions);
        return articles;
    }

    @Override
    public List<ArticleVendu> findByEncherisseur(Utilisateur utilisateur) {
        List<Integer> encheres = ((List<Integer>) enchereRepository.findByEncherisseur(utilisateur)
                .stream().map(e -> { return e.getArticleVendu().getNoArticle(); }).distinct());
        List<ArticleVendu> articles = articleVenduRepository.findById(encheres);
        return articles;
    }

    @Override
    public List<ArticleVendu> filter(List<ArticleVendu> articles, EtatVente etat, Categorie cat, String keywords) {

        LocalDateTime now = LocalDateTime.now();

        switch (etat) {
            case ATTENTE:
                articles = articles.stream().filter(a -> a.getDateDebutEncheres().isAfter(now))
                        .collect(Collectors.toList());
                break;

            case FIN:
                articles = articles.stream().filter(a -> a.getDateFinEncheres().isBefore(now))
                        .collect(Collectors.toList());
                break;

            case ACTIVE:
                articles = articles.stream().filter(a -> {
                    return a.getDateFinEncheres().isAfter(now) && a.getDateDebutEncheres().isBefore(now);
                }).collect(Collectors.toList());
                default:
        }
        if(cat != null) {
            articles = articles.stream().filter(a -> a.getCategorie().equals(cat)).collect(Collectors.toList());
        }
        if (!keywords.isEmpty() && keywords.length() >= 3) {
            articles = articles.stream().filter(a -> {
                return a.getNomArticle().contains(keywords) || a.getDescription().contains(keywords);
            }).collect(Collectors.toList());
        }
        return articles;
    }

}
