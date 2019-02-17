package client;

import client.model.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class AI
{
    private int pickNumber = 1;
    private int turnNumber = 0;
    private String mapSizeStatus; // normal, small, big
    private String mapWallStatus; // low, much, normal
    private String mapZoneNumbersStatus; // low, much, normal
    private String mapPathStatus; // linear, Square, circle, rectangle
    private String mapPathNumberStatus; // low, much, normal


    public void preProcess(World world) {
        System.out.println("pre process started");
        System.out.println("world Columns: " + world.getMap().getColumnNum());
        System.out.println("world Columns: " + world.getMap().getRowNum());
    }

    public void pickTurn(World world) {
//        System.out.println("pick started");
//        System.out.println("turn number: " + turnNumber);
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());

        HeroName heroName = pickHero();
        world.pickHero(heroName);

        pickNumber++;
        turnNumber++;
    }

    public void moveTurn(World world) {
//        System.out.println("move started");
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());
//        System.out.println("turn number: " + turnNumber);
//        printHeroList(world);
//        printOppHeroList(world);

        randomMove(world);


        printMap(world);
    }

    public void actionTurn(World world) {
//        System.out.println("action started");
//        System.out.println("turn number: " + turnNumber);
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());
        printHeroList(world);
        printOppHeroList(world);


        randomAction(world);

        turnNumber++;
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

    public void randomAction(World world) {
        ArrayList<Hero> inVisionOppHeroes = getInVisionOppHeroes(world);
        ArrayList<EffectiveHero> effectiveHeroes = getEffectiveHeroesForInVisionOppHeroes(world, inVisionOppHeroes);
        if (effectiveHeroes.isEmpty()) {
            return;
        }

        effectiveHeroes = getTargetForEachMyHeroes(effectiveHeroes);
        for (EffectiveHero effectiveHero: effectiveHeroes) {
            Hero myHero = effectiveHero.getMyHero();
            Hero oppHero = effectiveHero.getOppHero();
            Ability randomAbility = getRandomAbility(myHero);
            doAction(world, myHero, oppHero, randomAbility);
        }
    }

    public void doAction(World world, Hero myHero, Hero oppHero, Ability ability) {
        world.castAbility(myHero, ability.getName(), oppHero.getCurrentCell());
        int row =oppHero.getCurrentCell().getRow();
        int column =oppHero.getCurrentCell().getColumn();
        System.out.println("\n\n" + ability.getName() + " ability used with " + myHero.getName());
        System.out.println("in Position: " + row + " , " + column + "\n\n");
    }

    public ArrayList<EffectiveHero> getTargetForEachMyHeroes(ArrayList<EffectiveHero> effectiveHeroes) {
        ArrayList<EffectiveHero> targetForEachMyHero = new ArrayList<>();
        ArrayList<Integer> targetHeroIDs = new ArrayList();

        for (EffectiveHero effectiveHero: effectiveHeroes) {
            Integer myHeroID = effectiveHero.getMyHero().getId();
            if (!targetHeroIDs.contains(myHeroID)) {
                targetForEachMyHero.add(effectiveHero);
                targetHeroIDs.add(myHeroID);
            }
        }
        return targetForEachMyHero;
    }

    public ArrayList<Hero> getInVisionOppHeroes(World world) {
        Hero[] oppHeroes = world.getOppHeroes();
        ArrayList<Hero> availableOppHeroes = new ArrayList();
        for (Hero hero: oppHeroes) {
            Cell heroCell = hero.getCurrentCell();
            if (heroCell.isInVision() && heroCell.getColumn() != -1) {
                availableOppHeroes.add(hero);
            }
        }
        return availableOppHeroes;
    }

    public ArrayList<EffectiveHero> getEffectiveHeroesForInVisionOppHeroes(World world, ArrayList<Hero> inVisionOppHeroes) {
        ArrayList<EffectiveHero> effectiveHeroes = new ArrayList<>();
        Hero[] myHeroes = world.getMyHeroes();
        for (Hero myHero: myHeroes) {
            for (Hero oppHero: inVisionOppHeroes) {
                if (world.isInVision(myHero.getCurrentCell(), oppHero.getCurrentCell())) {
                    if (world.manhattanDistance(myHero.getCurrentCell(), oppHero.getCurrentCell()) <= 4) {
                        effectiveHeroes.add(new EffectiveHero(myHero, oppHero));
                    }
                }
            }
        }
        return effectiveHeroes;
    }

    public Ability getRandomAbility(Hero hero) {
        Ability[] abilities = hero.getOffensiveAbilities();
        Random random = new Random();
        int randomNumber = random.nextInt(abilities.length);
        return  abilities[randomNumber];
    }



    public HeroName pickHero() {
        switch (pickNumber) {
            case 1:{
                return HeroName.BLASTER;
            }
            case 2: {
                return HeroName.BLASTER;
            }case 3:{
                return HeroName.BLASTER;
            }
            default: {
                return HeroName.SENTRY;

            }

        }
    }

    public void printMap(World world) {
        Cell[][] cells = world.getMap().getCells();
        System.out.println("\n\n");
        for (Cell[] rowCell:cells) {
            for (Cell cell: rowCell) {
                boolean isHeroInMap = isHeroInCellForPrint(world, cell);
                boolean isOppHeroInMap = isOppHeroInCellForPrint(world, cell);
                if (isHeroInMap || isOppHeroInMap){
                    continue;
                }
                if (cell.isWall()) {
                    System.out.print(" #");
                } else if (cell.isInObjectiveZone()) {
                    System.out.print(" *");
                }else if (cell.isInMyRespawnZone()) {
                    System.out.print(" +");
                }else if(cell.isInOppRespawnZone()) {
                    System.out.print(" ^");
                } else{
                    System.out.print(" -");
                }
            }
            System.out.print("\n");
        }
        System.out.println("\n\n");
    }

    public void printHeroList(World world) {
        Hero[] heroes = world.getMyHeroes();
        for (Hero hero: heroes){
            int row = hero.getCurrentCell().getRow();
            int column = hero.getCurrentCell().getColumn();
            System.out.print("hero " + hero.getName() + ": " + row + " , " + column);
            System.out.println("  HP: " + hero.getCurrentHP());
        }
    }

    public void printOppHeroList(World world) {
        Hero[] heroes = world.getOppHeroes();
        for (Hero hero: heroes){
            int row = hero.getCurrentCell().getRow();
            int column = hero.getCurrentCell().getColumn();
            System.out.print("Opp hero " + hero.getName() + ": " + row + " , " + column);
            System.out.println("  HP: " + hero.getCurrentHP());
        }
    }

    public boolean isHeroInCellForPrint(World world, Cell cell) {

        for (Hero hero: world.getMyHeroes()) {
            Cell heroCell = hero.getCurrentCell();
            if (cell.equals(heroCell)) {
                if (hero.getName() == HeroName.SENTRY) {
                    System.out.print(" S");
                    return true;
                }
                else if (hero.getName() == HeroName.BLASTER) {
                    System.out.print(" B");
                    return true;
                }else if (hero.getName() == HeroName.HEALER) {
                    System.out.print(" H");
                    return true;
                }else if (hero.getName() == HeroName.GUARDIAN) {
                    System.out.print(" G");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOppHeroInCellForPrint(World world, Cell cell) {
        boolean isCellFull = false;
        for (Hero hero: world.getMyHeroes()) {
            if (cell.equals(hero.getCurrentCell())){
                isCellFull = true;
                break;
            }
        }

        for (Hero hero: world.getOppHeroes()) {
            Cell heroCell = hero.getCurrentCell();

            if (cell.equals(heroCell) && !isCellFull) {
                System.out.print(" O");
                return true;
            }
        }
        return false;
    }

}
