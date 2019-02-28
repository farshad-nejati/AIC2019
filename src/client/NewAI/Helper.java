package client.NewAI;

import client.NewAI.dodge.DodgeStatus;
import client.model.*;

import java.util.ArrayList;

public class Helper {


    public static ArrayList<Cell> findInRangeCells(World world, Hero myHero, Ability ability) {
        ArrayList<Cell> inRangeCells = new ArrayList<>();
        for (Cell cell : world.getMap().getObjectiveZone()) {
            int range = ability.getRange();
            int distance = world.manhattanDistance(myHero.getCurrentCell(), cell);
            if (distance <= range) {
                inRangeCells.add(cell);
            }
        }
        return inRangeCells;
    }
    public static ArrayList<Cell> findInRangeCells(World world, ArrayList<Cell> cells, Hero myHero, int minDistance, int maxDistance) {
        ArrayList<Cell> inRangeCells = new ArrayList<>();
        for (Cell cell : cells) {
            int distance = world.manhattanDistance(myHero.getCurrentCell(), cell);
            if (distance <= maxDistance && distance >= minDistance) {
                inRangeCells.add(cell);
            }
        }
        return inRangeCells;
    }


    public static boolean isPossibleDead(World world, Hero myHero) {
        int heroHP = myHero.getCurrentHP();
        int sumHP = 0;
        for (Hero oppHero : Helper.getInVisionOppHeroes(world)) {
            int abilityRange = Helper.getMaxRange(oppHero);
            int distance = world.manhattanDistance(myHero.getCurrentCell(), oppHero.getCurrentCell());
            if (abilityRange >= distance) {
                sumHP += Helper.getPowerFullAbility(oppHero);
            }
        }
        if (heroHP < sumHP) {
            return true;
        } else {
            return false;
        }
    }



    public static ArrayList<Hero> getInVisionOppHeroes(World world) {
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


    public static int getPowerFullAbility(Hero hero) {
        if (hero.getName().equals(HeroName.BLASTER)) {
            return 40;
        } else if (hero.getName().equals(HeroName.GUARDIAN)) {
            return 40;
        } else if (hero.getName().equals(HeroName.HEALER)) {
            return 25;
        } else {
            return 50;
        }
    }

    public static int getMaxRange(Hero hero) {

        if (hero.getName().equals(HeroName.BLASTER)) {
            return 7;
        } else if (hero.getName().equals(HeroName.GUARDIAN)) {
            return 2;
        } else if (hero.getName().equals(HeroName.HEALER)) {
            return 4;
        } else {
            return 7;
        }
    }
}
