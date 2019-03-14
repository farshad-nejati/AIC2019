package client.IntelligentAI;

//import client.model.Hero;

import client.model.Cell;

public class BestTargetCell {


    private Cell targetCell;
    private Integer threat;
    private Integer distance;

    public BestTargetCell(Cell targetCell, Integer threat, Integer distance) {
        this.targetCell = targetCell;
        this.threat = threat;
        this.distance = distance;
    }

    public Cell getTargetCell() {
        return targetCell;
    }

    public void setTargetCell(Cell targetCell) {
        this.targetCell = targetCell;
    }

    public Integer getThreat() {
        return threat;
    }

    public void setThreat(Integer threat) {
        this.threat = threat;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

}
