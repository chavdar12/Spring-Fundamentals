package bg.softuni.shoppinglist.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime neededBefore;

    @ManyToOne(optional = false)
    private Category category;

    protected Product() {
    }

    public Product(String name, String description, BigDecimal price, LocalDateTime neededBefore, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.neededBefore = neededBefore;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getNeededBefore() {
        return neededBefore;
    }

    public Category getCategory() {
        return category;
    }
}
