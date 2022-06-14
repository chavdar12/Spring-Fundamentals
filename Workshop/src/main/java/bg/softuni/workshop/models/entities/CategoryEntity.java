package bg.softuni.workshop.models.entities;

import bg.softuni.workshop.models.enums.CategoriesOptionsEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_categories")
public class CategoryEntity extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private CategoriesOptionsEnum name;
}
