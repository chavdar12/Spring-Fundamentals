package bg.softuni.mobilele.service.impl;

import bg.softuni.mobilele.model.view.StatsView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatsServiceImplTest {

    private final StatsServiceImpl service = new StatsServiceImpl();

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void countsAuthenticatedAndAnonymousRequestsSeparately() {
        service.onRequest(); // no authentication at all

        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken(
                "key", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));
        service.onRequest();

        MobileleUser user = new MobileleUser("pesho", "pass", List.of());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        service.onRequest();
        service.onRequest();

        StatsView stats = service.getStats();
        assertEquals(2, stats.getAuthRequests());
        assertEquals(2, stats.getAnonRequests());
        assertEquals(4, stats.getTotalRequests());
    }
}
