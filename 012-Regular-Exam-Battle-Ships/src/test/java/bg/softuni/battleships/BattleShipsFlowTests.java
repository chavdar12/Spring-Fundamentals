package bg.softuni.battleships;

import bg.softuni.battleships.model.Ship;
import bg.softuni.battleships.repository.ShipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BattleShipsFlowTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ShipRepository ships;

    private MockHttpSession registerAndLogin(String username) throws Exception {
        mvc.perform(post("/users/register")
                        .param("username", username).param("fullName", "Full " + username)
                        .param("email", username + "@mail.bg")
                        .param("password", "123").param("confirmPassword", "123"))
                .andExpect(redirectedUrl("/users/login"));
        MockHttpSession session = new MockHttpSession();
        mvc.perform(post("/users/login").session(session)
                        .param("username", username).param("password", "123"))
                .andExpect(redirectedUrl("/home"));
        return session;
    }

    private void addShip(MockHttpSession session, String name, int health, int power) throws Exception {
        mvc.perform(post("/ships/add").session(session)
                        .param("name", name).param("health", "" + health).param("power", "" + power)
                        .param("created", "2021-10-24").param("category", "BATTLE"))
                .andExpect(redirectedUrl("/home"));
    }

    private Ship ship(String name) {
        return ships.findAll().stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }

    @Test
    void guestIsSentToLogin() throws Exception {
        mvc.perform(get("/home")).andExpect(redirectedUrl("/users/login"));
        mvc.perform(get("/ships/add")).andExpect(redirectedUrl("/users/login"));
    }

    @Test
    void mismatchedPasswordsDoNotRegister() throws Exception {
        mvc.perform(post("/users/register")
                        .param("username", "misha").param("fullName", "Misha Mishev")
                        .param("email", "misha@mail.bg").param("password", "123").param("confirmPassword", "456"))
                .andExpect(view().name("register"));
        mvc.perform(post("/users/login").param("username", "misha").param("password", "123"))
                .andExpect(model().attribute("badCredentials", true));
    }

    @Test
    void invalidShipShowsErrors() throws Exception {
        MockHttpSession session = registerAndLogin("vasko");
        mvc.perform(post("/ships/add").session(session)
                        .param("name", "X").param("health", "0").param("power", "-1")
                        .param("created", "2999-01-01"))
                .andExpect(view().name("ship-add"))
                .andExpect(model().attributeHasFieldErrors("ship", "name", "health", "power", "created", "category"));
    }

    @Test
    void firingReducesHealthAndSinksTheDefender() throws Exception {
        MockHttpSession pesho = registerAndLogin("pesho");
        MockHttpSession gosho = registerAndLogin("gosho");
        addShip(pesho, "Titanic", 100, 60);
        addShip(gosho, "Nautilus", 100, 10);

        mvc.perform(get("/home").session(pesho))
                .andExpect(content().string(containsString("Nautilus -- 100 -- 10")))
                .andExpect(content().string(containsString("Titanic -- 100 -- 60")));

        Long titanic = ship("Titanic").getId();
        Long nautilus = ship("Nautilus").getId();

        // Gosho cannot fire with Pesho's ship
        mvc.perform(post("/battle").session(gosho)
                .param("attackerId", "" + titanic).param("defenderId", "" + titanic));
        assertThat(ship("Titanic").getHealth()).isEqualTo(100);

        mvc.perform(post("/battle").session(pesho)
                        .param("attackerId", "" + titanic).param("defenderId", "" + nautilus))
                .andExpect(redirectedUrl("/home"));
        assertThat(ship("Nautilus").getHealth()).isEqualTo(40);

        mvc.perform(post("/battle").session(pesho)
                .param("attackerId", "" + titanic).param("defenderId", "" + nautilus));
        assertThat(ship("Nautilus")).isNull();
    }
}
