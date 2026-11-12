package bg.softuni.supermarket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "First name must be minimum two characters!")
    @Size(min = 2, message = "First name must be minimum two characters!")
    @Column(nullable = false)
    private String firstName;

    @NotNull(message = "Last name must be minimum two characters!")
    @Size(min = 2, message = "Last name must be minimum two characters!")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "Seller must be at least 18 years old!")
    @Min(value = 18, message = "Seller must be at least 18 years old!")
    @Column(nullable = false)
    private Integer age;

    @NotNull(message = "Salary must be a positive number!")
    @Positive(message = "Salary must be a positive number!")
    @Column(nullable = false)
    private BigDecimal salary;

    @ManyToOne(optional = false)
    private Shop shop;

    @ManyToOne
    private Seller manager;

    protected Seller() {
    }

    public Seller(String firstName, String lastName, Integer age, BigDecimal salary, Shop shop) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.salary = salary;
        this.shop = shop;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Shop getShop() {
        return shop;
    }

    public Seller getManager() {
        return manager;
    }

    public void setManager(Seller manager) {
        this.manager = manager;
    }
}
