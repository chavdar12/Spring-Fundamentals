package bg.softuni.pathfinder.repository;

import bg.softuni.pathfinder.model.entity.RoleEntity;
import bg.softuni.pathfinder.model.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(RoleName name);
}
