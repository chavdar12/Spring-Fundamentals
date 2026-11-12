package bg.softuni.coffeeshop.web;

import bg.softuni.coffeeshop.model.CategoryName;
import bg.softuni.coffeeshop.model.OrderAddBindingModel;
import bg.softuni.coffeeshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ModelAttribute("categories")
    public CategoryName[] categories() {
        return CategoryName.values();
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("order") OrderAddBindingModel order) {
        return "order-add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("order") OrderAddBindingModel order, BindingResult result) {
        if (result.hasErrors()) {
            return "order-add";
        }
        orderService.add(order);
        return "redirect:/home";
    }

    @GetMapping("/ready/{id}")
    public String ready(@PathVariable Long id) {
        orderService.ready(id);
        return "redirect:/home";
    }
}
