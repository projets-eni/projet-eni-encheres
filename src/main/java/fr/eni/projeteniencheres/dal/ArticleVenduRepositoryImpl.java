package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.*;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import fr.eni.projeteniencheres.dal.interfaces.EnchereRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.thymeleaf.expression.Lists;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ArticleVenduRepositoryImpl implements ArticleVenduRepository {

    @Autowired
    private EnchereRepository enchereRepository;

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String rqtSelect = "select v.* from ArticlesVendus v ";

    public ArticleVenduRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ArticleVendu> findAll() {
        return jdbcTemplate.query(this.rqtSelect, Map.of(), venteRowMapper);
    }

    @Override
    public List<ArticleVendu> findEnCours() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("dateDebut", "GETUTCDATE()");
        return jdbcTemplate.query(this.rqtSelect
                + " WHERE v.date_fin_encheres < :dateDebut and v.date_debut_encheres <= :dateDebut",
                params, venteRowMapper);
    }

    public List<ArticleVendu> findTermines() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("dateFin", "GETUTCDATE()");
        return jdbcTemplate.query(this.rqtSelect
                        + " WHERE v.date_fin_encheres <= :dateFin",
                params, venteRowMapper);
    }

    @Override
    public List<ArticleVendu> findByEtatEtVendeur(EtatVente etat, Utilisateur utilisateur) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder where = new StringBuilder();
        if (etat != null) {
            params.addValue("vEtat", etat);
            where.append(" AND vend.etat = :vEtat");
        }
        if (utilisateur != null) {
            params.addValue("utilisateur", utilisateur.getNoUtilisateur());
            where.append(" AND v.no_utilisateur = :utilisateur");
        }
        return jdbcTemplate.query(this.rqtSelect + " WHERE 1 " + where.toString(), params, venteRowMapper);
    }


    @Override
    public ArticleVendu findById(int id) {
        return null;
    }

    @Override
    public List<ArticleVendu> findById(List<Integer> ids) {
        return jdbcTemplate.query(rqtSelect + " WHERE v.no_article IN ("
                + ids.stream().map(String::valueOf).collect(Collectors.joining(", "))
                + ")", new MapSqlParameterSource(), venteRowMapper);
    }

    @Override
    public ArticleVendu save(ArticleVendu vente) {
        return null;
    }

    @Override
    public void delete(ArticleVendu vente) {

    }

    @Override
    public Enchere placerEnchere(Enchere enchere) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();

            jdbcTemplate.update("call placerEnchere(:)");
        } catch (RuntimeException ex) {

        }
        return null;
    }

    private final RowMapper<ArticleVendu> venteRowMapper = (rs, rowNum) -> {
        ArticleVendu articleVendu = new ArticleVendu();

        BeanUtils.copyProperties(rs, articleVendu);

        Utilisateur vendeur = new Utilisateur();
        vendeur.setNoUtilisateur(rs.getInt("no_utilisateur"));
        articleVendu.setVendeur(vendeur);

        Retrait retrait = new Retrait();
        retrait.setNoArticle(rs.getInt("no_article"));
        articleVendu.setRetrait(retrait);

        return articleVendu;
    };


}
