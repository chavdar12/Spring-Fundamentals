package bg.softuni.pathfinder.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_roles")
public class RoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName name;

    public RoleName getName() {
        return name;
    }

    public RoleEntity setName(RoleName name) {
        this.name = name;
        return this;
    }
}
