package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.*;
import fr.eni.projeteniencheres.dal.interfaces.EnchereRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("enchereRepository")
public class EnchereRepositoryImpl implements EnchereRepository {

    NamedParameterJdbcTemplate jdbcTemplate;
    public EnchereRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        return jdbcTemplate.query("select * from Encheres where no_utilisateur=:noUtilisateur", parameterSource, rowMapper);
    }

    @Override
    public List<Enchere> findByAcquereur(Utilisateur utilisateur) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("noUtilisateur", utilisateur.getNoUtilisateur());
        parameterSource.addValue("etat_vente", EtatVente.FIN);
        return jdbcTemplate.query("select e.*, MAX(e.montant_enchere) as montant_enchere from Encheres e " +
                "inner join ArticlesVendus a on a.no_article = e.no_article and e.etat_vente=:etat_vente " +
                "where no_utilisateur=:noUtilisateur group by no_article", parameterSource, rowMapper);
    }


    public final RowMapper<Enchere> rowMapper = (rs, rowNum) -> {
        Enchere enchere = new Enchere();

        BeanUtils.copyProperties(rs, enchere);

        return enchere;
    };

}
