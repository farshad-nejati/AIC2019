package client.NewAI;

import client.model.Ability;
import client.model.Cell;
import client.model.Hero;

public class HeroAbility {
    private Hero myHero;
    private Ability ability;
    private Cell targetCell;

    public HeroAbility(Hero myHero, Ability ability) {
        this.myHero = myHero;
        this.ability = ability;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Hero getMyHero() {

        return myHero;
    }

    public void setMyHero(Hero myHero) {
        this.myHero = myHero;
    }

    public Cell getTargetCell() {
        return targetCell;
    }

    public void setTargetCell(Cell targetCell) {
        this.targetCell = targetCell;
    }
}
