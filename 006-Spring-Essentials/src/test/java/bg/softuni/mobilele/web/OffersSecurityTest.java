package bg.softuni.mobilele.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Relies on DBInit seed data: offer 1 is sold by "pesho" (USER), offer 2 by "admin" (ADMIN)
@SpringBootTest
@AutoConfigureMockMvc
class OffersSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void anonymousUserIsRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/offers/all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    @WithUserDetails("pesho")
    void sellerCanEditOwnOffer() throws Exception {
        mockMvc.perform(get("/offers/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("update"));
    }

    @Test
    @WithUserDetails("pesho")
    void userCannotEditSomeoneElsesOffer() throws Exception {
        mockMvc.perform(get("/offers/2/edit")).andExpect(status().isForbidden());
        mockMvc.perform(patch("/offers/2/edit").with(csrf())).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void adminCanEditAnyOffer() throws Exception {
        mockMvc.perform(get("/offers/1/edit")).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("pesho")
    void missingOfferReturnsNotFound() throws Exception {
        mockMvc.perform(get("/offers/999/details")).andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("pesho")
    void statisticsAreAdminOnly() throws Exception {
        mockMvc.perform(get("/statistics")).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void adminCanSeeStatistics() throws Exception {
        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("stats"));
    }
}
