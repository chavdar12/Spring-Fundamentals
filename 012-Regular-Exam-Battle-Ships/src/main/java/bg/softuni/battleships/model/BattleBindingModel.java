package bg.softuni.battleships.model;

import jakarta.validation.constraints.NotNull;

public class BattleBindingModel {

    @NotNull
    private Long attackerId;

    @NotNull
    private Long defenderId;

    public Long getAttackerId() {
        return attackerId;
    }

    public void setAttackerId(Long attackerId) {
        this.attackerId = attackerId;
    }

    public Long getDefenderId() {
        return defenderId;
    }

    public void setDefenderId(Long defenderId) {
        this.defenderId = defenderId;
    }
}
