package client.NewAI.dodge;


import client.NewAI.Helper;
import client.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DodgeAction {

    public static void executeMove(World world, ArrayList<NoneZoneDodge> noneZoneDodges) {
        for (NoneZoneDodge noneZoneDodge : noneZoneDodges) {
            Hero hero = noneZoneDodge.getHero();
            Ability ability = noneZoneDodge.getAbility();
            Cell targetCell = noneZoneDodge.getTargetCell();
            world.castAbility(hero, ability, targetCell);
        }
        noneZoneDodges.clear();
    }

    public static void executeAction(World world, ArrayList<Hero> inZoneHeroes, ArrayList<DodgeStatus> inZoneDodgeStatuses) {
        ArrayList<Cell> allDangerCells = new ArrayList<>();
        for (Hero inZoneHero : inZoneHeroes) {
            ArrayList<Cell> inRangeCells = new ArrayList<Cell>(Arrays.asList(world.getMap().getObjectiveZone()));
            // TODO: fix max distance to areaEffect
            ArrayList<Cell> dangerCells = Helper.findInRangeCells(world, inRangeCells,  inZoneHero, 0, 3);
            DodgeAction.addTODangerCells(allDangerCells, dangerCells);
        }

        for (DodgeStatus dodgeStatusHero : inZoneDodgeStatuses) {
            if (dodgeStatusHero.isActive()) {
                if (world.getAP() > dodgeStatusHero.getAbility().getAPCost()) {
                    Hero hero = dodgeStatusHero.getHero();
                    Ability dodgeAbility = dodgeStatusHero.getAbility();
                    ArrayList<Cell> objectiveCells = new ArrayList<>(Arrays.asList(world.getMap().getObjectiveZone()));
                    ArrayList<Cell> inRangeCells = Helper.findInRangeCells(world, objectiveCells, hero, dodgeAbility);
                    // TODO: fix max distance to areaEffect
                    ArrayList<Cell> dangerCells = Helper.findInRangeCells(world, inRangeCells, hero, 0, 3);
                    DodgeAction.addTODangerCells(allDangerCells, dangerCells);
                    inRangeCells = getLowerDangerousCells(inRangeCells, allDangerCells);

                    // TODO: select from in range Cells with enough distance to other friend heroes
                    if (inRangeCells.size() > 0) {
                        Cell randomCell = inRangeCells.get(new Random().nextInt(inRangeCells.size()));
                        world.castAbility(hero, dodgeAbility, randomCell);
                    } else {
                        inZoneHeroes.add(dodgeStatusHero.getHero());
                    }
                }else {
                    inZoneHeroes.add(dodgeStatusHero.getHero());
                }
            }

        }
    }

    private static void addTODangerCells(ArrayList<Cell> allDangerCells, ArrayList<Cell> dangerCells) {
        for (Cell dangerCell : dangerCells) {
            if (!allDangerCells.contains(dangerCell)) {
                allDangerCells.add(dangerCell);
            }
        }
    }

    private static ArrayList<Cell> getLowerDangerousCells(ArrayList<Cell> inRangeCells, ArrayList<Cell> dangerCells) {
        ArrayList<Cell> lowerDangerousCells = new ArrayList<>();
        for (Cell inRangeCell : inRangeCells) {
            if ( !dangerCells.contains(inRangeCell) ) {
                lowerDangerousCells.add(inRangeCell);
            }
        }
        return lowerDangerousCells;
    }


}
