package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleVenduRepositoryImpl implements ArticleVenduRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public ArticleVenduRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
    }

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
        namedParameterJdbcTemplate.update(sqlArticle, paramsArticle, keyHolder, new String[]{"no_article"});

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
