package bg.softuni.mobilele;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MobileleMvcTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void guestSeesLoginAndRegister() throws Exception {
        mvc.perform(get("/"))
                .andExpect(content().string(containsString("/users/login")))
                .andExpect(content().string(not(containsString("Logout"))));
    }

    @Test
    void brandsListsSeededModels() throws Exception {
        mvc.perform(get("/brands/all"))
                .andExpect(content().string(containsString("Ford")))
                .andExpect(content().string(containsString("Fiesta")));
    }

    @Test
    void registerLoginAndLogout() throws Exception {
        mvc.perform(post("/users/register").param("firstName", "G"))
                .andExpect(model().attributeHasFieldErrors("user", "firstName", "lastName", "username", "password"));
        mvc.perform(post("/users/register").param("firstName", "Gosho").param("lastName", "Goshev")
                        .param("username", "gosho").param("password", "secret"))
                .andExpect(redirectedUrl("/users/login"));
        mvc.perform(post("/users/register").param("firstName", "Gosho").param("lastName", "Goshev")
                        .param("username", "gosho").param("password", "secret"))
                .andExpect(content().string(containsString("Username is already taken.")));

        mvc.perform(post("/users/login").param("username", "gosho").param("password", "wrong"))
                .andExpect(content().string(containsString("Invalid username or password.")));

        MockHttpSession session = new MockHttpSession();
        mvc.perform(post("/users/login").session(session).param("username", "gosho").param("password", "secret"))
                .andExpect(redirectedUrl("/"));
        mvc.perform(get("/").session(session))
                .andExpect(content().string(containsString("Welcome, Gosho")))
                .andExpect(content().string(not(containsString("navbarDropdown"))));
    }

    @Test
    void adminSeesTheAdminDropdown() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mvc.perform(post("/users/login").session(session).param("username", "admin").param("password", "topsecret"));
        mvc.perform(get("/").session(session)).andExpect(content().string(containsString("navbarDropdown")));
        mvc.perform(get("/users/logout").session(session)).andExpect(redirectedUrl("/"));
        mvc.perform(get("/").session(session)).andExpect(content().string(not(containsString("Logout"))));
    }
}
