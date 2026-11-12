package bg.softuni.battleships.repository;

import bg.softuni.battleships.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipRepository extends JpaRepository<Ship, Long> {

    boolean existsByName(String name);

    List<Ship> findAllByUserId(Long userId);

    List<Ship> findAllByUserIdNot(Long userId);

    List<Ship> findAllByOrderByNameAscHealthAscPowerAsc();
}
