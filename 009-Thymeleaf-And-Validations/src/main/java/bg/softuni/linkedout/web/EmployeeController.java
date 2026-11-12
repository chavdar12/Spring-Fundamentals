package bg.softuni.linkedout.web;

import bg.softuni.linkedout.model.EducationLevel;
import bg.softuni.linkedout.model.Employee;
import bg.softuni.linkedout.repository.CompanyRepository;
import bg.softuni.linkedout.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employees;
    private final CompanyRepository companies;

    public EmployeeController(EmployeeRepository employees, CompanyRepository companies) {
        this.employees = employees;
        this.companies = companies;
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("employee") Employee employee, Model model) {
        return addForm(model);
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return addForm(model);
        }
        employees.save(employee);
        return "redirect:/employees/all";
    }

    private String addForm(Model model) {
        model.addAttribute("companies", companies.findAll());
        model.addAttribute("educationLevels", EducationLevel.values());
        return "employee-add";
    }

    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute("employees", employees.findAll());
        return "employee-all";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employees.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "employee-details";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        employees.deleteById(id);
        return "redirect:/employees/all";
    }
}
