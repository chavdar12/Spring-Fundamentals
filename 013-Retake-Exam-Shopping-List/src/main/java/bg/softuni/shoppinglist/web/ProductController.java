package bg.softuni.shoppinglist.web;

import bg.softuni.shoppinglist.model.CategoryName;
import bg.softuni.shoppinglist.model.ProductAddBindingModel;
import bg.softuni.shoppinglist.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ModelAttribute("categories")
    public CategoryName[] categories() {
        return CategoryName.values();
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("product") ProductAddBindingModel product) {
        return "product-add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("product") ProductAddBindingModel product, BindingResult result) {
        if (result.hasErrors()) {
            return "product-add";
        }
        if (!productService.add(product)) {
            result.rejectValue("name", "duplicate", "A product with this name already exists.");
            return "product-add";
        }
        return "redirect:/home";
    }

    @GetMapping("/buy/{id}")
    public String buy(@PathVariable Long id) {
        productService.buy(id);
        return "redirect:/home";
    }

    @GetMapping("/buy-all")
    public String buyAll() {
        productService.buyAll();
        return "redirect:/home";
    }
}
