package bg.softuni.shoppinglist.service;

import bg.softuni.shoppinglist.model.User;
import bg.softuni.shoppinglist.model.UserLoginBindingModel;
import bg.softuni.shoppinglist.model.UserRegisterBindingModel;
import bg.softuni.shoppinglist.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    public UserService(UserRepository userRepository, CurrentUser currentUser) {
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    /** Returns false when the passwords differ or the username is taken. */
    public boolean register(UserRegisterBindingModel model) {
        if (!model.getPassword().equals(model.getConfirmPassword())
                || userRepository.existsByUsernameOrEmail(model.getUsername(), model.getEmail())) {
            return false;
        }
        userRepository.save(new User(model.getUsername(), encoder.encode(model.getPassword()), model.getEmail()));
        return true;
    }

    public boolean login(UserLoginBindingModel model) {
        return userRepository.findByUsername(model.getUsername())
                .filter(user -> encoder.matches(model.getPassword(), user.getPassword()))
                .map(user -> {
                    currentUser.login(user.getId(), user.getUsername());
                    return true;
                })
                .orElse(false);
    }

    public void logout() {
        currentUser.logout();
    }
}
