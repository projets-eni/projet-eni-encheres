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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ArticleVenduRepositoryImpl implements ArticleVenduRepository {

    @Autowired
    private EnchereRepository enchereRepository;

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String rqtSelect = "select v.*, c.*, vend.*, r.rue as r_rue, r.ville as r_ville, r.code_postal as r_cp, r.est_retire as r_retire " +
            "from ArticlesVendus v " +
            "inner join Categories c on c.no_categorie = v.no_categorie " +
            "inner join Utilisateurs vend on vend.no_utilisateur = v.no_utilisateur " +
            "left join Retraits r on r.no_article=v.no_article";

    public ArticleVenduRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ArticleVendu> findAll() {
        return jdbcTemplate.query(this.rqtSelect, Map.of(), venteRowMapper);
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
    public List<ArticleVendu> findByAcquereur(EtatAchat etat, Utilisateur utilisateur) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder where = new StringBuilder();
        switch (etat) {
            case ACQUEREUR -> {
                enchereRepository.
                where.append(" AND vend.etat = :etat");
            }

            case ENCHERISSEUR ->  {
                where.append(" AND vend.etat = :etat");
            }
        }
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
    public ArticleVendu save(ArticleVendu vente) {
        return null;
    }

    @Override
    public void delete(ArticleVendu vente) {

    }

    private final RowMapper<ArticleVendu> venteRowMapper = (rs, rowNum) -> {
        ArticleVendu articleVendu = new ArticleVendu();

        BeanUtils.copyProperties(rs, articleVendu);

        Utilisateur vendeur = new Utilisateur(
                rs.getLong("no_utilisateur"), rs.getString("pseudo"), rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"), rs.getString("telephone"), rs.getString("rue"),
                rs.getString("code_postal"), rs.getString("ville"), null,
                rs.getInt("credit"), rs.getBoolean("admin"),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        );
        articleVendu.setVendeur(vendeur);

        Retrait retrait = new Retrait(rs.getString("r_rue"), rs.getString("r_cp"), rs.getString("r_ville"), rs.getBoolean("r_retire"));
        articleVendu.setRetrait(retrait);

        return articleVendu;
    };


}
