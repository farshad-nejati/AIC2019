package client.NewAI.move.noneZone;

import client.model.Cell;
import client.model.Hero;

import java.util.Collection;

public class RespawnObjectiveZoneCell {
    private Cell objectiveZoneCell;
    private Cell respawnZoneCell;
    private Hero hero;

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

    public Cell getObjectiveZoneCell() {
        return objectiveZoneCell;
    }

    public void setObjectiveZoneCell(Cell objectiveZoneCell) {
        this.objectiveZoneCell = objectiveZoneCell;
    }

    public Cell getRespawnZoneCell() {
        return respawnZoneCell;
    }

    public void setRespawnZoneCell(Cell respawnZoneCell) {
        this.respawnZoneCell = respawnZoneCell;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }


    public static RespawnObjectiveZoneCell findByHero(Collection<RespawnObjectiveZoneCell> list, Hero hero) {
        return list.stream().filter(object -> hero.equals(object.getHero())).findFirst().orElse(null);
    }
}
