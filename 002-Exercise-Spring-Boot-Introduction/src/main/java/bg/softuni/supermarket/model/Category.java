package bg.softuni.supermarket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "Name must be minimum two characters!")
    @Size(min = 2, message = "Name must be minimum two characters!")
    @Column(nullable = false, unique = true)
    private String name;

    protected Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
