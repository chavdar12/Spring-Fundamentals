package bg.softuni.pathfinder.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_categories")
public class CategoryEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CategoryName name;

    @Column(columnDefinition = "TEXT")
    private String description;

    public CategoryName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
