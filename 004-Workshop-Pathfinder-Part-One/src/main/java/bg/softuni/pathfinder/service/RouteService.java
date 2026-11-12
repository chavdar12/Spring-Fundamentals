package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.binding.RouteAddBindingModel;
import bg.softuni.pathfinder.model.entity.CategoryName;
import bg.softuni.pathfinder.model.entity.RouteEntity;
import bg.softuni.pathfinder.repository.CategoryRepository;
import bg.softuni.pathfinder.repository.RouteRepository;
import bg.softuni.pathfinder.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final RouteRepository routes;
    private final CategoryRepository categories;
    private final UserRepository users;
    private final CurrentUser currentUser;

    public RouteService(RouteRepository routes, CategoryRepository categories, UserRepository users,
                        CurrentUser currentUser) {
        this.routes = routes;
        this.categories = categories;
        this.users = users;
        this.currentUser = currentUser;
    }

    public List<RouteEntity> all() {
        return routes.findAll();
    }

    public List<RouteEntity> byCategory(CategoryName category) {
        return routes.findAllByCategoriesName(category);
    }

    /** Loads the comments too, because the session closes before the view renders. */
    @Transactional(readOnly = true)
    public Optional<RouteEntity> details(Long id) {
        Optional<RouteEntity> route = routes.findById(id);
        route.ifPresent(r -> r.getComments().size());
        return route;
    }

    public boolean exists(String name) {
        return routes.existsByName(name);
    }

    public RouteEntity add(RouteAddBindingModel model) throws IOException {
        RouteEntity route = new RouteEntity().setName(model.getName()).setDescription(model.getDescription())
                .setLevel(model.getLevel()).setVideoUrl(model.getVideoUrl())
                .setGpxCoordinates(new String(model.getGpxCoordinates().getBytes(), StandardCharsets.UTF_8))
                .setAuthor(users.getReferenceById(currentUser.getId()));
        route.getCategories().addAll(categories.findAllByNameIn(model.getCategories()));
        return routes.save(route);
    }
}
