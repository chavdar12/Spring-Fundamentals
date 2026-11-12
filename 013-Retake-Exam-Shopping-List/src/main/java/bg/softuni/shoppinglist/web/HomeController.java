package bg.softuni.shoppinglist.web;

import bg.softuni.shoppinglist.model.CategoryName;
import bg.softuni.shoppinglist.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("foods", productService.byCategory(CategoryName.FOOD));
        model.addAttribute("drinks", productService.byCategory(CategoryName.DRINK));
        model.addAttribute("households", productService.byCategory(CategoryName.HOUSEHOLD));
        model.addAttribute("others", productService.byCategory(CategoryName.OTHER));
        model.addAttribute("totalPrice", productService.totalPrice());
        return "home";
    }
}
