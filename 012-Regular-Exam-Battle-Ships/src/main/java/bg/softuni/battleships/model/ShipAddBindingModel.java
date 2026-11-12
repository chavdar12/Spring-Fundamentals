package bg.softuni.battleships.model;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ShipAddBindingModel {

    @NotBlank
    @Size(min = 2, max = 10)
    private String name;

    @NotNull
    @Positive
    private Long power;

    @NotNull
    @Positive
    private Long health;

    @NotNull
    @PastOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate created;

    @NotNull
    private CategoryName category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPower() {
        return power;
    }

    public void setPower(Long power) {
        this.power = power;
    }

    public Long getHealth() {
        return health;
    }

    public void setHealth(Long health) {
        this.health = health;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public CategoryName getCategory() {
        return category;
    }

    public void setCategory(CategoryName category) {
        this.category = category;
    }
}
