package bg.softuni.demo.web;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CookieController.class)
class CookieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void cookiesPageDefaultsToEnglish() throws Exception {
        mockMvc.perform(get("/cookies"))
                .andExpect(status().isOk())
                .andExpect(view().name("cookies"))
                .andExpect(model().attribute("lang", "en"));
    }

    @Test
    void cookiesPageReadsLangCookie() throws Exception {
        mockMvc.perform(get("/cookies").cookie(new Cookie("lang", "bg")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("lang", "bg"));
    }

    @Test
    void submitSetsLangCookieAndRedirects() throws Exception {
        mockMvc.perform(post("/cookies").param("lang", "bg"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cookies"))
                .andExpect(cookie().value("lang", "bg"));
    }
}
