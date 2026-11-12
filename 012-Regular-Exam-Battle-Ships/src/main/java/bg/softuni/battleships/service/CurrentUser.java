package bg.softuni.battleships.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class CurrentUser {

    private Long id;
    private String username;

    public boolean isLoggedIn() {
        return id != null;
    }

    public void login(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public void logout() {
        id = null;
        username = null;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
