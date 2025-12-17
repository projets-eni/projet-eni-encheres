package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dal.interfaces.UtilisateurRepository;
import fr.eni.projeteniencheres.exception.UtilisateurNotFoundByIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UtilisateurRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Utilisateur> findAllUtilisateurs() {
        String sql = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administateur FROM utilisateur";
        List<Utilisateur> utilisateurs = jdbcTemplate.query(sql, new UtilisateurRowMapper());
        return utilisateurs;
    }

    @Override
    public Utilisateur findUtilisateurById(long id) {
        String sql = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administateur FROM utilisateur WHERE no_utilisateur = ?";

        Utilisateur utilisateur = null;

        try {
            utilisateur = jdbcTemplate.queryForObject(sql, new UtilisateurRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new UtilisateurNotFoundByIdException(id);
        }
        return utilisateur;
    }

    @Override
    public Utilisateur findUtilisateurByPseudo(String pseudo) {
        return null;
    }

    @Override
    public Utilisateur findUtilisateurByEmail(String email) {
        return null;
    }

    @Override
    public void saveUtilisateur(Utilisateur utilisateur) {
        String sql = "INSERT INTO Utilisateurs (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)" +
                "VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :code_postal, :ville, :mot_de_passe, :credit, :administrateur)";

        GeneratedKeyHolder  keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("pseudo", utilisateur.getPseudo());
        parameterSource.addValue("nom", utilisateur.getNom());
        parameterSource.addValue("prenom", utilisateur.getPrenom());
        parameterSource.addValue("email", utilisateur.getEmail());
        parameterSource.addValue("telephone", utilisateur.getTelephone());
        parameterSource.addValue("rue", utilisateur.getRue());
        parameterSource.addValue("code_postal", utilisateur.getCodePostal());
        parameterSource.addValue("ville", utilisateur.getVille());
        parameterSource.addValue("mot_de_passe", utilisateur.getMotDePasse());
        parameterSource.addValue("credit", utilisateur.getCredit());
        parameterSource.addValue("administrateur", utilisateur.isAdministrateur() ? 1 : 0);

        namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder, new String[]{"no_utilisateur"});

        utilisateur.setNoUtilisateur(keyHolder.getKey().longValue());
    }

    class UtilisateurRowMapper implements RowMapper<Utilisateur> {

        @Override
        public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNoUtilisateur(rs.getLong("no_utilisateur"));
            utilisateur.setPseudo(rs.getString("pseudo"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setTelephone(rs.getString("telephone"));
            utilisateur.setRue(rs.getString("rue"));
            utilisateur.setCodePostal(rs.getString("code_postal"));
            utilisateur.setVille(rs.getString("ville"));
            utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
            utilisateur.setCredit(rs.getLong("credit"));
            utilisateur.setAdministrateur(rs.getBoolean("administrateur"));

            return utilisateur;
        }
    }
}
