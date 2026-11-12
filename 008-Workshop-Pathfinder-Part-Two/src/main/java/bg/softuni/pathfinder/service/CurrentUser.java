package bg.softuni.pathfinder.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class CurrentUser {

    private Long id;
    private String username;
    private boolean admin;

    public boolean isLoggedIn() {
        return id != null;
    }

    public void login(Long id, String username, boolean admin) {
        this.id = id;
        this.username = username;
        this.admin = admin;
    }

    public void logout() {
        login(null, null, false);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return admin;
    }
}
