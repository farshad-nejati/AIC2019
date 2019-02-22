package client.IntelligentAI;

import client.model.Cell;
import client.model.Direction;
import client.model.Hero;
import client.model.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Utility {
    public static HashMap<Cell, Direction> getCellNeighbors(Cell cell, Map map) {
        HashMap<Cell, Direction> cellNeighbors = new HashMap<>();
        int row = cell.getRow();
        int column = cell.getColumn();

        cellNeighbors.put(map.getCell(row - 1, column), Direction.LEFT);
        cellNeighbors.put(map.getCell(row + 1, column), Direction.RIGHT);
        cellNeighbors.put(map.getCell(row, column - 1), Direction.DOWN);
        cellNeighbors.put(map.getCell(row, column + 1), Direction.UP);

        return cellNeighbors;
    }

    public static ArrayList<MyDirection> getPossibleDirections(Hero myHero, Map virtualMap, ArrayList<Hero> otherOurHeroes) {
        ArrayList<MyDirection> myDirections = new ArrayList<>(Arrays.asList(MyDirection.values()));

        Cell myHeroCurrentCell = myHero.getCurrentCell();
        int row = myHeroCurrentCell.getRow();
        int column = myHeroCurrentCell.getColumn();

        HashMap<Cell, Direction> cellNeighbors = Utility.getCellNeighbors(myHeroCurrentCell, virtualMap);
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
}
