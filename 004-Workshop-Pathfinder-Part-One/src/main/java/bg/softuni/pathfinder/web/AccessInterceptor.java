package bg.softuni.pathfinder.web;

import bg.softuni.pathfinder.service.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Login and register are for guests only; home, profile, logout and adding routes need a login. */
@Configuration
public class AccessInterceptor implements WebMvcConfigurer {

    private final CurrentUser currentUser;

    public AccessInterceptor(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(redirectWhen(false, "/users/login"))
                .addPathPatterns("/home", "/users/profile", "/users/logout", "/routes/add");
        registry.addInterceptor(redirectWhen(true, "/home"))
                .addPathPatterns("/users/login", "/users/register");
    }

    private HandlerInterceptor redirectWhen(boolean loggedIn, String target) {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                    throws Exception {
                if (currentUser.isLoggedIn() == loggedIn) {
                    response.sendRedirect(target);
                    return false;
                }
                return true;
            }
        };
    }
}
