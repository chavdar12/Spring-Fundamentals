package bg.softuni.coffeeshop.repository;

import bg.softuni.coffeeshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByPriceDesc();
}
