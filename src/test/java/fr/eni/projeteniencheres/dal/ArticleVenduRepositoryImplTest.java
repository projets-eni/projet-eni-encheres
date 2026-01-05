package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import fr.eni.projeteniencheres.dal.interfaces.UtilisateurRepository;
import fr.eni.projeteniencheres.dal.populateFakeDatas;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class ArticleVenduRepositoryImplTests {

    private final Faker faker = new Faker();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ArticleVenduRepository articleVenduRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    static ArticleVendu articleVendu = new ArticleVendu();

    static List<Utilisateur> utilisateurs = new ArrayList<>();

    static List<ArticleVendu> articles = new ArrayList<>();

    static Random random = new Random();


    @BeforeAll
    static void beforeAll(@Autowired JdbcTemplate jdbcTemplate,  @Autowired UtilisateurRepository utilisateurRepository) {

        // réinit encheres
        jdbcTemplate.update("DELETE FROM Encheres");
        // réinit ventes
        //jdbcTemplate.update("DELETE FROM ArticlesVendus");

        jdbcTemplate.update("DELETE FROM Utilisateurs");
        for(int i = 0; i < 10; i++) {
            Utilisateur user = populateFakeDatas.utilisateur();
            utilisateurRepository.saveUtilisateur(user);
            utilisateurs.add(user);
        }

        // réinit encheres
        jdbcTemplate.update("DELETE FROM Encheres");
        // réinit ventes
        jdbcTemplate.update("DELETE FROM ArticlesVendus");
    }

    /**
     * @todo datafaker (user, vente, encheres)
     */
    @Test
    @Order(1)
    void testArticleVenduSave() {

        Long id = random.nextLong(10000);

        articleVendu.setNoArticle(id);
        articleVendu.setVendeur(utilisateurs.get(random.nextInt(utilisateurs.size())));
        articleVendu.setNomArticle(faker.lorem().maxLengthSentence(30));
        articleVendu.setDescription(faker.lorem().maxLengthSentence(300));
        articleVendu.setPrixInitial(random.nextInt(1500));

        LocalDateTime debut = LocalDateTime.ofInstant(Instant.ofEpochMilli(faker.timeAndDate().future().toEpochMilli()), ZoneOffset.UTC);
        articleVendu.setDateDebutEncheres(debut);
        articleVendu.setDateFinEncheres(debut.plusWeeks(random.nextInt(7)));

        Retrait retrait = new  Retrait();
        retrait.setRue(faker.address().streetAddress());
        retrait.setCodePostal(faker.address().zipCode());
        retrait.setVille(faker.address().city());
        retrait.setEstRetire(faker.bool().bool());

        articleVendu.setRetrait(retrait);

        ArticleVendu res = articleVenduRepository.save(articleVendu);

        Assertions.assertEquals(id, res.getNoArticle());

    }

    @Test
    @Order(2)
    void testArticleVenduFindById() {
        Long id = articleVendu.getNoArticle();
        ArticleVendu foundArticle = articleVenduRepository.findById(id);
        Assertions.assertNotNull(foundArticle);
        Assertions.assertEquals(id, foundArticle.getNoArticle());
    }

}
