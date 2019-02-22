package client.NewAI.move.noneZone;

import client.Printer;
import client.model.Cell;
import client.model.Direction;
import client.model.Hero;
import client.model.World;

import java.util.ArrayList;

public class NoneZoneMoving {
    private ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells;

    public NoneZoneMoving(ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells) {
        this.respawnObjectiveZoneCells = respawnObjectiveZoneCells;
    }

    public void move(World world, ArrayList<Hero> noneZoneHeroes, ArrayList<Cell> blockedCells) {
        for (Hero hero : noneZoneHeroes) {

            for (RespawnObjectiveZoneCell respawnObjectiveZoneCell : this.respawnObjectiveZoneCells) {
                Cell objectiveCell = respawnObjectiveZoneCell.getObjectiveZoneCell();
                if (respawnObjectiveZoneCell.getHero().getId() == hero.getId()) {
                    blockedCells.remove(objectiveCell);
                    respawnObjectiveZoneCell.setHero(hero);
                    Direction[] directions = world.getPathMoveDirections(hero.getCurrentCell(), objectiveCell, blockedCells);
                    new Printer().printDirections(directions);
                    Direction direction = directions[0];
                    world.moveHero(hero, direction);
                    blockedCells.add(objectiveCell);
                    break;
                }
            }
        }
    }
}
