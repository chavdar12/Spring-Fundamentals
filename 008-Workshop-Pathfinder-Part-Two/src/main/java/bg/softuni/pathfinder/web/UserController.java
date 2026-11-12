package bg.softuni.pathfinder.web;

import bg.softuni.pathfinder.model.binding.UserRegisterBindingModel;
import bg.softuni.pathfinder.service.CurrentUser;
import bg.softuni.pathfinder.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final String BINDING_RESULT = BindingResult.MODEL_KEY_PREFIX + "userModel";

    private final UserService userService;
    private final CurrentUser currentUser;

    public UserController(UserService userService, CurrentUser currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
    }

    /** Empty form on the first visit; after a failed submit the flash attribute with the user's input wins. */
    @ModelAttribute("userModel")
    public UserRegisterBindingModel userModel() {
        return new UserRegisterBindingModel();
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterBindingModel userModel, BindingResult result,
                           RedirectAttributes redirect) {
        if (!result.hasErrors() && !userModel.getPassword().equals(userModel.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Passwords do not match.");
        }
        if (!result.hasErrors() && !userService.register(userModel)) {
            result.rejectValue("username", "taken", "Username is already occupied.");
        }
        if (result.hasErrors()) {
            redirect.addFlashAttribute("userModel", userModel);
            redirect.addFlashAttribute(BINDING_RESULT, result);
            return "redirect:/users/register";
        }
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirect) {
        if (userService.login(username, password)) {
            return "redirect:/home";
        }
        redirect.addFlashAttribute("badCredentials", true);
        redirect.addFlashAttribute("username", username);
        return "redirect:/users/login";
    }

    @GetMapping("/logout")
    public String logout() {
        userService.logout();
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String ownProfile() {
        return "redirect:/users/profile/" + currentUser.getId();
    }

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "profile";
    }
}
