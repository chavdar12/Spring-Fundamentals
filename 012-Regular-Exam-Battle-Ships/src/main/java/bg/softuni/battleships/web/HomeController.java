package bg.softuni.battleships.web;

import bg.softuni.battleships.model.BattleBindingModel;
import bg.softuni.battleships.service.ShipService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final ShipService shipService;

    public HomeController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(@ModelAttribute("battle") BattleBindingModel battle, Model model) {
        model.addAttribute("ownShips", shipService.ownShips());
        model.addAttribute("otherShips", shipService.otherShips());
        model.addAttribute("allShips", shipService.allShips());
        return "home";
    }

    @PostMapping("/battle")
    public String battle(@Valid BattleBindingModel battle, BindingResult result) {
        if (!result.hasErrors()) {
            shipService.fire(battle);
        }
        return "redirect:/home";
    }
}
