package bg.softuni.linkedout.web;

import bg.softuni.linkedout.model.Company;
import bg.softuni.linkedout.repository.CompanyRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyRepository companies;

    public CompanyController(CompanyRepository companies) {
        this.companies = companies;
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("company") Company company) {
        return "company-add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("company") Company company, BindingResult result) {
        if (!result.hasFieldErrors("name") && companies.existsByName(company.getName())) {
            result.rejectValue("name", "duplicate", "A company with this name already exists.");
        }
        if (result.hasErrors()) {
            return "company-add";
        }
        companies.save(company);
        return "redirect:/companies/all";
    }

    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute("companies", companies.findAll());
        return "company-all";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("company", companies.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "company-details";
    }

    @Transactional
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        // cascades to the company's employees
        companies.findById(id).ifPresent(companies::delete);
        return "redirect:/companies/all";
    }
}
