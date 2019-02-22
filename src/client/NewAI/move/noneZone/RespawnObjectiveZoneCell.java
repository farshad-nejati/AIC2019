package client.NewAI.move.noneZone;

import client.model.Cell;

public class RespawnObjectiveZoneCell {
    private Cell objectiveZoneCell;
    private Cell respawnZoneCell;

    public RespawnObjectiveZoneCell(Cell objectiveZoneCell, Cell respawnZoneCell) {
        this.objectiveZoneCell = objectiveZoneCell;
        this.respawnZoneCell = respawnZoneCell;
    }

    public Cell getRespawnZoneCell(Cell objectiveZoneCell) {
        return respawnZoneCell;
    }

    public Cell getObjectiveZoneCell(Cell respawnZoneCell) {
        return objectiveZoneCell;
    }
}
