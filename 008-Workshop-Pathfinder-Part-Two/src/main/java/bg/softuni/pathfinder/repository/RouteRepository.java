package bg.softuni.pathfinder.repository;

import bg.softuni.pathfinder.model.entity.CategoryName;
import bg.softuni.pathfinder.model.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<RouteEntity, Long> {

    List<RouteEntity> findAllByCategoriesName(CategoryName name);

    boolean existsByName(String name);
}
