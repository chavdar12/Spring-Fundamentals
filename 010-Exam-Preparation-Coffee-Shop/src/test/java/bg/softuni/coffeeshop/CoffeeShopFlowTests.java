package bg.softuni.coffeeshop;

import bg.softuni.coffeeshop.repository.OrderRepository;
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
class CoffeeShopFlowTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderRepository orders;

    private MockHttpSession registerAndLogin(String username) throws Exception {
        mvc.perform(post("/users/register")
                        .param("username", username).param("lastName", "Peshev")
                        .param("email", username + "@mail.bg")
                        .param("password", "123").param("confirmPassword", "123"))
                .andExpect(redirectedUrl("/users/login"));
        MockHttpSession session = new MockHttpSession();
        mvc.perform(post("/users/login").session(session)
                        .param("username", username).param("password", "123"))
                .andExpect(redirectedUrl("/home"));
        return session;
    }

    @Test
    void guestCannotOpenHomeOrAddOrder() throws Exception {
        mvc.perform(get("/home")).andExpect(redirectedUrl("/users/login"));
        mvc.perform(get("/orders/add")).andExpect(redirectedUrl("/users/login"));
        mvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
    }

    @Test
    void invalidRegistrationShowsErrors() throws Exception {
        mvc.perform(post("/users/register")
                        .param("username", "ab").param("lastName", "x")
                        .param("email", "nope").param("password", "1").param("confirmPassword", "1"))
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "username", "lastName", "email", "password"));
    }

    @Test
    void wrongPasswordShowsMessage() throws Exception {
        registerAndLogin("georgi");
        mvc.perform(post("/users/login").param("username", "georgi").param("password", "999"))
                .andExpect(view().name("login"))
                .andExpect(content().string(containsString("Wrong username and password combination.")));
    }

    @Test
    void orderIsAddedListedAndReadied() throws Exception {
        MockHttpSession session = registerAndLogin("pesho1");

        mvc.perform(get("/users/login").session(session)).andExpect(redirectedUrl("/home"));

        mvc.perform(post("/orders/add").session(session)
                        .param("name", "Cake").param("price", "-1").param("orderTime", "2999-01-01T10:00")
                        .param("description", "abc"))
                .andExpect(model().attributeHasFieldErrors("order", "price", "orderTime", "category", "description"));

        mvc.perform(post("/orders/add").session(session)
                        .param("name", "Espresso").param("price", "2.50").param("orderTime", "2021-10-15T10:00")
                        .param("category", "COFFEE").param("description", "Double shot"))
                .andExpect(redirectedUrl("/home"));
        mvc.perform(post("/orders/add").session(session)
                        .param("name", "Rainbow").param("price", "6").param("orderTime", "2021-10-15T10:00")
                        .param("category", "CAKE").param("description", "Big slice"))
                .andExpect(redirectedUrl("/home"));

        mvc.perform(get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalTime", 12))
                .andExpect(content().string(containsString("Employee - pesho1")))
                .andExpect(content().string(containsString("Number of orders: 2")));

        Long id = orders.findAllByOrderByPriceDesc().getFirst().getId();
        assertThat(orders.findAllByOrderByPriceDesc().getFirst().getName()).isEqualTo("Rainbow");
        mvc.perform(get("/orders/ready/" + id).session(session)).andExpect(redirectedUrl("/home"));
        assertThat(orders.findById(id)).isEmpty();
    }
}
