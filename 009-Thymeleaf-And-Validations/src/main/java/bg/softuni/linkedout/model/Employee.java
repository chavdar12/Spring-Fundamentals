package bg.softuni.linkedout.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Birth date cannot be empty.")
    @Past(message = "Birth date must be in the past.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate birthDate;

    @NotNull(message = "Choose an education level.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EducationLevel educationLevel;

    @NotBlank(message = "First name must be at least 2 characters.")
    @Size(min = 2, message = "First name must be at least 2 characters.")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Job title cannot be empty.")
    @Column(nullable = false)
    private String jobTitle;

    @NotBlank(message = "Last name must be at least 2 characters.")
    @Size(min = 2, message = "Last name must be at least 2 characters.")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "Salary must be a positive number.")
    @Positive(message = "Salary must be a positive number.")
    @Column(nullable = false)
    private BigDecimal salary;

    @NotNull(message = "Choose a company.")
    @ManyToOne(optional = false)
    private Company company;

    public Long getId() {
        return id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public EducationLevel getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(EducationLevel educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
