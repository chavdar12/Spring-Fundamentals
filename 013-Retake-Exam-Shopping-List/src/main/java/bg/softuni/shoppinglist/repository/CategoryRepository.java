package bg.softuni.shoppinglist.repository;

import bg.softuni.shoppinglist.model.Category;
import bg.softuni.shoppinglist.model.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(CategoryName name);
}
