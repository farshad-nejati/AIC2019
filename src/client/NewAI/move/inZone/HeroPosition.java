package client.NewAI.move.inZone;

import client.model.Cell;
import client.model.Hero;

import java.util.Collection;

public class HeroPosition {
    private Hero hero;
    private Cell cell;

    public HeroPosition(Hero hero, Cell cell) {
        this.hero = hero;
        this.cell = cell;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public static HeroPosition findByHero(Collection<HeroPosition> list, Hero hero) {
        return list.stream().filter(object -> hero.equals(object.getHero())).findFirst().orElse(null);
    }

}
