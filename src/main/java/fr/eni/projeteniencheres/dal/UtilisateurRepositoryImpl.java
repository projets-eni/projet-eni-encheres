package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.Categorie;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dal.interfaces.UtilisateurRepository;
import fr.eni.projeteniencheres.exception.UtilisateurExisteDeja;
import fr.eni.projeteniencheres.exception.UtilisateurNotFoundByIdException;
import fr.eni.projeteniencheres.exception.UtilisateurNotFoundByPseudoException;
import fr.eni.projeteniencheres.exception.UtilisateurNotFoundByPseudoOrEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UtilisateurRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Utilisateur> findAllUtilisateurs() {
        String sql = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur FROM Utilisateurs WHERE deleted_at IS NULL";
        List<Utilisateur> utilisateurs = jdbcTemplate.query(sql, new UtilisateurRowMapper());
        return utilisateurs;
    }

    @Override
    public Utilisateur findUtilisateurById(long id) {
        String sql = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur FROM Utilisateurs WHERE no_utilisateur = ? AND deleted_at IS NULL";

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
        String sql = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur FROM Utilisateurs WHERE pseudo = ? AND deleted_at IS NULL";

        Utilisateur utilisateur = null;

        try {
            utilisateur = jdbcTemplate.queryForObject(sql, new UtilisateurRowMapper(), pseudo);
        } catch (EmptyResultDataAccessException ex) {
            throw new UtilisateurNotFoundByPseudoException(pseudo);
        }
        return utilisateur;
    }

    @Override
    public Utilisateur findUtilisateurByEmail(String email) {
        String sql = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur FROM Utilisateurs where email = ? AND deleted_at IS NULL";
        try {
            Utilisateur vendeur = jdbcTemplate.queryForObject(sql, new UtilisateurRowMapper(), email);
            return vendeur;

        } catch (DataAccessException ex) {
//            throw new UtilisateurNotFoundByEmailException(ex);
            throw new RuntimeException(ex) ;
        }
    }

    @Override
    public Optional<Utilisateur> findUtilisateurByPseudoOrEmail(String pseudo, String email) {
        String sql = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur FROM Utilisateurs WHERE (pseudo = :pseudo OR email = :email) AND deleted_at IS NULL";
        Utilisateur utilisateur = null;
        Map<String, Object> params = new HashMap<>();
        params.put("pseudo", pseudo);
        params.put("email", email);

        try {
            utilisateur = namedParameterJdbcTemplate.queryForObject(sql, params, new UtilisateurRowMapper());
            return Optional.of(utilisateur);
        } catch (EmptyResultDataAccessException ex) {
            throw new UtilisateurNotFoundByPseudoOrEmailException(pseudo);
        }
    }


    @Override
    public void saveUtilisateur(Utilisateur utilisateur) {

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        if (Long.valueOf(utilisateur.getNoUtilisateur()) < 1) {
            String sql = "INSERT INTO Utilisateurs (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)" +
                    "VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :code_postal, :ville, :mot_de_passe, :credit, :administrateur)";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

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

            try {
                namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder, new String[]{"no_utilisateur"});
            } catch (DuplicateKeyException ex) {
                throw new UtilisateurExisteDeja("Utilisateur déjà existant");
            }

            utilisateur.setNoUtilisateur(keyHolder.getKey().longValue());

        } else {

            String sql = "UPDATE Utilisateurs SET pseudo=:pseudo, nom=:nom, prenom=:prenom, email=:email, " +
                    "telephone=:telephone, rue=:rue, code_postal=:code_postal, ville=:ville, mot_de_passe=:mot_de_passe, " +
                    "credit=:credit, administrateur=:administrateur " +
                    "WHERE no_utilisateur = :noUtilisateur";

            parameterSource.addValue("noUtilisateur", utilisateur.getNoUtilisateur());
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

            namedParameterJdbcTemplate.update(sql, parameterSource);

        }
    }

    @Override
    public void deleteUtilisateurById(long no_utilisateur) {
        // Soft delete
        String sql = "UPDATE Utilisateurs SET deleted_at = GETDATE() WHERE no_utilisateur = ?";
        jdbcTemplate.update(sql, no_utilisateur);
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
