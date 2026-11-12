package bg.softuni.coffeeshop.web;

import bg.softuni.coffeeshop.model.Order;
import bg.softuni.coffeeshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final OrderService orderService;

    public HomeController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Order> orders = orderService.allByPriceDesc();
        model.addAttribute("orders", orders);
        model.addAttribute("totalTime", orderService.totalNeededTime(orders));
        model.addAttribute("employees", orderService.employees(orders));
        return "home";
    }
}
