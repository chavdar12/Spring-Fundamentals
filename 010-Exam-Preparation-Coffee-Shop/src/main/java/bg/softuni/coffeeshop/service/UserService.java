package bg.softuni.coffeeshop.service;

import bg.softuni.coffeeshop.model.User;
import bg.softuni.coffeeshop.model.UserLoginBindingModel;
import bg.softuni.coffeeshop.model.UserRegisterBindingModel;
import bg.softuni.coffeeshop.repository.UserRepository;
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

    /** Returns false when the passwords differ or the username/email is taken. */
    public boolean register(UserRegisterBindingModel model) {
        if (!model.getPassword().equals(model.getConfirmPassword())
                || userRepository.existsByUsernameOrEmail(model.getUsername(), model.getEmail())) {
            return false;
        }
        userRepository.save(new User(model.getUsername(), model.getFirstName(), model.getLastName(),
                encoder.encode(model.getPassword()), model.getEmail()));
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
