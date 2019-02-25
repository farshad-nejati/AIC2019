package client.IntelligentAI;

import client.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Utility {
    public static HashMap<Cell, Direction> getCellNeighbors(Cell cell, Map map) {
        HashMap<Cell, Direction> cellNeighbors = new HashMap<>();
        int row = cell.getRow();
        int column = cell.getColumn();

        if (row<0 || column<0){
            return null;
        }
        cellNeighbors.put(map.getCell(row - 1, column), Direction.UP);
        cellNeighbors.put(map.getCell(row + 1, column), Direction.DOWN);
        cellNeighbors.put(map.getCell(row, column - 1), Direction.LEFT);
        cellNeighbors.put(map.getCell(row, column + 1), Direction.RIGHT);

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
            for (Hero otherOurHero : otherOurHeroes) {
                if (otherOurHero.getCurrentCell().equals(cellNeighbor)) {
                    myDirections.remove(castDirectionToMyDirection(cellNeighbors.get(cellNeighbor)));
                    break;
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
}
