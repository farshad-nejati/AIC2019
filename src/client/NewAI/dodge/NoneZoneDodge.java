package client.NewAI.dodge;

import client.model.Ability;
import client.model.Cell;
import client.model.Hero;

public class NoneZoneDodge {
    private Hero hero;
    private Ability ability;
    private Cell targetCell;

    public NoneZoneDodge(Hero hero, Ability ability, Cell targetCell) {
        this.hero = hero;
        this.ability = ability;
        this.targetCell = targetCell;
    }

    public Hero getHero() {

        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Cell getTargetCell() {
        return targetCell;
    }

    public void setTargetCell(Cell targetCell) {
        this.targetCell = targetCell;
    }
}
