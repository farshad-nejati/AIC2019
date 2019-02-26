package client.NewAI.move.noneZone;

import client.model.Cell;
import client.model.Hero;

public class NoneZoneHero {
    private Hero hero;
    private Cell targetCell;
    private boolean arrived;

    public NoneZoneHero(Hero hero, Cell targetCell, boolean arrived) {
        this.hero = hero;
        this.targetCell = targetCell;
        this.arrived = arrived;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Cell getTargetCell() {
        return targetCell;
    }

    public void setTargetCell(Cell targetCell) {
        this.targetCell = targetCell;
    }
}