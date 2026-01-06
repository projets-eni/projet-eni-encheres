package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.*;
import fr.eni.projeteniencheres.dal.interfaces.EnchereRepository;
import fr.eni.projeteniencheres.exception.EnchereImpossible;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository("enchereRepository")
public class EnchereRepositoryImpl implements EnchereRepository {

    NamedParameterJdbcTemplate jdbcTemplate;
    JdbcTemplate sJdbcTemplate;

    public EnchereRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sJdbcTemplate = new JdbcTemplate(jdbcTemplate.getJdbcTemplate().getDataSource());  // Création d'un JdbcTemplate à partir du DataSource de NamedParameterJdbcTemplate
    }

    public final RowMapper<Enchere> rowMapper = (rs, rowNum) -> {
        Enchere enchere = new Enchere();

//        BeanUtils.copyProperties(rs, enchere);
        enchere.setNoEnchere(rs.getInt("no_enchere"));
        enchere.setDateEnchere(rs.getTimestamp("date_enchere").toLocalDateTime());
        enchere.setMontantEnchere(rs.getInt("montant_enchere"));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNoUtilisateur(rs.getInt("no_utilisateur"));
        utilisateur.setPseudo(rs.getString("pseudo"));
        enchere.setAcheteur(utilisateur);

        ArticleVendu articleVendu = new ArticleVendu();
        articleVendu.setNoArticle(rs.getInt("no_article"));
        enchere.setArticleVendu(articleVendu);

        return enchere;
    };

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
        return jdbcTemplate.query("select e.*, pseudo from Encheres AS e LEFT JOIN Utilisateurs AS u ON e.no_utilisateur=u.no_utilisateur where no_article=:noArticle", parameterSource, rowMapper);
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
        // Créer une instance de SimpleJdbcCall pour appeler la procédure stockée
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(sJdbcTemplate)
                .withProcedureName("placerEnchere") // Le nom de la procédure stockée
                .declareParameters(
                        new SqlParameter("montantEnchere", Types.INTEGER),  // Paramètre d'entrée
                        new SqlParameter("noArticle", Types.INTEGER),      // Paramètre d'entrée
                        new SqlParameter("noUtilisateur", Types.INTEGER),  // Paramètre d'entrée
                        new SqlOutParameter("EnchereId", Types.INTEGER),   // Paramètre de sortie pour l'IDENTITY
                        new SqlOutParameter("errorMessage", Types.VARCHAR) // Paramètre de sortie pour le message d'erreur
                );

        // Créer un Map pour passer les paramètres d'entrée
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("montantEnchere", enchere.getMontantEnchere());
        inParams.put("noArticle", enchere.getArticleVendu().getNoArticle());
        inParams.put("noUtilisateur", enchere.getAcheteur().getNoUtilisateur());

        // Appeler la procédure stockée
        Map<String, Object> outParams = simpleJdbcCall.execute(new MapSqlParameterSource(inParams));

        // Récupérer l'IDENTITY (le ID de l'enchère insérée) et le message d'erreur
        Integer enchereId = (Integer) outParams.get("EnchereId");
        String errorMessage = (String) outParams.get("errorMessage");

        // Gérer l'erreur si nécessaire
        if (errorMessage != null && !errorMessage.isEmpty()) {
            System.out.println("Erreur : " + errorMessage);
            throw new EnchereImpossible("Erreur : " + errorMessage);
        }

        // Retourner l'IDENTITY de l'enchère insérée
        return enchereId;
    }

    @Override
    public Enchere getMeilleureOffreByArticle(ArticleVendu article) {
        List<Enchere> encheres = findByVente(article);
        Enchere enchere = encheres.stream()
                .max(Comparator.comparingInt(Enchere::getMontantEnchere))
                .orElse(null);

        return enchere ;
    }

    @Override
    public Enchere getLastOffreByArticleAndUserName(ArticleVendu article, String username) {
        List<Enchere> encheres = findByVente(article);
        Enchere encheresMonArticle = encheres.stream()
                                        .filter(enchere -> enchere.getAcheteur().getPseudo().equals(username))
                                        .max(Comparator.comparingInt(Enchere::getMontantEnchere))
                                        .orElse(null);
        return encheresMonArticle ;
    }


}