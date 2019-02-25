package client.IntelligentAI;

import client.model.Cell;
import client.model.Hero;

import java.util.Collection;

public class Move {
    private Hero hero;
    private Cell currentCell;
    private Cell nextCell;

    public Cell getTargetZoneCell() {
        return targetZoneCell;
    }

    public void setTargetZoneCell(Cell targetZoneCell) {
        this.targetZoneCell = targetZoneCell;
    }

    private Cell targetZoneCell;

    public Move() {
        this.currentCell = null;
        this.nextCell = null;
    }

    public Move(Hero hero, Cell currentCell) {
        this.hero = hero;
        this.currentCell = currentCell;
        this.nextCell = currentCell;
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

    public static Move findByHero(Collection<Move> list, Hero hero){
        return list.stream().filter(object -> hero.equals(object.getHero())).findFirst().orElse(null);
    }
}
