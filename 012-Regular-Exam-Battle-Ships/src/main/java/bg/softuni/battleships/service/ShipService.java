package bg.softuni.battleships.service;

import bg.softuni.battleships.model.BattleBindingModel;
import bg.softuni.battleships.model.Category;
import bg.softuni.battleships.model.CategoryName;
import bg.softuni.battleships.model.Ship;
import bg.softuni.battleships.model.ShipAddBindingModel;
import bg.softuni.battleships.repository.CategoryRepository;
import bg.softuni.battleships.repository.ShipRepository;
import bg.softuni.battleships.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ShipService {

    private final ShipRepository shipRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    public ShipService(ShipRepository shipRepository, CategoryRepository categoryRepository,
                       UserRepository userRepository, CurrentUser currentUser) {
        this.shipRepository = shipRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @Bean
    ApplicationRunner initCategories() {
        return args -> {
            if (categoryRepository.count() == 0) {
                categoryRepository.saveAll(Arrays.stream(CategoryName.values()).map(Category::new).toList());
            }
        };
    }

    /** Returns false when a ship with that name already exists. */
    public boolean add(ShipAddBindingModel model) {
        if (shipRepository.existsByName(model.getName())) {
            return false;
        }
        shipRepository.save(new Ship(model.getName(), model.getHealth(), model.getPower(), model.getCreated(),
                categoryRepository.findByName(model.getCategory()).orElseThrow(),
                userRepository.getReferenceById(currentUser.getId())));
        return true;
    }

    public List<Ship> ownShips() {
        return shipRepository.findAllByUserId(currentUser.getId());
    }

    public List<Ship> otherShips() {
        return shipRepository.findAllByUserIdNot(currentUser.getId());
    }

    public List<Ship> allShips() {
        return shipRepository.findAllByOrderByNameAscHealthAscPowerAsc();
    }

    /** The attacker must belong to the current user and the defender to someone else. */
    @Transactional
    public void fire(BattleBindingModel battle) {
        Ship attacker = shipRepository.findById(battle.getAttackerId()).orElse(null);
        Ship defender = shipRepository.findById(battle.getDefenderId()).orElse(null);
        if (attacker == null || defender == null
                || !attacker.getUser().getId().equals(currentUser.getId())
                || defender.getUser().getId().equals(currentUser.getId())) {
            return;
        }
        long health = defender.getHealth() - attacker.getPower();
        if (health <= 0) {
            shipRepository.delete(defender);
        } else {
            defender.setHealth(health);
        }
    }
}
