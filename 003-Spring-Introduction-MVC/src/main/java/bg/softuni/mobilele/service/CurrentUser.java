package bg.softuni.mobilele.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class CurrentUser {

    private String username;
    private String firstName;
    private boolean admin;

    public boolean isLoggedIn() {
        return username != null;
    }

    public void login(String username, String firstName, boolean admin) {
        this.username = username;
        this.firstName = firstName;
        this.admin = admin;
    }

    public void logout() {
        login(null, null, false);
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean isAdmin() {
        return admin;
    }
}
