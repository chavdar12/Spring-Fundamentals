package bg.softuni.mobilele.service;

import bg.softuni.mobilele.model.binding.UserRegisterBindingModel;
import bg.softuni.mobilele.model.entity.UserEntity;
import bg.softuni.mobilele.model.entity.enums.UserRoleEnum;
import bg.softuni.mobilele.repository.UserRepository;
import bg.softuni.mobilele.repository.UserRoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository users;
    private final UserRoleRepository roles;
    private final CurrentUser currentUser;

    public UserService(UserRepository users, UserRoleRepository roles, CurrentUser currentUser) {
        this.users = users;
        this.roles = roles;
        this.currentUser = currentUser;
    }

    /** Registers a regular user; returns false when the username is taken. */
    public boolean register(UserRegisterBindingModel model) {
        if (users.findByUsernameIgnoreCase(model.getUsername()).isPresent()) {
            return false;
        }
        users.save(new UserEntity().setUsername(model.getUsername()).setFirstName(model.getFirstName())
                .setLastName(model.getLastName()).setPassword(encoder.encode(model.getPassword()))
                .setActive(true).setRoles(Set.of(roles.findByRole(UserRoleEnum.USER))));
        return true;
    }

    public boolean login(String username, String password) {
        return users.findByUsername(username)
                .filter(user -> user.isActive() && encoder.matches(password, user.getPassword()))
                .map(user -> {
                    currentUser.login(user.getUsername(), user.getFirstName(),
                            user.getRoles().stream().anyMatch(r -> r.getRole() == UserRoleEnum.ADMIN));
                    return true;
                })
                .orElse(false);
    }

    public void logout() {
        currentUser.logout();
    }
}
