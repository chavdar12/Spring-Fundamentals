package bg.softuni.battleships.repository;

import bg.softuni.battleships.model.Category;
import bg.softuni.battleships.model.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(CategoryName name);
}
