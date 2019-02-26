package client.NewAI.move.inZone;

import client.model.Cell;
import client.model.Hero;

import java.util.ArrayList;
import java.util.Collection;

public class HeroNeighbors {
    private ArrayList<Cell> neighborCells;
    private Hero hero;

    public HeroNeighbors(ArrayList<Cell> neighborCells, Hero hero) {
        this.neighborCells = neighborCells;
        this.hero = hero;
    }

    public HeroNeighbors(Hero hero) {
        this.hero = hero;
        this.neighborCells = new ArrayList<>();
    }

    public ArrayList<Cell> getNeighborCells() {

        return neighborCells;
    }

    public void setNeighborCells(ArrayList<Cell> neighborCells) {
        this.neighborCells = neighborCells;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public static HeroNeighbors findByHero(Collection<HeroNeighbors> list, Hero hero) {
        return list.stream().filter(object -> hero.equals(object.getHero())).findFirst().orElse(null);
    }
}
