package bg.softuni.battleships.web;

import bg.softuni.battleships.model.CategoryName;
import bg.softuni.battleships.model.ShipAddBindingModel;
import bg.softuni.battleships.service.ShipService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ships")
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @ModelAttribute("categories")
    public CategoryName[] categories() {
        return CategoryName.values();
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("ship") ShipAddBindingModel ship) {
        return "ship-add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("ship") ShipAddBindingModel ship, BindingResult result) {
        if (result.hasErrors()) {
            return "ship-add";
        }
        if (!shipService.add(ship)) {
            result.rejectValue("name", "duplicate", "Ship name is already taken.");
            return "ship-add";
        }
        return "redirect:/home";
    }
}
