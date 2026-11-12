package bg.softuni.supermarket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDate bestBefore;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Name must be minimum two characters!")
    @Size(min = 2, message = "Name must be minimum two characters!")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Price must be a positive number!")
    @Positive(message = "Price must be a positive number!")
    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(optional = false)
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Shop> shops = new HashSet<>();

    protected Product() {
    }

    public Product(String name, BigDecimal price, LocalDate bestBefore, Category category) {
        this.name = name;
        this.price = price;
        this.bestBefore = bestBefore;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public Set<Shop> getShops() {
        return shops;
    }
}
