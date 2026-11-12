package bg.softuni.mobilele.web;

import bg.softuni.mobilele.model.binding.UserRegisterBindingModel;
import bg.softuni.mobilele.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("user") UserRegisterBindingModel user) {
        return "auth-register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegisterBindingModel user, BindingResult result) {
        if (!result.hasFieldErrors("username") && !result.hasErrors() && !userService.register(user)) {
            result.rejectValue("username", "taken", "Username is already taken.");
        }
        return result.hasErrors() ? "auth-register" : "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login() {
        return "auth-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        if (userService.login(username, password)) {
            return "redirect:/";
        }
        model.addAttribute("badCredentials", true);
        model.addAttribute("username", username);
        return "auth-login";
    }

    @GetMapping("/logout")
    public String logout() {
        userService.logout();
        return "redirect:/";
    }
}
