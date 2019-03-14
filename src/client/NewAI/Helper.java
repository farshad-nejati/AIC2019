package client.NewAI;

import client.NewAI.action.areaEffect.AreaEffect;
import client.NewAI.action.areaEffect.AreaEffectHelper;
import client.NewAI.dodge.DodgeStatus;
import client.NewAI.dodge.NoneZoneDodge;
import client.NewAI.move.noneZone.RespawnObjectiveZoneCell;
import client.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Helper {


    public static ArrayList<Cell> findInRangeCells(World world, ArrayList<Cell> cells, Hero myHero, Ability ability) {
        //TODO: if ability is non lobbing, must check that target cell is in vision to hero
        ArrayList<Cell> inRangeCells = new ArrayList<>();
        for (Cell cell : cells) {
            if (ability.getAreaOfEffect() <= 0) {
                if (cell.isWall()) {
                    continue;
                }
            }
            if (!world.isInVision(myHero.getCurrentCell(), cell) && !ability.isLobbing()){
                continue;
            }
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


    public static boolean isPossibleDead(World world, Hero myHero, ArrayList<AreaEffect> areaEffectList) {
        int heroHP = myHero.getCurrentHP();
        int sumHP = 0;
        for (AreaEffect areaEffect : areaEffectList) {
            Hero oppHero = areaEffect.getHero();
            if (oppHero.getCurrentHP() > 0) {
                int abilityRange = areaEffect.getMaxRange();
                int distance = world.manhattanDistance(myHero.getCurrentCell(), oppHero.getCurrentCell());
                if (abilityRange >= distance) {
                    sumHP += areaEffect.getAbility().getPower();
                }
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

    public static Ability getPowerfulAbility(Hero myHero) {
        Ability[] abilities = myHero.getOffensiveAbilities();
        int maxPower = 0;
        Ability returnAbility = null;
        for (Ability ability : abilities) {
            if (!ability.isReady()) {
                continue;
            }
            int newPower = ability.getPower();
            if (newPower > maxPower) {
                maxPower = newPower;
                returnAbility = ability;
            }
        }
        return returnAbility;
    }



    public static ArrayList<Cell> getBlockedCells(ArrayList<RespawnObjectiveZoneCell> reSpawnObjectiveCells, Hero ignoreHero, ArrayList<Hero> inZoneHeroes) {
        ArrayList<Cell> blockedCells = new ArrayList<>();
//        for (Hero hero : inZoneHeroes) {
//            blockedCells.add(hero.getCurrentCell());
//        }
        for (RespawnObjectiveZoneCell respawnObjectiveZoneCell : reSpawnObjectiveCells) {
            if (respawnObjectiveZoneCell.isArrival()) {
                continue;
            }
            if (respawnObjectiveZoneCell.getHero().equals(ignoreHero)) {
                continue;
            }
            blockedCells.add(respawnObjectiveZoneCell.getObjectiveZoneCell());
        }
        return blockedCells;
    }
    public static ArrayList<Cell> getBlockedCellsForNoneZoneHeroes(ArrayList<Hero> noneZoneHeroes, ArrayList<NoneZoneDodge> noneZoneDodges, Hero ignoreHero, ArrayList<Hero> inZoneHeroes) {
        ArrayList<Cell> blockedCells = new ArrayList<>();
        for (Hero hero : inZoneHeroes) {
            blockedCells.add(hero.getCurrentCell());
        }
        for (NoneZoneDodge noneZoneDodge : noneZoneDodges) {
            Cell cell = noneZoneDodge.getHero().getCurrentCell();
            blockedCells.add(cell);
        }
//        for (Hero noneZoneHero : noneZoneHeroes) {
//            Cell cell = noneZoneHero.getCurrentCell();
//            blockedCells.add(cell);
//        }

        return blockedCells;
    }

    public static int getPathDistance(World world, Cell currentCell, Cell targetCell, ArrayList<Cell> blockedCells) {
        Direction[] directions = world.getPathMoveDirections(currentCell, targetCell, blockedCells);
        return directions.length;
    }

    public static  <T> ArrayList<T> twoDArrayToList(T[][] twoDArray) {
        ArrayList<T> list = new ArrayList<T>();
        for (T[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

    public static Hero getOppHeroByID(World world, int id) {
        for (Hero hero : world.getOppHeroes()) {
            if (hero.getId() == id) {
                return hero;
            }
        }
        return null;
    }

    public static ArrayList<Cell> getSortedObjectiveCells(World world) {
        ArrayList<Cell> objectiveCells = new ArrayList(Arrays.asList(world.getMap().getObjectiveZone()));
        Cell firstObjectiveCell = objectiveCells.get(0);
        ArrayList<Cell> returnObjectiveZone = new ArrayList<>();
        Cell myRespwanZone = world.getMap().getMyRespawnZone()[0];
        Cell oppRespwanZone = world.getMap().getOppRespawnZone()[0];
        int myDistance = world.manhattanDistance(myRespwanZone, firstObjectiveCell);
        int oppDstance = world.manhattanDistance(oppRespwanZone, firstObjectiveCell);
        if (myDistance <= oppDstance) {
            return objectiveCells;
        } else {
            Collections.reverse(objectiveCells);
            return objectiveCells;
        }

    }
}
