package client.RandomAI;

import client.model.Cell;
import client.model.Hero;

public class Moving {
    private int heroID;
    private boolean selected;
    private Cell objectiveCell;

//    public String print() {
//
//        String returnObjectiveCell = objectiveCell == null ? " Objective cell is empty \n" : "Hero Objective Cell: " + objectiveCell.getRow()
//                + " " + objectiveCell.getColumn() + "\n";
//
//        return
//                "hero: " + hero + "\n"
//                + "Hero Cell: " + hero.getCurrentCell().getRow()
//                + " " + hero.getCurrentCell().getColumn() + "\n"
//                + " is Selected: " + isSelected() + "\n"
//                + " " + returnObjectiveCell;
//    }

//    public Moving(Hero hero, boolean selected) {
//        this.hero = hero;
//        this.selected = selected;
//        this.objectiveCell = null;
//    }
//    public Moving(Hero hero, boolean selected, Cell objectiveCell) {
//        this.hero = hero;
//        this.selected = selected;
//        this.objectiveCell = objectiveCell;
//    }


    public Moving(int heroID, boolean selected) {
        this.heroID = heroID;
        this.selected = selected;
        this.objectiveCell = null;
    }

    public Moving(int heroID, boolean selected, Cell objectiveCell) {
        this.heroID = heroID;
        this.selected = selected;
        this.objectiveCell = objectiveCell;
    }

    public Cell getObjectiveCell() {
        return objectiveCell;
    }

    public void setObjectiveCell(Cell objectiveCell) {
        this.objectiveCell = objectiveCell;
    }

//    public Hero getHero() {
//        return hero;
//    }
//
//    public void setHero(Hero hero) {
//        this.hero = hero;
//    }


    public int getHeroID() {
        return heroID;
    }

    public void setHeroID(int heroID) {
        this.heroID = heroID;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
