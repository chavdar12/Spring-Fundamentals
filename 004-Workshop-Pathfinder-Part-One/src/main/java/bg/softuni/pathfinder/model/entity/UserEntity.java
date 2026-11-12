package bg.softuni.pathfinder.model.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String fullName;

    private Integer age;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<RoleEntity> role = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Level level;

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserEntity setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<RoleEntity> getRole() {
        return role;
    }

    public Level getLevel() {
        return level;
    }

    public UserEntity setLevel(Level level) {
        this.level = level;
        return this;
    }

    public boolean hasRole(RoleName name) {
        return role.stream().anyMatch(r -> r.getName() == name);
    }
}
