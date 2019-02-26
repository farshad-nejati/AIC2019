package client.NewAI.move.noneZone;

import client.model.Cell;

public class ObjectiveCellsDistance {
    private Cell objectiveCell;
    private int distance;

    public ObjectiveCellsDistance(Cell objectiveCell, int distance) {
        this.objectiveCell = objectiveCell;
        this.distance = distance;
    }

    public Cell getObjectiveCell() {

        return objectiveCell;
    }

    public void setObjectiveCell(Cell objectiveCell) {
        this.objectiveCell = objectiveCell;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
