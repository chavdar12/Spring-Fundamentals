package bg.softuni.battleships.web;

import bg.softuni.battleships.service.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

/** Guests may only see the index, login and register pages; logged-in users everything else. */
@Configuration
public class AccessInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    private static final Set<String> GUEST_PAGES = Set.of("/", "/users/login", "/users/register");

    private final CurrentUser currentUser;

    public AccessInterceptor(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).excludePathPatterns("/css/**", "/images/**", "/error");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean guestPage = GUEST_PAGES.contains(request.getRequestURI());
        if (guestPage && currentUser.isLoggedIn()) {
            response.sendRedirect("/home");
            return false;
        }
        if (!guestPage && !currentUser.isLoggedIn()) {
            response.sendRedirect("/users/login");
            return false;
        }
        return true;
    }
}
