package bg.softuni.linkedout.repository;

import bg.softuni.linkedout.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
