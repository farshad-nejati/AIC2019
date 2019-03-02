package client.NewAI.dodge;

import client.model.Cell;

public class DodgeDistance {
    private Cell dodgeCell;
    private int distance;

    public DodgeDistance(Cell dodgeCell, int distance) {
        this.dodgeCell = dodgeCell;
        this.distance = distance;
    }

    public Cell getDodgeCell() {
        return dodgeCell;
    }

    public void setDodgeCell(Cell dodgeCell) {
        this.dodgeCell = dodgeCell;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
