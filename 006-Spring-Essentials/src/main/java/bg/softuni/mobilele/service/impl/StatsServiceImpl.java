package bg.softuni.mobilele.service.impl;

import bg.softuni.mobilele.model.view.StatsView;
import bg.softuni.mobilele.service.StatsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StatsServiceImpl implements StatsService {

    // singleton shared by all request threads
    private final AtomicInteger anonymousRequests = new AtomicInteger();
    private final AtomicInteger authRequests = new AtomicInteger();

    @Override
    public void onRequest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && (authentication.getPrincipal() instanceof UserDetails)) {
            authRequests.incrementAndGet();
        } else {
            anonymousRequests.incrementAndGet();
        }
    }

    @Override
    public StatsView getStats() {
        return new StatsView(authRequests.get(), anonymousRequests.get());
    }
}
