package bg.softuni.linkedout.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Budget must be a positive number.")
    @Positive(message = "Budget must be a positive number.")
    @Column(nullable = false)
    private BigDecimal budget;

    @NotNull(message = "Description must be at least 10 characters.")
    @Size(min = 10, message = "Description must be at least 10 characters.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Name must be between 2 and 10 characters.")
    @Size(min = 2, max = 10, message = "Name must be between 2 and 10 characters.")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Town must be between 2 and 10 characters.")
    @Size(min = 2, max = 10, message = "Town must be between 2 and 10 characters.")
    @Column(nullable = false)
    private String town;

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE)
    private List<Employee> employees = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
