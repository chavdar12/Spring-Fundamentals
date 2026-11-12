package bg.softuni.battleships.model;

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

    @Column(columnDefinition = "TEXT")
    private String description;

    protected Category() {
    }

    public Category(CategoryName name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public CategoryName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
