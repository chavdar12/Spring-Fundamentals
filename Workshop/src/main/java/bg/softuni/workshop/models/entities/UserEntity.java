package bg.softuni.workshop.models.entities;

import bg.softuni.workshop.models.enums.LevelEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_users")
public class UserEntity extends BaseEntity {

    @Column
    private Integer age;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @ManyToMany
    private Set<RoleEntity> role;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;
}
