package bg.softuni.pathfinder.web;

import bg.softuni.pathfinder.model.binding.UserRegisterBindingModel;
import bg.softuni.pathfinder.service.CurrentUser;
import bg.softuni.pathfinder.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CurrentUser currentUser;

    public UserController(UserService userService, CurrentUser currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("userModel") UserRegisterBindingModel userModel) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userModel") UserRegisterBindingModel userModel, Model model) {
        if (userModel.getPassword() == null || !userModel.getPassword().equals(userModel.getConfirmPassword())
                || !userService.register(userModel)) {
            model.addAttribute("registerFailed", true);
            return "register";
        }
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        if (userService.login(username, password)) {
            return "redirect:/home";
        }
        model.addAttribute("badCredentials", true);
        model.addAttribute("username", username);
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        userService.logout();
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("user", userService.findById(currentUser.getId()).orElseThrow());
        return "profile";
    }
}
