package bg.softuni.pathfinder;

import bg.softuni.pathfinder.repository.RouteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PathfinderTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RouteRepository routes;

    private MockHttpSession login(String username) throws Exception {
        MockHttpSession session = new MockHttpSession();
        mvc.perform(post("/users/login").session(session).param("username", username).param("password", "12345"))
                .andExpect(redirectedUrl("/home"));
        return session;
    }

    @Test
    void guestPagesAndNavigation() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(containsString("/users/register")))
                .andExpect(content().string(not(containsString("Logout"))));
        mvc.perform(get("/about")).andExpect(status().isOk());
        mvc.perform(get("/home")).andExpect(redirectedUrl("/users/login"));
        mvc.perform(get("/routes/add")).andExpect(redirectedUrl("/users/login"));
    }

    @Test
    void seededRoutesAreListedAndFilteredByCategory() throws Exception {
        mvc.perform(get("/routes")).andExpect(content().string(containsString("Връх Кумата")));
        Long id = routes.findAll().getFirst().getId();
        mvc.perform(get("/routes/details/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Admin Adminov")));
        mvc.perform(get("/routes/pedestrian")).andExpect(view().name("category"));
        mvc.perform(get("/routes/details/9999")).andExpect(status().isNotFound());
    }

    @Test
    void registerKeepsInputAndErrorsAcrossTheRedirect() throws Exception {
        var flash = mvc.perform(post("/users/register")
                        .param("username", "pe").param("fullName", "Pesho Peshev").param("email", "bad")
                        .param("age", "20").param("password", "12345").param("confirmPassword", "12345"))
                .andExpect(redirectedUrl("/users/register"))
                .andReturn().getFlashMap();
        mvc.perform(get("/users/register").flashAttrs(flash))
                .andExpect(content().string(containsString("value=\"Pesho Peshev\"")))
                .andExpect(model().attributeHasFieldErrors("userModel", "username", "email"));

        mvc.perform(post("/users/register")
                        .param("username", "pesho").param("fullName", "Pesho Peshev").param("email", "p@p.bg")
                        .param("age", "20").param("password", "12345").param("confirmPassword", "12345"))
                .andExpect(redirectedUrl("/users/login"));
        MockHttpSession session = login("pesho");
        mvc.perform(get("/users/profile").session(session)).andExpect(redirectedUrlPattern("/users/profile/*"));
        mvc.perform(get("/users/login").session(session)).andExpect(redirectedUrl("/home"));
    }

    @Test
    void adminSeesAdminLinkAndCanAddARoute() throws Exception {
        MockHttpSession admin = login("admin");
        mvc.perform(get("/home").session(admin)).andExpect(content().string(containsString(">Admin<")));

        mvc.perform(multipart("/routes/add").session(admin).param("name", "x"))
                .andExpect(redirectedUrl("/routes/add"))
                .andExpect(flash().attributeExists("routeModel"));

        MockMultipartFile gpx = new MockMultipartFile("gpxCoordinates", "track.gpx", "application/gpx+xml",
                "<gpx></gpx>".getBytes());
        mvc.perform(multipart("/routes/add").file(gpx).session(admin)
                        .param("name", "Vitosha Loop").param("description", "A short loop on Vitosha.")
                        .param("level", "BEGINNER").param("videoUrl", "dQw4w9WgXcQ").param("categories", "PEDESTRIAN"))
                .andExpect(redirectedUrlPattern("/routes/details/*"));
        assertThat(routes.existsByName("Vitosha Loop")).isTrue();
        mvc.perform(get("/routes/pedestrian")).andExpect(content().string(containsString("Vitosha Loop")));
    }
}
