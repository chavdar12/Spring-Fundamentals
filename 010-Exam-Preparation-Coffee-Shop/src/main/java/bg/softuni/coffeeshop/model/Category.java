package bg.softuni.coffeeshop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CategoryName name;

    @Column(nullable = false)
    private int neededTime;

    protected Category() {
    }

    public Category(CategoryName name) {
        this.name = name;
        this.neededTime = name.getNeededTime();
    }

    public Long getId() {
        return id;
    }

    public CategoryName getName() {
        return name;
    }

    public int getNeededTime() {
        return neededTime;
    }
}
