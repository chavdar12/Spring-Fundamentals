package bg.softuni.battleships.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "ships")
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private long health;

    @Column(nullable = false)
    private long power;

    @Column(nullable = false)
    private LocalDate created;

    @ManyToOne(optional = false)
    private Category category;

    @ManyToOne(optional = false)
    private User user;

    protected Ship() {
    }

    public Ship(String name, long health, long power, LocalDate created, Category category, User user) {
        this.name = name;
        this.health = health;
        this.power = power;
        this.created = created;
        this.category = category;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getHealth() {
        return health;
    }

    public void setHealth(long health) {
        this.health = health;
    }

    public long getPower() {
        return power;
    }

    public LocalDate getCreated() {
        return created;
    }

    public Category getCategory() {
        return category;
    }

    public User getUser() {
        return user;
    }
}
