package bg.softuni.supermarket.repository;

import bg.softuni.supermarket.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, String> {

    Optional<Shop> findByName(String name);
}
