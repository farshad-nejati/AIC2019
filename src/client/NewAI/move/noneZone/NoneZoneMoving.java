package client.NewAI.move.noneZone;

import client.NewAI.Helper;
import client.NewAI.dodge.NoneZoneDodge;
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

    public void move(World world, ArrayList<Hero> noneZoneHeroes, ArrayList<Hero> inZoneHeroes, ArrayList<NoneZoneDodge> noneZoneDodges) {
        for (Hero hero : noneZoneHeroes) {

            ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells = new ArrayList<>(this.respawnObjectiveZoneCells);
            RespawnObjectiveZoneCell respawnObjectiveZoneCell = RespawnObjectiveZoneCell.findByHero(this.respawnObjectiveZoneCells, hero) ;
            int index = respawnObjectiveZoneCells.indexOf(respawnObjectiveZoneCell);
            respawnObjectiveZoneCells.remove(respawnObjectiveZoneCell);

            ArrayList<Cell> blockedCells = Helper.getBlockedCellsForNoneZoneHeroes(noneZoneHeroes, noneZoneDodges,  hero, inZoneHeroes);
//            ArrayList<Cell> blockedCells = findBlockedCells(respawnObjectiveZoneCells, inZoneHeroes);
            Cell objectiveCell = respawnObjectiveZoneCell.getObjectiveZoneCell();

            Direction[] directions = world.getPathMoveDirections(hero.getCurrentCell(), objectiveCell, blockedCells);
            if (directions.length > 0) {
                new Printer().printDirections(directions);
                Direction direction = directions[0];
                world.moveHero(hero, direction);
                respawnObjectiveZoneCell.setHero(hero);
                this.respawnObjectiveZoneCells.set(index, respawnObjectiveZoneCell);
            }

        }
    }

    private ArrayList<Cell> findBlockedCells(ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells, ArrayList<Hero> inZoneHeroes) {
        ArrayList<Cell> blockedCells = new ArrayList<>();
        for (Hero hero: inZoneHeroes) {
            blockedCells.add(hero.getCurrentCell());
        }
        for (RespawnObjectiveZoneCell obj : respawnObjectiveZoneCells) {
            blockedCells.add(obj.getObjectiveZoneCell());
        }
        return blockedCells;
    }
}
