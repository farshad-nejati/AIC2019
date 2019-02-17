package client;

import client.model.*;

import java.util.ArrayList;
import java.util.Random;

public class RandomAI {

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

}
