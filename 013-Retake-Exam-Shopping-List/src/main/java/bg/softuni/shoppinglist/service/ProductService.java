package bg.softuni.shoppinglist.service;

import bg.softuni.shoppinglist.model.Category;
import bg.softuni.shoppinglist.model.CategoryName;
import bg.softuni.shoppinglist.model.Product;
import bg.softuni.shoppinglist.model.ProductAddBindingModel;
import bg.softuni.shoppinglist.repository.CategoryRepository;
import bg.softuni.shoppinglist.repository.ProductRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Bean
    ApplicationRunner initCategories() {
        return args -> {
            if (categoryRepository.count() == 0) {
                categoryRepository.saveAll(Arrays.stream(CategoryName.values()).map(Category::new).toList());
            }
        };
    }

    /** Returns false when a product with that name already exists. */
    public boolean add(ProductAddBindingModel model) {
        if (productRepository.existsByName(model.getName())) {
            return false;
        }
        productRepository.save(new Product(model.getName(), model.getDescription(), model.getPrice(),
                model.getNeededBefore(), categoryRepository.findByName(model.getCategory()).orElseThrow()));
        return true;
    }

    public List<Product> byCategory(CategoryName category) {
        return productRepository.findAllByCategoryName(category);
    }

    public BigDecimal totalPrice() {
        return productRepository.findAll().stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void buy(Long id) {
        productRepository.deleteById(id);
    }

    public void buyAll() {
        productRepository.deleteAll();
    }
}
