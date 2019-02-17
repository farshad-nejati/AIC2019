package client.RandomAI;

import client.model.Ability;
import client.model.Cell;
import client.model.Hero;

public class EffectiveHero {
    private Hero myHero;
    private Hero oppHero;
    private Ability randomAbility;
    private Cell targetCell;

    public EffectiveHero(Hero myHero, Hero oppHero, Ability randomAbility, Cell targetCell) {
        this.myHero = myHero;
        this.oppHero = oppHero;
        this.randomAbility = randomAbility;
        this.targetCell = targetCell;
    }

    public Hero getMyHero() {
        return myHero;
    }

    public void setMyHero(Hero meHero) {
        this.myHero = meHero;
    }

    public Hero getOppHero() {
        return oppHero;
    }

    public void setOppHero(Hero oppHero) {
        this.oppHero = oppHero;
    }

    public Ability getRandomAbility() {
        return randomAbility;
    }

    public void setRandomAbility(Ability randomAbility) {
        this.randomAbility = randomAbility;
    }

    public Cell getTargetCell() {
        return targetCell;
    }

    public void setTargetCell(Cell targetCell) {
        this.targetCell = targetCell;
    }
}
