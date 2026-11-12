package bg.softuni.supermarket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "Address must be minimum two characters!")
    @Size(min = 2, message = "Address must be minimum two characters!")
    @Column(nullable = false, unique = true)
    private String address;

    @NotNull(message = "Name must be minimum two characters!")
    @Size(min = 2, message = "Name must be minimum two characters!")
    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private Town town;

    protected Shop() {
    }

    public Shop(String name, String address, Town town) {
        this.name = name;
        this.address = address;
        this.town = town;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Town getTown() {
        return town;
    }
}
