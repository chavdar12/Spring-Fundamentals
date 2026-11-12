package bg.softuni.mobilele.init;

import bg.softuni.mobilele.model.entity.*;
import bg.softuni.mobilele.model.entity.enums.*;
import bg.softuni.mobilele.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/** Fills an empty database with brands, models, users and offers on the first start. */
@Component
public class DataInitializer implements CommandLineRunner {

    private final BrandRepository brands;
    private final ModelRepository models;
    private final UserRoleRepository roles;
    private final UserRepository users;
    private final OfferRepository offers;

    public DataInitializer(BrandRepository brands, ModelRepository models, UserRoleRepository roles,
                           UserRepository users, OfferRepository offers) {
        this.brands = brands;
        this.models = models;
        this.roles = roles;
        this.users = users;
        this.offers = offers;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (brands.count() > 0) {
            return;
        }
        BrandEntity ford = brands.save(new BrandEntity().setName("Ford"));
        BrandEntity honda = brands.save(new BrandEntity().setName("Honda"));

        ModelEntity fiesta = models.save(model("Fiesta", CategoryEnum.CAR, 1976, null, ford,
                "https://upload.wikimedia.org/wikipedia/commons/7/7d/2017_Ford_Fiesta_Zetec_Turbo_1.0_Front.jpg"));
        models.save(model("Escort", CategoryEnum.CAR, 1968, 2004, ford,
                "https://upload.wikimedia.org/wikipedia/commons/1/1e/Ford_Escort_RS2000_MkI.jpg"));
        models.save(model("NC750S", CategoryEnum.MOTORCYCLE, 2014, null, honda,
                "https://upload.wikimedia.org/wikipedia/commons/a/a6/Honda_NC750S_2016.jpg"));

        UserRoleEntity adminRole = roles.save(new UserRoleEntity().setRole(UserRoleEnum.ADMIN));
        UserRoleEntity userRole = roles.save(new UserRoleEntity().setRole(UserRoleEnum.USER));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserEntity admin = users.save(new UserEntity().setUsername("admin").setFirstName("Admin")
                .setLastName("Adminov").setPassword(encoder.encode("topsecret")).setActive(true)
                .setRoles(Set.of(adminRole, userRole)));
        users.save(new UserEntity().setUsername("pesho").setFirstName("Pesho").setLastName("Petrov")
                .setPassword(encoder.encode("topsecret")).setActive(true).setRoles(Set.of(userRole)));

        offers.save(new OfferEntity().setModel(fiesta).setSeller(admin).setEngine(EngineEnum.GASOLINE)
                .setTransmission(TransmissionEnum.MANUAL).setMileage(22500).setPrice(14300).setYear(2019)
                .setDescription("Used, but well services and in good condition.")
                .setImageUrl("https://upload.wikimedia.org/wikipedia/commons/7/7d/2017_Ford_Fiesta_Zetec_Turbo_1.0_Front.jpg"));
    }

    private static ModelEntity model(String name, CategoryEnum category, Integer startYear, Integer endYear,
                                     BrandEntity brand, String imageUrl) {
        return new ModelEntity().setName(name).setCategory(category).setStartYear(startYear).setEndYear(endYear)
                .setBrand(brand).setImageUrl(imageUrl);
    }
}
