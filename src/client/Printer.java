package client;

import client.NewAI.action.areaEffect.AreaEffect;
import client.NewAI.action.areaEffect.PowerFullAbility;
import client.model.*;

import java.util.ArrayList;

public class Printer {


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
            System.out.print("hero " + hero.getName() + hero.getId() + ": " + row + " , " + column);
            System.out.println("  HP: " + hero.getCurrentHP());
        }
    }

    public void printOppHeroList(World world) {
        Hero[] heroes = world.getOppHeroes();
        for (Hero hero: heroes){
            int row = hero.getCurrentCell().getRow();
            int column = hero.getCurrentCell().getColumn();
            System.out.print("Opp hero " + hero.getName()+ hero.getId() + ": " + row + " , " + column);
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

    public void printDirections(Direction[] directions) {
        System.out.print("\n\nDirections: ");
        for (Direction direction: directions) {
            System.out.print(direction.name() + " ");
        }
    }

    public void printPowerAbilities(ArrayList<PowerFullAbility> powerFullAbilities) {
        System.out.println("\npowerFullAbilities.size() = " + powerFullAbilities.size());
        for (PowerFullAbility powerFullAbility : powerFullAbilities) {
            System.out.println("\npowerFullAbility.getId() = " + powerFullAbility.getId());
            System.out.println("powerFullAbility.getRemCoolDown() = " + powerFullAbility.getRemCoolDown());
        }
    }

    public void printAreaEffectList(ArrayList<AreaEffect> areaEffectList) {
        System.out.println("\n Area Effect List Size : " + areaEffectList.size());

        for (AreaEffect areaEffect : areaEffectList) {
            System.out.println("\n");
            System.out.println("areaEffect.getId() = " + areaEffect.getId());
            System.out.println("areaEffect.getHero() = " + areaEffect.getHero());
            System.out.println("areaEffect.getAbility() = " + areaEffect.getAbility().getName());
            System.out.println("areaEffect.getMaxRange() = " + areaEffect.getMaxRange());
        }
    }
}
