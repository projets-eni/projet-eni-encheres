package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.Categorie;
import fr.eni.projeteniencheres.dal.interfaces.CategorieRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategorieRepositoryImpl implements CategorieRepository {

    private final JdbcTemplate jdbcTemplate;
    public CategorieRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    class CategorieRowMapper implements RowMapper<Categorie> {
        @Override
        public Categorie mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Categorie(rs.getLong("no_categorie"),rs.getString("libelle"));
        }
    }

    @Override
    public List<Categorie> afficherCategories() {
        String sql = "SELECT no_categorie, libelle FROM Categories";
        List<Categorie> categories = jdbcTemplate.query(sql, new CategorieRowMapper());
        return categories;
    }

    @Override
    public Categorie afficherCategoryParId(int id) {
        String sql = "SELECT no_categorie, libelle FROM Categories where no_categorie = ?";
        try {
            Categorie categorie = jdbcTemplate.queryForObject(sql, new CategorieRowMapper(), id);
            return categorie;

        } catch (DataAccessException ex) {
//            throw new CategorieNotFoundException("Categorie avec ID " + xx + " non trouvé!");
            throw new RuntimeException(ex) ;
        }

    }
}