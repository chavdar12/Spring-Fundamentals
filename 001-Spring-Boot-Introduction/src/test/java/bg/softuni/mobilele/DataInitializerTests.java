package bg.softuni.mobilele;

import bg.softuni.mobilele.init.DataInitializer;
import bg.softuni.mobilele.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DataInitializerTests {

    @Autowired
    private DataInitializer initializer;

    @Autowired
    private BrandRepository brands;

    @Autowired
    private ModelRepository models;

    @Autowired
    private UserRepository users;

    @Autowired
    private OfferRepository offers;

    @Test
    void populatesTheDatabaseOnce() {
        initializer.run(); // second run must not duplicate the data

        assertThat(brands.count()).isEqualTo(2);
        assertThat(models.count()).isEqualTo(3);
        assertThat(users.count()).isEqualTo(2);
        assertThat(offers.count()).isEqualTo(1);
        assertThat(users.findByUsername("admin").orElseThrow().getRoles()).hasSize(2);
        assertThat(users.findByUsername("admin").orElseThrow().getPassword()).startsWith("$2a$");
    }
}
