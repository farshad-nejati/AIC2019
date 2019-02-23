package client.NewAI.move.inZone;

import client.model.Cell;

import java.util.Collection;

public class ObjectiveCellThreat {
    private Cell cell;
    private int threatNumber;
    private int threatHP;

    public ObjectiveCellThreat(Cell cell) {
        this.cell = cell;
        this.threatNumber = 0;
        this.threatHP = 0;

    }

    public ObjectiveCellThreat(Cell cell, int threatNumber, int threatHP) {
        this.cell = cell;
        this.threatNumber = threatNumber;
        this.threatHP = threatHP;
    }

    public Cell getCell() {

        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public int getThreatNumber() {
        return threatNumber;
    }

    public void setThreatNumber(int threatNumber) {
        this.threatNumber = threatNumber;
    }

    public int getThreatHP() {
        return threatHP;
    }

    public void setThreatHP(int threatHP) {
        this.threatHP = threatHP;
    }

    public static ObjectiveCellThreat findByCell(Collection<ObjectiveCellThreat> list, Cell cell) {
        return list.stream().filter(object -> cell.equals(object.getCell())).findFirst().orElse(null);
    }
}
