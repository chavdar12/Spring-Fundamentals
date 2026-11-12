package bg.softuni.shoppinglist;

import bg.softuni.shoppinglist.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingListFlowTests {

    private static final String TOMORROW = java.time.LocalDateTime.now().plusDays(1).withNano(0).withSecond(0)
            .toString();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository products;

    private MockHttpSession session;

    @BeforeEach
    void login() throws Exception {
        products.deleteAll();
        String username = "user" + System.nanoTime() % 100000;
        mvc.perform(post("/users/register")
                .param("username", username).param("email", username + "@mail.bg")
                .param("password", "123").param("confirmPassword", "123"));
        session = new MockHttpSession();
        mvc.perform(post("/users/login").session(session).param("username", username).param("password", "123"))
                .andExpect(redirectedUrl("/home"));
    }

    private void add(String name, String price, String category) throws Exception {
        mvc.perform(post("/products/add").session(session)
                        .param("name", name).param("description", "Something tasty")
                        .param("neededBefore", TOMORROW).param("price", price).param("category", category))
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void guestIsSentToLogin() throws Exception {
        mvc.perform(get("/home")).andExpect(redirectedUrl("/users/login"));
        mvc.perform(get("/products/buy-all")).andExpect(redirectedUrl("/users/login"));
    }

    @Test
    void invalidProductShowsMessages() throws Exception {
        mvc.perform(post("/products/add").session(session)
                        .param("name", "ab").param("description", "abc")
                        .param("neededBefore", "2000-01-01T10:00").param("price", "0"))
                .andExpect(view().name("product-add"))
                .andExpect(content().string(containsString("The date cannot be in the past.")))
                .andExpect(model().attributeHasFieldErrors("product",
                        "name", "description", "neededBefore", "price", "category"));
    }

    @Test
    void productsAreGroupedBoughtAndBoughtAll() throws Exception {
        add("Bread", "1.50", "FOOD");
        add("Beer", "2.00", "DRINK");
        add("Soap", "3.00", "HOUSEHOLD");

        mvc.perform(get("/home").session(session))
                .andExpect(model().attribute("totalPrice", new BigDecimal("6.50")))
                .andExpect(content().string(containsString("Name: Bread Price: 1.50 lv")))
                .andExpect(content().string(containsString("Buy all products for 6.50 lv")));

        Long bread = products.findAll().stream().filter(p -> p.getName().equals("Bread")).findFirst().orElseThrow().getId();
        mvc.perform(get("/products/buy/" + bread).session(session)).andExpect(redirectedUrl("/home"));
        assertThat(products.count()).isEqualTo(2);

        mvc.perform(get("/products/buy-all").session(session)).andExpect(redirectedUrl("/home"));
        assertThat(products.count()).isZero();
    }
}
