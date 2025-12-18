package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.*;
import fr.eni.projeteniencheres.dal.interfaces.EnchereRepository;
import fr.eni.projeteniencheres.exception.EnchereImpossible;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("enchereRepository")
public class EnchereRepositoryImpl implements EnchereRepository {

    NamedParameterJdbcTemplate jdbcTemplate;
    public EnchereRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Enchere findById(int id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        return jdbcTemplate.queryForObject("select * from Encheres where no_enchere = :id", paramSource, rowMapper);
    }

    @Override
    public List<Enchere> findByVente(ArticleVendu vente) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("noArticle", vente.getNoArticle());
        return jdbcTemplate.query("select * from Encheres where no_article=:noArticle", parameterSource, rowMapper);
    }

    @Override
    public List<Enchere> findByEncherisseur(Utilisateur utilisateur) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("noUtilisateur", utilisateur.getNoUtilisateur());
        return jdbcTemplate.query("select * from Encheres where no_utilisateur=:noUtilisateur group by no_article", parameterSource, rowMapper);
    }

    @Override
    public List<Enchere> findByAcquereur(Utilisateur utilisateur) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("noUtilisateur", utilisateur.getNoUtilisateur());
        //parameterSource.addValue("etat_vente", EtatVente.FIN);
        return jdbcTemplate.query("select e.* from Encheres e " +
                //"inner join ArticlesVendus a on a.no_article = e.no_article and e.etat_vente=:etat_vente " +
                "where e.montant_enchere = MAX(e.montant_enchere) and e.no_utilisateur=:noUtilisateur group by e.no_article", parameterSource, rowMapper);
    }

    /**
     * Place une enchère
     * @param enchere
     * @return int no_enchere, < 0 si erreur
     */
    @Override
    public Integer placer(Enchere enchere) {
        Integer res = null;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("montantEnchere", enchere.getMontantEnchere())
                .addValue("noArticle", enchere.getArticleVendu().getNoArticle())
                .addValue("noUtilisateur", enchere.getAcheteur().getNoUtilisateur());
        try {
            res = jdbcTemplate.update("EXEC placerEnchere :montantEnchere, :noArticle, :noUtilisateur", params);
        } catch (RuntimeException ex) {
            throw new EnchereImpossible(ex.getMessage());
        }
        return res;
    }

    public final RowMapper<Enchere> rowMapper = (rs, rowNum) -> {
        Enchere enchere = new Enchere();

        BeanUtils.copyProperties(rs, enchere);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNoUtilisateur(rs.getInt("no_utilisateur"));
        enchere.setAcheteur(utilisateur);

        ArticleVendu articleVendu = new ArticleVendu();
        articleVendu.setNoArticle(rs.getInt("no_article"));
        enchere.setArticleVendu(articleVendu);

        return enchere;
    };

}
