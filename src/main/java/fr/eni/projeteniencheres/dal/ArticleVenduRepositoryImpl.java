package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
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
    @Autowired
    private UtilisateurService utilisateurService;

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


    @Override
    public ArticleVendu findById(int id) {

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        String sql = "SELECT a.*, c.libelle, u.pseudo, r.rue, r.code_postal, r.ville " +
                "FROM ArticlesVendus AS a " +
                "INNER JOIN Categories AS c ON a.no_categorie = c.no_categorie " +
                "INNER JOIN Utilisateurs AS u ON a.no_utilisateur = u.no_utilisateur " +
                "INNER JOIN Retraits AS r ON a.no_article = r.no_article " +
                "WHERE a.no_article = :id";
        ArticleVendu article = jdbcTemplate.queryForObject(sql, params, venteRowMapper);

        sql = "SELECT e.*, u.pseudo FROM Encheres AS e INNER JOIN Utilisateurs AS u ON e.no_utilisateur=u.no_utilisateur WHERE no_article= :id" ;
        List<Enchere> encheres = jdbcTemplate.query(sql, params, enchereRowMapper);

//        try {
//            ArticleVendu article = jdbcTemplate.queryForObject(sql, params, venteRowMapper);
//            return article;
//        } catch (DataAccessException e) {
//            throw new ArticleNotFoundException("Article avec ID " + id + " non trouvé!");
//        }
        article.setEncheres(encheres);

        return article;

    }

    private final RowMapper<ArticleVendu> venteRowMapper = (rs, rowNum) -> {
        ArticleVendu article = new ArticleVendu();
        article.setNoArticle(rs.getInt("no_article"));
        article.setNomArticle(rs.getString("nom_article"));
        article.setDescription(rs.getString("description"));
        article.setDateDebutEncheres(rs.getTimestamp("date_debut_encheres").toLocalDateTime());
        article.setDateFinEncheres(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
        article.setPrixInitial(rs.getInt("prix_initial"));
        article.setPrixVente(rs.getInt("prix_vente"));
        article.setEtatVente(rs.getString("etat_vente"));
        article.setImageFilename(rs.getString("image_filename"));

        Utilisateur vendeur = new Utilisateur();
        vendeur.setNoUtilisateur(rs.getInt("no_utilisateur"));
        vendeur.setPseudo(rs.getString("pseudo"));
        article.setVendeur(vendeur);

        Retrait retrait = new Retrait();
        retrait.setNoArticle(rs.getInt("no_article"));
        retrait.setRue(rs.getString("rue"));
        retrait.setCodePostal(rs.getString("code_postal"));
        retrait.setVille(rs.getString("ville"));
        article.setRetrait(retrait);

        Categorie categorie = new Categorie();
        categorie.setNoCategorie(rs.getInt("no_categorie"));
        categorie.setLibelle(rs.getString("libelle"));
        article.setCategorie(categorie);

        return article;
    };

    private final RowMapper<Enchere> enchereRowMapper = ( rs, rowNum) -> {
        Enchere enchere = new Enchere();
        enchere.setNoEnchere(rs.getInt("no_enchere"));
        enchere.setDateEnchere(rs.getTimestamp("date_enchere").toLocalDateTime());
        enchere.setMontantEnchere(rs.getInt("montant_enchere"));

        Utilisateur acheteur = new Utilisateur();
        acheteur.setNoUtilisateur(rs.getInt("no_utilisateur"));
        acheteur.setPseudo(rs.getString("pseudo"));
        enchere.setAcheteur(acheteur);

//        ArticleVendu articleVendu = new ArticleVendu();
//        articleVendu.setNoArticle(rs.getInt("no_article"));
//        enchere.setArticleVendu(articleVendu);

        return enchere;
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
