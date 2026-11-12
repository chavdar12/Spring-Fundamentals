package bg.softuni.supermarket.repository;

import bg.softuni.supermarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findFirstByName(String name);

    List<Product> findAllByShopsName(String shopName);
}
