package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.binding.UserRegisterBindingModel;
import bg.softuni.pathfinder.model.entity.Level;
import bg.softuni.pathfinder.model.entity.RoleName;
import bg.softuni.pathfinder.model.entity.UserEntity;
import bg.softuni.pathfinder.repository.RoleRepository;
import bg.softuni.pathfinder.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository users;
    private final RoleRepository roles;
    private final CurrentUser currentUser;

    public UserService(UserRepository users, RoleRepository roles, CurrentUser currentUser) {
        this.users = users;
        this.roles = roles;
        this.currentUser = currentUser;
    }

    /** Every new user starts as a BEGINNER with the USER role; false when the username is taken. */
    public boolean register(UserRegisterBindingModel model) {
        if (users.existsByUsername(model.getUsername())) {
            return false;
        }
        UserEntity user = new UserEntity().setUsername(model.getUsername()).setFullName(model.getFullName())
                .setEmail(model.getEmail()).setAge(model.getAge()).setLevel(Level.BEGINNER)
                .setPassword(encoder.encode(model.getPassword()));
        user.getRole().add(roles.findByName(RoleName.USER).orElseThrow());
        users.save(user);
        return true;
    }

    public boolean login(String username, String password) {
        return users.findByUsername(username)
                .filter(user -> encoder.matches(password, user.getPassword()))
                .map(user -> {
                    currentUser.login(user.getId(), user.getUsername(), user.hasRole(RoleName.ADMIN));
                    return true;
                })
                .orElse(false);
    }

    public void logout() {
        currentUser.logout();
    }

    public Optional<UserEntity> findById(Long id) {
        return users.findById(id);
    }
}
