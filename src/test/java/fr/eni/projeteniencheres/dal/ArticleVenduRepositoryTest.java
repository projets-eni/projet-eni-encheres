package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.ArticleVendu;
import fr.eni.projeteniencheres.dal.interfaces.ArticleVenduRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class ArticleVenduRepositoryImplTests {

    private final Faker faker = new Faker();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ArticleVenduRepository articleVenduRepository;

    static ArticleVendu articleVendu = new ArticleVendu();

    @BeforeAll
    static void beforeAll(@Autowired JdbcTemplate jdbcTemplate) {
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
    void () {
    }

}
