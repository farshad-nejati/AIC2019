package client.NewAI.dodge;

import client.NewAI.Helper;
import client.NewAI.move.noneZone.RespawnObjectiveZoneCell;
import client.model.*;

import java.util.ArrayList;

public class DodgeMove {
    public static ArrayList<NoneZoneDodge> executeMove(ArrayList<NoneZoneDodge> noneZoneDodges, World world, ArrayList<Hero> noneZoneHeroes, ArrayList<Hero> zoneHeroes, ArrayList<DodgeStatus> noneZoneDodgeStatuses, ArrayList<RespawnObjectiveZoneCell> reSpawnObjectiveCells) {
        noneZoneHeroes.clear();
        ArrayList<Cell> alreadySelectedCells = new ArrayList<>();
        for (DodgeStatus dodgeStatus : noneZoneDodgeStatuses) {
            Hero hero = dodgeStatus.getHero();
            boolean againAdd = true;
            if (dodgeStatus.isActive()) {
                ArrayList<Cell> blockedCells = Helper.getBlockedCells(reSpawnObjectiveCells, hero, zoneHeroes);
                RespawnObjectiveZoneCell respawnObjective = RespawnObjectiveZoneCell.findByHero(reSpawnObjectiveCells, hero);
                Cell targetCell = respawnObjective.getObjectiveZoneCell();

                int normalDistance = Helper.getPathDistance(world, hero.getCurrentCell(), targetCell, blockedCells);
                DodgeDistance bestDodgeDistance = getBestDodgeDistance(world, hero, targetCell, dodgeStatus.getAbility(), alreadySelectedCells);
                if (bestDodgeDistance != null) {
                    int dodgeDistance = bestDodgeDistance.getDistance();
                    Cell dodgeCell = bestDodgeDistance.getDodgeCell();

                    if (dodgeDistance < normalDistance - 6) {
                        noneZoneDodges.add(new NoneZoneDodge(hero, dodgeStatus.getAbility(), dodgeCell));
                        alreadySelectedCells.add(dodgeCell);
                        againAdd =false;
                    }
                }
            }
            if (againAdd){
                noneZoneHeroes.add(hero);
            }
        }
        return noneZoneDodges;
    }

    private static DodgeDistance getBestDodgeDistance(World world, Hero hero, Cell targetCell, Ability ability, ArrayList<Cell> blockedCells) {

        ArrayList<Cell> mapCells = Helper.twoDArrayToList(world.getMap().getCells());
        ArrayList<Cell> dodgeCells = Helper.findInRangeCells(world, mapCells, hero, ability);

        DodgeDistance minDistanceDodge = getMinDistance(world, dodgeCells, targetCell, blockedCells);
        return minDistanceDodge;
    }

    private static DodgeDistance getMinDistance(World world, ArrayList<Cell> cells, Cell targetCell, ArrayList<Cell> blockedCells) {
        double maxDistance = Double.POSITIVE_INFINITY;
        DodgeDistance  dodgeDistance = null;
        for (Cell cell : cells) {
            if (cell.isWall() || blockedCells.contains(cell)) {
                continue;
            }

            Direction[] directions = world.getPathMoveDirections(cell, targetCell);
            double distance = directions.length;
            if (distance < maxDistance) {
                dodgeDistance = new DodgeDistance(cell, (int) distance);
                maxDistance = distance;
            }
        }
        return dodgeDistance;
    }

}
