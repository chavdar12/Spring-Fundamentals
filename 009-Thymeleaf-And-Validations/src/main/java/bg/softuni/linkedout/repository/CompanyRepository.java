package bg.softuni.linkedout.repository;

import bg.softuni.linkedout.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByName(String name);
}
