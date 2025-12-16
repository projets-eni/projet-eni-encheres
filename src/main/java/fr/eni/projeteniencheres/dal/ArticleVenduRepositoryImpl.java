//package fr.eni.projeteniencheres.dal;
//
//import fr.eni.projeteniencheres.bo.ArticleVendu;
//import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//@Repository
//public class ArticleVenduRepositoryImpl implements ArticleVenduRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//    public ArticleVenduRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
//        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
//        this.jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
//    }
////
////    class ArticleRowMapper implements RowMapper<ArticleVendu> {
////        @Override
////        public ArticleVendu mapRow(ResultSet rs, int rowNum) throws SQLException {
////            return new ArticleVendu(
////                    rs.getString("nomArticle"),
////                    rs.getString("description"),
////                    rs.getDate("dateDebutEncheres"),
////                    rs.getDate("dateFinEncheres"),
////                    rs.getInt("prixInitial"),
////                    rs.getInt("prixVente"),
////
////
////            );
////        }
////    }
//
//    @Override
//    public List<ArticleVendu> afficherArticles() {
//        String sql = "SELECT * FROM xxxxx";
//        List<ArticleVendu> articles = jdbcTemplate.query(sql, new ArticleRowMapper());
//        return articles;
//    }
//
//    @Override
//    public ArticleVendu afficherArticleParId(int id) {
//        return null;
//    }
//
//    @Override
//    public ArticleVendu ajoutArticle(ArticleVendu article) {
//        return null;
//    }
//
//    @Override
//    public ArticleVendu modifierArticle(ArticleVendu article) {
//        return null;
//    }
//
//    @Override
//    public void supprimerArticle(ArticleVendu article) {
//
//    }
//}
