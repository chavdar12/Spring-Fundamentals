package bg.softuni.linkedout;

import bg.softuni.linkedout.repository.CompanyRepository;
import bg.softuni.linkedout.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LinkedOutTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CompanyRepository companies;

    @Autowired
    private EmployeeRepository employees;

    @Test
    void indexShowsLastUpdate() throws Exception {
        mvc.perform(get("/")).andExpect(content().string(containsString("Last update of Database:")));
    }

    @Test
    void invalidCompanyShowsEveryMessage() throws Exception {
        mvc.perform(post("/companies/add")
                        .param("name", "A").param("town", "ThisTownIsTooLong")
                        .param("description", "short").param("budget", "-5"))
                .andExpect(view().name("company-add"))
                .andExpect(content().string(containsString("Name must be between 2 and 10 characters.")))
                .andExpect(content().string(containsString("Budget must be a positive number.")))
                .andExpect(model().attributeHasFieldErrors("company", "name", "town", "description", "budget"));
    }

    @Test
    void companyAndEmployeeLifecycle() throws Exception {
        mvc.perform(post("/companies/add")
                        .param("name", "SoftUni").param("town", "Sofia")
                        .param("description", "Software University").param("budget", "1000000"))
                .andExpect(redirectedUrl("/companies/all"));
        mvc.perform(post("/companies/add")
                        .param("name", "SoftUni").param("town", "Sofia")
                        .param("description", "Software University").param("budget", "1"))
                .andExpect(content().string(containsString("A company with this name already exists.")));

        Long companyId = companies.findAll().getFirst().getId();
        mvc.perform(get("/companies/all")).andExpect(content().string(containsString("Description: Softw ...")));

        mvc.perform(post("/employees/add")
                        .param("firstName", "I").param("lastName", "Petrov").param("salary", "0"))
                .andExpect(model().attributeHasFieldErrors("employee",
                        "firstName", "educationLevel", "company", "jobTitle", "birthDate", "salary"));

        mvc.perform(post("/employees/add")
                        .param("firstName", "Ivan").param("lastName", "Petrov")
                        .param("educationLevel", "MASTER").param("company", "" + companyId)
                        .param("jobTitle", "Trainer").param("birthDate", "1990-05-01").param("salary", "3000"))
                .andExpect(redirectedUrl("/employees/all"));

        Long employeeId = employees.findAll().getFirst().getId();
        mvc.perform(get("/employees/" + employeeId))
                .andExpect(content().string(containsString("Salary: 3000.00$")))
                .andExpect(content().string(containsString("Company: SoftUni")));

        mvc.perform(post("/companies/" + companyId + "/delete")).andExpect(redirectedUrl("/companies/all"));
        assertThat(companies.count()).isZero();
        assertThat(employees.count()).isZero();
        mvc.perform(get("/companies/" + companyId)).andExpect(status().isNotFound());
    }
}
