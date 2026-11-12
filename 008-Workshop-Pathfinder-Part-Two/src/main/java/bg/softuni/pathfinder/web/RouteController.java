package bg.softuni.pathfinder.web;

import bg.softuni.pathfinder.model.binding.RouteAddBindingModel;
import bg.softuni.pathfinder.model.entity.CategoryName;
import bg.softuni.pathfinder.model.entity.Level;
import bg.softuni.pathfinder.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/routes")
public class RouteController {

    private static final String BINDING_RESULT = BindingResult.MODEL_KEY_PREFIX + "routeModel";

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public String all(Model model) {
        model.addAttribute("routes", routeService.all());
        return "routes";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("route", routeService.details(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "route-details";
    }

    @GetMapping("/add")
    public String add(Model model) {
        if (!model.containsAttribute("routeModel")) {
            model.addAttribute("routeModel", new RouteAddBindingModel());
        }
        model.addAttribute("levels", Level.values());
        model.addAttribute("categories", CategoryName.values());
        return "add-route";
    }

    @PostMapping("/add")
    public String add(@Valid RouteAddBindingModel routeModel, BindingResult result, RedirectAttributes redirect)
            throws IOException {
        if (routeModel.getGpxCoordinates() == null || routeModel.getGpxCoordinates().isEmpty()) {
            result.rejectValue("gpxCoordinates", "required", "Must be valid GPX Coordinates");
        }
        if (!result.hasFieldErrors("name") && routeService.exists(routeModel.getName())) {
            result.rejectValue("name", "taken", "A route with this name already exists.");
        }
        if (result.hasErrors()) {
            routeModel.setGpxCoordinates(null); // uploaded files cannot survive a redirect
            redirect.addFlashAttribute("routeModel", routeModel);
            redirect.addFlashAttribute(BINDING_RESULT, result);
            return "redirect:/routes/add";
        }
        return "redirect:/routes/details/" + routeService.add(routeModel).getId();
    }

    @GetMapping("/{category:pedestrian|bicycle|motorcycle|car}")
    public String byCategory(@PathVariable String category, Model model) {
        CategoryName name = CategoryName.valueOf(category.toUpperCase());
        model.addAttribute("category", name);
        model.addAttribute("routes", routeService.byCategory(name));
        return "category";
    }
}
