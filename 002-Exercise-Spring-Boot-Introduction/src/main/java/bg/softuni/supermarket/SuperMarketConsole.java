package bg.softuni.supermarket;

import bg.softuni.supermarket.model.*;
import bg.softuni.supermarket.repository.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Set;

@Component
public class SuperMarketConsole implements CommandLineRunner {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String MENU = """
            Choose option from:
            1 - for Add Category
            2 - for Add Town
            3 - for Add Shop
            4 - for Add Seller
            5 - for Add Product
            6 - for Set seller new manager
            7 - for Distributing product in shops
            8 - for Showing all sellers in Shop
            9 - for Showing all products in Shop""";

    private final CategoryRepository categories;
    private final TownRepository towns;
    private final ShopRepository shops;
    private final SellerRepository sellers;
    private final ProductRepository products;
    private final Validator validator;
    private final boolean enabled;

    public SuperMarketConsole(CategoryRepository categories, TownRepository towns, ShopRepository shops,
                              SellerRepository sellers, ProductRepository products, Validator validator,
                              @Value("${supermarket.console.enabled:true}") boolean enabled) {
        this.categories = categories;
        this.towns = towns;
        this.shops = shops;
        this.sellers = sellers;
        this.products = products;
        this.validator = validator;
        this.enabled = enabled;
    }

    @Override
    public void run(String... args) throws IOException {
        if (enabled) {
            run(new BufferedReader(new InputStreamReader(System.in)), System.out);
        }
    }

    /** Reads options until end of input or the word "end". */
    public void run(BufferedReader in, PrintStream out) throws IOException {
        out.println("Hi");
        while (true) {
            out.println(MENU);
            String option = in.readLine();
            if (option == null || option.isBlank() || option.trim().equalsIgnoreCase("end")) {
                return;
            }
            try {
                out.println(execute(option.trim(), in, out));
            } catch (IllegalArgumentException | DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                out.println(e instanceof IllegalArgumentException ? e.getMessage() : "Invalid input!");
            }
            out.println("=================================");
        }
    }

    String execute(String option, BufferedReader in, PrintStream out) throws IOException {
        return switch (option) {
            case "1" -> {
                out.println("Enter category name:");
                save(categories, new Category(in.readLine().trim()));
                yield "Successfully added category!";
            }
            case "2" -> {
                out.println("Enter town name:");
                save(towns, new Town(in.readLine().trim()));
                yield "Successfully added town!";
            }
            case "3" -> {
                out.println("Enter shop details in format: name address town");
                String[] t = tokens(in);
                save(shops, new Shop(t[0], t[1], towns.findByName(t[2]).orElseThrow(() -> notFound("Town"))));
                yield "Successfully added shop!";
            }
            case "4" -> {
                out.println("Enter seller details in format: firstName lastName age salary shopName");
                String[] t = tokens(in);
                save(sellers, new Seller(t[0], t[1], Integer.parseInt(t[2]), new BigDecimal(t[3]),
                        shops.findByName(t[4]).orElseThrow(() -> notFound("Shop"))));
                yield "Successfully added seller!";
            }
            case "5" -> {
                out.println("Enter product details in format: name price bestBefore(dd-MM-yyyy) category");
                String[] t = tokens(in);
                save(products, new Product(t[0], new BigDecimal(t[1]), LocalDate.parse(t[2], DATE),
                        categories.findByName(t[3]).orElseThrow(() -> notFound("Category"))));
                yield "Successfully added product!";
            }
            case "6" -> {
                out.println("Enter seller first and last names:");
                Seller seller = seller(tokens(in));
                out.println("Enter manager first and last names:");
                seller.setManager(seller(tokens(in)));
                sellers.save(seller);
                yield "Successfully added manager!";
            }
            case "7" -> {
                out.println("Enter product name:");
                Product product = products.findFirstByName(in.readLine().trim())
                        .orElseThrow(() -> notFound("Product"));
                out.println("Enter product distribution in Shops names in format [shopName1 shopName2 ... ]:");
                Arrays.stream(tokens(in))
                        .map(name -> shops.findByName(name).orElseThrow(() -> notFound("Shop " + name)))
                        .forEach(product.getShops()::add);
                products.save(product);
                yield "Successfully added product distribution!";
            }
            case "8" -> {
                out.println("Enter shop name:");
                yield String.join(System.lineSeparator(), sellers.findAllByShopName(in.readLine().trim()).stream()
                        .map(s -> s.getFirstName() + " " + s.getLastName()).toList());
            }
            case "9" -> {
                out.println("Enter shop name:");
                yield String.join(System.lineSeparator(), products.findAllByShopsName(in.readLine().trim()).stream()
                        .map(p -> "%s - %.2f $".formatted(p.getName(), p.getPrice())).toList());
            }
            default -> "Invalid option!";
        };
    }

    private Seller seller(String[] names) {
        return sellers.findFirstByFirstNameAndLastName(names[0], names[1]).orElseThrow(() -> notFound("Seller"));
    }

    private <T> void save(org.springframework.data.jpa.repository.JpaRepository<T, String> repository, T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }
        repository.save(entity);
    }

    private static String[] tokens(BufferedReader in) throws IOException {
        return in.readLine().trim().split("\\s+");
    }

    private static IllegalArgumentException notFound(String what) {
        return new IllegalArgumentException(what + " not found!");
    }
}
