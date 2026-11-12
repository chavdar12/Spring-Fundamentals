package bg.softuni.coffeeshop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    @ManyToOne(optional = false)
    private Category category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = false)
    private User employee;

    protected Order() {
    }

    public Order(String name, BigDecimal price, LocalDateTime orderTime, Category category,
                 String description, User employee) {
        this.name = name;
        this.price = price;
        this.orderTime = orderTime;
        this.category = category;
        this.description = description;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public User getEmployee() {
        return employee;
    }
}
