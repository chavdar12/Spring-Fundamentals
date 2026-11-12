package bg.softuni.supermarket.repository;

import bg.softuni.supermarket.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TownRepository extends JpaRepository<Town, String> {

    Optional<Town> findByName(String name);
}
