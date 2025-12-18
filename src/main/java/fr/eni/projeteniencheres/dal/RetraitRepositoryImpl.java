package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dal.interfaces.RetraitRepository;
import fr.eni.projeteniencheres.exception.RetraitNotFoundByIdException;
import fr.eni.projeteniencheres.exception.UtilisateurNotFoundByIdException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RetraitRepositoryImpl implements RetraitRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public RetraitRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
    }

    @Override
    public Retrait ajoutRetrait(Retrait retrait) {

        // ----------------------------------------------------------- //
        // --                Création d'un retrait                  -- //
        // ----------------------------------------------------------- //
        String sqlRetrait = "INSERT INTO Retraits (no_article, rue, code_postal, ville, est_retire) " +
                "VALUES (:no_article, :rue, :code_postal, :ville, :est_retire)";

        MapSqlParameterSource paramsRetrait = new MapSqlParameterSource();
        paramsRetrait.addValue("no_article", retrait.getNoArticle());
        paramsRetrait.addValue("rue", retrait.getRue());
        paramsRetrait.addValue("code_postal", retrait.getCodePostal());
        paramsRetrait.addValue("ville", retrait.getVille());
        paramsRetrait.addValue("est_retire", retrait.isEstRetire());

        namedParameterJdbcTemplate.update(sqlRetrait, paramsRetrait);

        return retrait;
    }


    class RetraitRowMapper implements RowMapper<Retrait> {

        @Override
        public Retrait mapRow(ResultSet rs, int rowNum) throws SQLException {
            Retrait retrait = new Retrait();
            retrait.setNoArticle(rs.getInt("no_article"));
            retrait.setRue(rs.getString("rue"));
            retrait.setCodePostal(rs.getString("code_postal"));
            retrait.setVille(rs.getString("ville"));
            retrait.setEstRetire(rs.getBoolean("est_retire"));
            return retrait;
        }
    }

    @Override
    public Retrait afficherRetraitParId(int id) {
        String sql = "SELECT no_article, rue, code_postal, ville, est_retire FROM Retraits WHERE no_article = ?";

        Retrait retrait = new Retrait();
        try {
            retrait = jdbcTemplate.queryForObject(sql, new RetraitRepositoryImpl.RetraitRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new RetraitNotFoundByIdException(id);
        }
        return retrait;
    }
}