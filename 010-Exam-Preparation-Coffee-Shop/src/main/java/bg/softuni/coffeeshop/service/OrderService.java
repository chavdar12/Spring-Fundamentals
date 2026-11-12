package bg.softuni.coffeeshop.service;

import bg.softuni.coffeeshop.model.*;
import bg.softuni.coffeeshop.repository.CategoryRepository;
import bg.softuni.coffeeshop.repository.OrderRepository;
import bg.softuni.coffeeshop.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    public OrderService(OrderRepository orderRepository, CategoryRepository categoryRepository,
                        UserRepository userRepository, CurrentUser currentUser) {
        this.orderRepository = orderRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @Bean
    ApplicationRunner initCategories() {
        return args -> {
            if (categoryRepository.count() == 0) {
                categoryRepository.saveAll(Arrays.stream(CategoryName.values()).map(Category::new).toList());
            }
        };
    }

    public void add(OrderAddBindingModel model) {
        orderRepository.save(new Order(model.getName(), model.getPrice(), model.getOrderTime(),
                categoryRepository.findByName(model.getCategory()).orElseThrow(),
                model.getDescription(), userRepository.getReferenceById(currentUser.getId())));
    }

    public void ready(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> allByPriceDesc() {
        return orderRepository.findAllByOrderByPriceDesc();
    }

    public int totalNeededTime(List<Order> orders) {
        return orders.stream().mapToInt(o -> o.getCategory().getNeededTime()).sum();
    }

    /** Every registered employee with their open orders, most orders first. */
    public List<EmployeeOrdersView> employees(List<Order> orders) {
        Map<String, Long> counts = orders.stream()
                .collect(Collectors.groupingBy(o -> o.getEmployee().getUsername(), Collectors.counting()));
        return userRepository.findAll().stream()
                .map(u -> new EmployeeOrdersView(u.getUsername(), counts.getOrDefault(u.getUsername(), 0L)))
                .sorted(Comparator.comparingLong(EmployeeOrdersView::orders).reversed())
                .toList();
    }
}
