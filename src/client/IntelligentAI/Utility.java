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
                myDirections.remove(cellNeighbors.get(cellNeighbor));
                continue;
            }
            for (Hero otherOurHero : otherOurHeroes) {
                if (otherOurHero.getCurrentCell().equals(cellNeighbor)) {
                    myDirections.remove(cellNeighbors.get(cellNeighbor));
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
        if (direction.equals(MyDirection.LEFT)) {
            return map.getCell(row - 1, column);
        }

        return cell;
    }
}
