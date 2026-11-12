package bg.softuni.supermarket;

import bg.softuni.supermarket.repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SuperMarketConsoleTests {

    @Autowired
    private SuperMarketConsole console;

    @Autowired
    private SellerRepository sellers;

    private String run(String... lines) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        console.run(new BufferedReader(new StringReader(String.join("\n", lines))), new PrintStream(bytes, true));
        return bytes.toString();
    }

    @Test
    void walksThroughEveryOption() throws Exception {
        String output = run(
                "1", "F",
                "1", "Food",
                "2", "Sofia",
                "3", "Pila Tintqva-273 Sofia",
                "4", "Ivan Petrov 24 1500 Pila",
                "4", "Petur Stefanov 40 3000 Pila",
                "4", "Young Kid 16 100 Pila",
                "5", "Bread 1 03-05-2020 Food",
                "6", "Ivan Petrov", "Petur Stefanov",
                "7", "Bread", "Pila",
                "8", "Pila",
                "9", "Pila");

        assertThat(output)
                .contains("Name must be minimum two characters!")
                .contains("Successfully added category!")
                .contains("Successfully added town!")
                .contains("Successfully added shop!")
                .contains("Successfully added seller!")
                .contains("Seller must be at least 18 years old!")
                .contains("Successfully added product!")
                .contains("Successfully added manager!")
                .contains("Successfully added product distribution!")
                .contains("Ivan Petrov" + System.lineSeparator() + "Petur Stefanov")
                .contains("Bread - 1.00 $");
        assertThat(sellers.findFirstByFirstNameAndLastName("Ivan", "Petrov").orElseThrow()
                .getManager().getLastName()).isEqualTo("Stefanov");
    }

    @Test
    void unknownReferencesAreReported() throws Exception {
        assertThat(run("3", "Lidl Mladost-1 Varna")).contains("Town not found!");
        assertThat(run("42")).contains("Invalid option!");
    }
}
