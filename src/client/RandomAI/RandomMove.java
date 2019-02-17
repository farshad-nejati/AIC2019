package client.RandomAI;

import client.Printer;
import client.model.Cell;
import client.model.Direction;
import client.model.Hero;
import client.model.World;

import java.util.ArrayList;
import java.util.Random;

public class RandomMove {
    ArrayList<Moving> movingHeroes;
    ArrayList<Cell> blockedObjectiveZones;

    public RandomMove(World world) {
        blockedObjectiveZones = new ArrayList();
        this.movingHeroes = new ArrayList<>();

        Hero[] myHeroes = world.getMyHeroes();
        for (Hero myHero: myHeroes) {
            this.movingHeroes.add(new Moving(myHero.getId(), false));
        }
    }

    public void moveToObjectiveZone(World world) {

        Cell selectedObjectiveCell;

        for (Moving movingHero: movingHeroes) {


            Hero myHero = null;
            for (Hero hero: world.getMyHeroes()) {
                if (movingHero.getHeroID() == hero.getId()) {
                    myHero = hero;
                    break;
                }
            }

//            System.out.println(movingHero.print());

            if (!movingHero.isSelected()) {

                selectedObjectiveCell = getRandomObjectiveCell(world, myHero.getCurrentCell(), blockedObjectiveZones);
                movingHero.setSelected(true);
                movingHero.setObjectiveCell(selectedObjectiveCell);
            } else {
                selectedObjectiveCell = movingHero.getObjectiveCell();
            }
            Direction[] directions;
            blockedObjectiveZones.remove(selectedObjectiveCell);
            Cell[] blockedCells = blockedObjectiveZones.toArray(new Cell[blockedObjectiveZones.size()]);
            if (blockedCells.length == 0) {
                directions = world.getPathMoveDirections(myHero.getCurrentCell(), selectedObjectiveCell);
            } else {
                directions = world.getPathMoveDirections(myHero.getCurrentCell(), selectedObjectiveCell, blockedCells);
            }
            blockedObjectiveZones.add(selectedObjectiveCell);
            new Printer().printDirections(directions);
            if (directions.length != 0) {
                try {
                    Direction direction = directions[0];
                    System.out.println("Direction 0 : " + direction);
                    world.moveHero(myHero, direction);
                } catch (Exception e) {
                    System.out.println("\n\n\n ERROR:  " + directions.length+ "\n\n\n\n");
                }
            }
        }
    }

    public void randomMove(World world) {

        Hero[] myHeroes = world.getMyHeroes();
        for (Hero myHero: myHeroes) {


            if (myHero.getCurrentCell().isInObjectiveZone()) {
                continue;
            }
            Direction[] directions;
            while (true) {
                Cell heroCell = myHero.getCurrentCell();

                Cell selectedObjectiveCell = getRandomObjectiveCell(world, heroCell, blockedObjectiveZones);
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




    public Direction getRandomDirection(World world) {
        Direction[] directions = Direction.values();
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        Direction randomDirection = directions[randomNumber];
        return randomDirection;
    }

    public Cell getRandomObjectiveCell(World world, Cell heroCell, ArrayList<Cell> blockedObjectiveZones) {
        Cell[] objectiveCells = world.getMap().getObjectiveZone();
        while (true) {
            Random random = new Random();
            int randomNumber = random.nextInt(objectiveCells.length);
            Cell randomCell = objectiveCells[randomNumber];
            if (randomCell.equals(heroCell) && blockedObjectiveZones.contains(randomCell)) {
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


//    public Hero getRandomHero(World world) {
//        Random random = new Random();
//        int randomNumber = random.nextInt(4);
//        Hero randomHero = world.getMyHeroes()[randomNumber];
//        return randomHero;
//    }

}
