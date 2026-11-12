package bg.softuni.shoppinglist.model;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductAddBindingModel {

    @NotBlank(message = "Name length must be between 3 and 20 characters.")
    @Size(min = 3, max = 20, message = "Name length must be between 3 and 20 characters.")
    private String name;

    @NotNull(message = "Description length must be at least 5 characters.")
    @Size(min = 5, message = "Description length must be at least 5 characters.")
    private String description;

    @NotNull(message = "The date cannot be in the past.")
    @FutureOrPresent(message = "The date cannot be in the past.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime neededBefore;

    @NotNull(message = "Price must be a positive number.")
    @Positive(message = "Price must be a positive number.")
    private BigDecimal price;

    @NotNull(message = "You must select a category.")
    private CategoryName category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getNeededBefore() {
        return neededBefore;
    }

    public void setNeededBefore(LocalDateTime neededBefore) {
        this.neededBefore = neededBefore;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CategoryName getCategory() {
        return category;
    }

    public void setCategory(CategoryName category) {
        this.category = category;
    }
}
