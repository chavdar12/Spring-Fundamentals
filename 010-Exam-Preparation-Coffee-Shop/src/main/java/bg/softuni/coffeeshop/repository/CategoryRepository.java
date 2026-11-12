package bg.softuni.coffeeshop.repository;

import bg.softuni.coffeeshop.model.Category;
import bg.softuni.coffeeshop.model.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(CategoryName name);
}
