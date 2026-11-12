package bg.softuni.shoppinglist.repository;

import bg.softuni.shoppinglist.model.CategoryName;
import bg.softuni.shoppinglist.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    List<Product> findAllByCategoryName(CategoryName name);
}
