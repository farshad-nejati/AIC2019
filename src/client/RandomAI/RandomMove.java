package client.RandomAI;

import client.model.Cell;
import client.model.Direction;
import client.model.Hero;
import client.model.World;

import java.util.Random;

public class RandomMove {
    public void randomMove(World world) {

        Hero[] myHeroes = world.getMyHeroes();
        for (Hero myHero: myHeroes) {

            if (myHero.getCurrentCell().isInObjectiveZone()) {
                continue;
            }
            Direction[] directions;
            while (true) {
                Cell heroCell = myHero.getCurrentCell();

                Cell selectedObjectiveCell = getRandomObjectiveCell(world, heroCell);
                if (heroCell.getColumn() != selectedObjectiveCell.getColumn() && heroCell.getRow() != selectedObjectiveCell.getRow()) {
                    directions = world.getPathMoveDirections(heroCell, selectedObjectiveCell, getMyHeroesCells(world));
                    break;
                }
            }
            if (directions != null) {
                try {

                    Direction direction = directions[0];
                    world.moveHero(myHero, direction);
                } catch (Exception e) {
                    System.out.println("\n\n\n ERROR:  " + directions.length+ "\n\n\n\n");
                }
            }
        }
    }



    public Hero getRandomHero(World world) {
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        Hero randomHero = world.getMyHeroes()[randomNumber];
        return randomHero;
    }

    public Direction getRandomDirection(World world) {
        Direction[] directions = Direction.values();
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        Direction randomDirection = directions[randomNumber];
        return randomDirection;
    }

    public Cell getRandomObjectiveCell(World world, Cell heroCell) {
        Cell[] objectiveCells = world.getMap().getObjectiveZone();
        while (true) {
            Random random = new Random();
            int randomNumber = random.nextInt(objectiveCells.length);
            if (objectiveCells[randomNumber].equals(heroCell)) {
                continue;
            }
            return objectiveCells[randomNumber];
        }
    }

    public Cell[] getMyHeroesCells(World world) {
        Hero[] myHeroes = world.getMyHeroes();
        Cell[] heroCells = new Cell[4];
        for (int i = 0; i < myHeroes.length; i++) {
            heroCells[i] = myHeroes[i].getCurrentCell();
        }
        return heroCells;
    }

}
