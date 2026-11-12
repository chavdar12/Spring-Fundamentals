package bg.softuni.shoppinglist.web;

import bg.softuni.shoppinglist.model.UserLoginBindingModel;
import bg.softuni.shoppinglist.model.UserRegisterBindingModel;
import bg.softuni.shoppinglist.service.UserService;
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
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegisterBindingModel user, BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        if (!userService.register(user)) {
            model.addAttribute("registerFailed", true);
            return "register";
        }
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("user") UserLoginBindingModel user) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") UserLoginBindingModel user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "login";
        }
        if (!userService.login(user)) {
            model.addAttribute("badCredentials", true);
            return "login";
        }
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout() {
        userService.logout();
        return "redirect:/";
    }
}
