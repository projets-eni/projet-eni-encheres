package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.dal.interfaces.RetraitRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
}