package client.IntelligentAI;

import client.model.Ability;
import client.model.Cell;
import client.model.Hero;

import java.util.Collection;

public class Move {
    private Hero hero;
    private Cell currentCell;
    private Cell nextCell;
    private Cell targetZoneCell;
    private Cell beforeCell;

    private Ability ability;
    private int maxRange;


    public Move() {
        this.currentCell = null;
        this.nextCell = null;
    }

    public Move(Hero hero, Cell currentCell, Ability ability, Integer maxRange) {
        this.hero = hero;
        this.currentCell = currentCell;
        this.nextCell = currentCell;
        this.maxRange = maxRange;
        this.ability = ability;
    }

    public Move(Hero hero, Cell currentCell, Cell nextCell) {
        this.hero = hero;
        this.currentCell = currentCell;
        this.nextCell = nextCell;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public Cell getNextCell() {
        return nextCell;
    }

    public void setNextCell(Cell nextCell) {
        this.nextCell = nextCell;
    }

    public Cell getBeforeCell() {
        return beforeCell;
    }

    public void setBeforeCell(Cell beforeCell) {
        this.beforeCell = beforeCell;
    }

    public Cell getTargetZoneCell() {
        return targetZoneCell;
    }

    public void setTargetZoneCell(Cell targetZoneCell) {
        this.targetZoneCell = targetZoneCell;
    }


    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public static Move findByHero(Collection<Move> list, Hero hero) {
        return list.stream().filter(object -> hero.equals(object.getHero())).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        if (currentCell == null)
            return "";

        return "Move{" +
                "hero=" + hero +
                ", currentCell=" + currentCell +
                ", nextCell=" + nextCell +
                '}';
    }
}
