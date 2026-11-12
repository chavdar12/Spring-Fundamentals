package bg.softuni.mobilele.web;

import bg.softuni.mobilele.repository.BrandRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrandController {

    private final BrandRepository brands;

    public BrandController(BrandRepository brands) {
        this.brands = brands;
    }

    @GetMapping("/brands/all")
    public String all(Model model) {
        model.addAttribute("brands", brands.findAll());
        return "brands";
    }
}
