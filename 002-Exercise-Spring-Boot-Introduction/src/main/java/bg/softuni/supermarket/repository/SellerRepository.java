package bg.softuni.supermarket.repository;

import bg.softuni.supermarket.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, String> {

    Optional<Seller> findFirstByFirstNameAndLastName(String firstName, String lastName);

    List<Seller> findAllByShopName(String shopName);
}
