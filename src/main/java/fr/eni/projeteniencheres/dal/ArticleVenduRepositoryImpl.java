package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.*;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import fr.eni.projeteniencheres.dal.interfaces.EnchereRepository;
import fr.eni.projeteniencheres.exception.ArticleNotFoundException;
import fr.eni.projeteniencheres.exception.EnchereImpossible;
import fr.eni.projeteniencheres.exception.EtatVenteErreur;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ArticleVenduRepositoryImpl implements ArticleVenduRepository {

    @Autowired
    private EnchereRepository enchereRepository;

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String rqtSelect = "select v.* from ArticlesVendus v ";

    public ArticleVenduRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ArticleVendu> findAll() {
        return jdbcTemplate.query(this.rqtSelect, Map.of(), venteRowMapper);
    }

    @Override
    public List<ArticleVendu> findEnCours() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("dateDebut", "GETUTCDATE()");
        return jdbcTemplate.query(this.rqtSelect
                + " WHERE v.date_fin_encheres < :dateDebut and v.date_debut_encheres <= :dateDebut",
                params, venteRowMapper);
    }

    public List<ArticleVendu> findTermines() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("dateFin", "GETUTCDATE()");
        return jdbcTemplate.query(this.rqtSelect
                        + " WHERE v.date_fin_encheres <= :dateFin",
                params, venteRowMapper);
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
        return List.of();
    }


    /**
     * Pour modifier le statut d'une vente avec la procédure stockée terminerVentes
     * si articleVendu est null, prend toutes les ventes avec etat_vente différent de "Terminée"
     * @param articleVendu
     * @return Nombre de vente impactée
     */
    @Override
    public Integer terminerVente(ArticleVendu articleVendu) {
        Integer res = null;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("noArticle", articleVendu.getNoArticle());
        try {
            res = jdbcTemplate.update("EXEC terminerVentes :noArticle", params);
        } catch (RuntimeException ex) {
            throw new EtatVenteErreur(ex.getMessage());
        }
        return res;
    }


    @Override
    public ArticleVendu findById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        String sql = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres," +
                "prix_initial, prix_vente, etat_vente, no_utilisateur, no_categorie, image_filename " +
                "FROM ArticlesVendus WHERE no_article = :id";
        try {
            ArticleVendu article = jdbcTemplate.queryForObject(sql, params, venteRowMapper);
            return article;
        } catch (DataAccessException e) {
            throw new ArticleNotFoundException("Article avec ID " + id + " non trouvé!");
        }
    }

    @Override
    public List<ArticleVendu> findById(List<Integer> ids) {
        return jdbcTemplate.query(rqtSelect + " WHERE v.no_article IN ("
                + ids.stream().map(String::valueOf).collect(Collectors.joining(", "))
                + ")", new MapSqlParameterSource(), venteRowMapper);
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

        Utilisateur vendeur = new Utilisateur();
        vendeur.setNoUtilisateur(rs.getInt("no_utilisateur"));
        articleVendu.setVendeur(vendeur);

        Retrait retrait = new Retrait();
        retrait.setNoArticle(rs.getInt("no_article"));
        articleVendu.setRetrait(retrait);

        return articleVendu;
    };


    @Override
    public ArticleVendu ajoutArticle(ArticleVendu article) {

        // ----------------------------------------------------------- //
        // --           Création d'un ArticleVendu                  -- //
        // ----------------------------------------------------------- //

        String sqlArticle = "INSERT INTO ArticlesVendus (nom_article, description, " +
                "date_debut_encheres, date_fin_encheres, prix_initial, image_filename, etat_vente, no_categorie, no_utilisateur) " +
                "VALUES (:nom_article, :description, :date_debut_encheres, :date_fin_encheres, " +
                ":prix_initial, :image_filename, :etat_vente, :no_categorie, :no_utilisateur)";

        // Crée un "contenant" vide qui va recevoir l'ID généré par MySQL.
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource paramsArticle = new MapSqlParameterSource();
        paramsArticle.addValue("nom_article", article.getNomArticle());
        paramsArticle.addValue("description", article.getDescription());
        paramsArticle.addValue("date_debut_encheres", article.getDateDebutEncheres());
        paramsArticle.addValue("date_fin_encheres", article.getDateFinEncheres());
        paramsArticle.addValue("prix_initial", article.getPrixInitial());
        paramsArticle.addValue("image_filename", article.getImageFilename());
        paramsArticle.addValue("etat_vente", article.getEtatVente());
        // ajout des relations de FK
        paramsArticle.addValue("no_categorie", article.getCategorie().getNoCategorie());
        paramsArticle.addValue("no_utilisateur", article.getVendeur().getNoUtilisateur());

        // Exécute l'INSERT ET remplit automatiquement keyHolder avec l'ID généré.
        //namedParameterJdbcTemplate.update(sqlFilm, paramsFilm, keyHolder);
        jdbcTemplate.update(sqlArticle, paramsArticle, keyHolder, new String[]{"no_article"});

        // ré-injecter l'id dans l'objet retourné
        long generatedId = keyHolder.getKey().longValue();
        article.setNoArticle(generatedId);

        // retrait créé dans RetraitRepositoryImpl

        // pas d'enchère à la création -> pas d'insertion dans Encheres
        // l'utilisateur est existant en BDD -> pas d'insertion dans Utilisateurs
        // les catégories sont prédéfinies -> pas d'insertion dans Categories

        return article;

    }


}
