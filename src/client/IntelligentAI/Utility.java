package client.IntelligentAI;

import client.NewAI.action.areaEffect.AreaEffect;
import client.NewAI.move.noneZone.RespawnObjectiveZoneCell;
import client.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Utility {
    public static HashMap<Cell, Direction> getCellNeighbors(Cell cell, Map map) {
        HashMap<Cell, Direction> cellNeighbors = new HashMap<>();
        int row = cell.getRow();
        int column = cell.getColumn();

        if (row < 0 || column < 0) {
            return null;
        }

//        Cell upCellNeighbor = map.getCell(row - 1, column);
        if (map.isInMap(row - 1, column)) {
            cellNeighbors.put(map.getCell(row - 1, column), Direction.UP);
        }

//        Cell downCellNeighbor = map.getCell(row + 1, column);
        if (map.isInMap(row + 1, column)) {
            cellNeighbors.put(map.getCell(row + 1, column), Direction.DOWN);
        }

//        Cell leftCellNeighbor = map.getCell(row, column - 1);
        if (map.isInMap(row, column - 1)) {
            cellNeighbors.put(map.getCell(row, column - 1), Direction.LEFT);
        }

//        Cell rightCellNeighbor = map.getCell(row, column + 1);
        if (map.isInMap(row, column + 1)) {
            cellNeighbors.put(map.getCell(row, column + 1), Direction.RIGHT);
        }

        return cellNeighbors;
    }

    public static ArrayList<MyDirection> getPossibleDirections(Hero myHero, World virtualWorld, ArrayList<Hero> otherOurHeroes) {
        ArrayList<MyDirection> myDirections = new ArrayList<>(Arrays.asList(MyDirection.values()));

        Cell myHeroCurrentCell = myHero.getCurrentCell();
        int row = myHeroCurrentCell.getRow();
        int column = myHeroCurrentCell.getColumn();

        HashMap<Cell, Direction> cellNeighbors = Utility.getCellNeighbors(myHeroCurrentCell, virtualWorld.getMap());
        for (Cell cellNeighbor : cellNeighbors.keySet()) {
            if (cellNeighbor.isWall()) {
                myDirections.remove(castDirectionToMyDirection(cellNeighbors.get(cellNeighbor)));
                continue;
            }
            if (!otherOurHeroes.isEmpty()) {
                for (Hero otherOurHero : otherOurHeroes) {
                    if (otherOurHero.getCurrentCell().equals(cellNeighbor)) {
                        myDirections.remove(castDirectionToMyDirection(cellNeighbors.get(cellNeighbor)));
                        break;
                    }
                }
            }

        }

        return myDirections;
    }


    public static Cell getCellFromDirection(Cell cell, MyDirection direction, Map map) {
        int row = cell.getRow();
        int column = cell.getColumn();

        if (direction.equals(MyDirection.LEFT)) {
            return map.getCell(row, column - 1);
        }
        if (direction.equals(MyDirection.RIGHT)) {
            return map.getCell(row, column + 1);
        }
        if (direction.equals(MyDirection.DOWN)) {
            return map.getCell(row + 1, column);
        }
        if (direction.equals(MyDirection.UP)) {
            return map.getCell(row - 1, column);
        }

        return cell;
    }

    public static MyDirection getDirectionFromCells(Cell currentCell, Cell nextCell) {
        int row = currentCell.getRow();
        int column = currentCell.getColumn();

        int row2 = nextCell.getRow();
        int column2 = nextCell.getColumn();

        if (row > row2 && column == column2) {
            return MyDirection.UP;
        }
        if (row < row2 && column == column2) {
            return MyDirection.DOWN;
        }
        if (row == row2 && column > column2) {
            return MyDirection.LEFT;
        }
        if (row == row2 && column < column2) {
            return MyDirection.RIGHT;
        }
        if (row == row2 && column == column2) {
            return MyDirection.FIX;
        }

        return MyDirection.FIX;
    }

    public static Direction castMyDirectionToDirection(MyDirection direction) {
        if (direction.equals(MyDirection.LEFT)) {
            return Direction.LEFT;
        }
        if (direction.equals(MyDirection.RIGHT)) {
            return Direction.RIGHT;
        }
        if (direction.equals(MyDirection.DOWN)) {
            return Direction.DOWN;
        }
        if (direction.equals(MyDirection.UP)) {
            return Direction.UP;
        }

        return null;
    }

    public static MyDirection castDirectionToMyDirection(Direction direction) {
        if (direction.equals(Direction.LEFT)) {
            return MyDirection.LEFT;
        }
        if (direction.equals(Direction.RIGHT)) {
            return MyDirection.RIGHT;
        }
        if (direction.equals(Direction.DOWN)) {
            return MyDirection.DOWN;
        }
        if (direction.equals(Direction.UP)) {
            return MyDirection.UP;
        }

        return null;
    }

    public static Cell getMyHeroTargetCell(Hero myHero, ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells, World virtualWorld, HashMap<Hero, Boolean> heroHashArrival) {
        RespawnObjectiveZoneCell respawnObjectiveZoneCell = RespawnObjectiveZoneCell.findByHero(respawnObjectiveZoneCells, myHero);
        if (respawnObjectiveZoneCell.isArrival() && heroHashArrival.containsKey(myHero)) {
            heroHashArrival.put(myHero, true);
        }
        if (heroHashArrival.get(myHero).equals(true))
            return null;
        return respawnObjectiveZoneCell.getObjectiveZoneCell();

    }

    public static int CanHitMaxDistance(Hero myHero) {
        Integer myHeroCanHitMaxDistance = -1;
        for (Ability myHeroAbility : myHero.getOffensiveAbilities()) {
            int canHitDistance = myHeroAbility.getRange() + myHeroAbility.getAreaOfEffect();
            if (myHeroAbility.isReady() && (canHitDistance > myHeroCanHitMaxDistance)) {
                myHeroCanHitMaxDistance = canHitDistance;
            }
        }
        return myHeroCanHitMaxDistance;
    }

    public static boolean willMyHeroGetKilled(Hero myHero, ArrayList<Hero> oppHeroes, ArrayList<AreaEffect> areaEffectListAIAlgorithm, World world) {

        boolean canHit;
        Integer losingHealthSum = 0;

        Cell myHeroCurrentCell = myHero.getCurrentCell();

        for (Hero oppHero : oppHeroes) {
            Cell oppHeroCurrentCell = oppHero.getCurrentCell();
            canHit = false;

            if (oppHeroCurrentCell.isInVision()) {
                int distance = world.manhattanDistance(myHeroCurrentCell, oppHeroCurrentCell);

                Integer maxRange = 0;
                Ability ability = null;
                for (AreaEffect areaEffect : areaEffectListAIAlgorithm) {
                    if (areaEffect.getHero().equals(oppHero)) {
                        ability = areaEffect.getAbility();
                        maxRange = areaEffect.getMaxRange();
                    }
                }

                canHit = distance <= maxRange;
                if (canHit) {
                    losingHealthSum += Objects.requireNonNull(ability).getPower();
                }
            }
        }

        if (losingHealthSum >= myHero.getCurrentHP())
            return true;
        else
            return false;
    }

    public static boolean mapRangeIsBig(World virtualWorld) {
        Cell[] objzoneCell = virtualWorld.getMap().getObjectiveZone();
        int minRow = objzoneCell[0].getRow();
        int maxRow = objzoneCell[0].getRow();
        int mincolumn = objzoneCell[0].getColumn();
        int maxcolumn = objzoneCell[0].getColumn();
        for (Cell obj : objzoneCell) {
            if (obj.getRow() > maxRow) {
                maxRow = obj.getRow();
            }
            if (obj.getRow() < minRow) {
                minRow = obj.getRow();
            }
            if (obj.getColumn() > maxcolumn) {
                maxcolumn = obj.getColumn();
            }
            if (obj.getColumn() < mincolumn) {
                mincolumn = obj.getColumn();
            }
        }

        int difRow = (maxRow - minRow) + 1;
        int difColumn = (maxcolumn - mincolumn) + 1;
        if (difColumn < 7 && difRow < 7) {
            return false;
        }
//        if ( difColumn/ difRow == 1){
        int maxZoneOnj = difColumn * difRow;
        double percent = maxZoneOnj * 0.6;
        if (objzoneCell.length < percent) {
            return false;
        } else {
            return true;
        }
//        }else {
//            return false;
//        }
    }

    public static void getAroundHitCells(Cell myHeroCurrentCell, Cell myOtherHeroCell, Cell condidateCell, Integer oppAbilityRangeEffect, ArrayList<Cell> condidateObjCells, World virtualWorld) {

//        int myotherRow = myOtherHeroCell.getRow();
//        int myotherColumn = myOtherHeroCell.getColumn();
//        if ((myotherRow == condidateCell.getRow()) && (myotherColumn == condidateCell.getColumn())) {
//            for (int i = myotherRow - oppAbilityRangeEffect; i <= myotherRow + oppAbilityRangeEffect; i++) {
//                for (int j = myotherColumn - oppAbilityRangeEffect; j <= myotherColumn + oppAbilityRangeEffect; j++) {
//                    Cell mapCell = virtualWorld.getMap().getCell(i, j);
//                    Integer manhatanDis = virtualWorld.manhattanDistance(myOtherHeroCell, mapCell);
//                    if (manhatanDis <= oppAbilityRangeEffect) {
//                        if (condidateObjCells.contains(mapCell)) {
//                            condidateObjCells.remove(mapCell);
//                            if (myHeroCurrentCell.getRow() == 19 && myHeroCurrentCell.getColumn() ==14){
//                                if (virtualWorld.getCurrentTurn()>= 24){
//                                    int ip = 0;
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
//        }
    }

    public static Integer threatNumber(Cell myHeroCurrentCell, ArrayList<Move> copyOfOppHeroesMove, Cell targetcell, World virtualWorld) {
        Integer number = 0;
        for (Move oppHeroMove : copyOfOppHeroesMove) {
            Cell oppHeroCurrentCell = oppHeroMove.getCurrentCell();

            boolean canHit = false;
            if (oppHeroCurrentCell.isInVision()) {
                int distance = virtualWorld.manhattanDistance(targetcell, oppHeroCurrentCell);
                Ability oppHeroMaximumAbility = oppHeroMove.getAbility();
//                int oppHeroMaximumPower = oppHeroMaximumAbility.getPower();
                Integer distanceThatCanHit = oppHeroMaximumAbility.getRange() + oppHeroMaximumAbility.getAreaOfEffect();
                canHit = distance <= distanceThatCanHit;
                if (canHit) {
                    number++;
                }
            }
        }
        return 0;
    }

    public static Integer distanceNUmber(Cell myHeroCurrentCell, Cell targetcell, World virtualWorld) {
        Integer distance = 0;
       distance = virtualWorld.getPathMoveDirections(myHeroCurrentCell,targetcell).length;
        return distance;
    }

}
