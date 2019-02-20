package client.RandomAI;

import client.model.*;

import java.util.ArrayList;
import java.util.Random;

public class RandomAction {

    public void randomAction(World world) {
        ArrayList<Hero> inVisionOppHeroes = getInVisionOppHeroes(world);
        ArrayList<EffectiveHero> effectiveHeroes = getEffectiveHeroesForInVisionOppHeroes(world, inVisionOppHeroes);
        if (effectiveHeroes.isEmpty()) {
            return;
        }

        effectiveHeroes = getTargetForEachMyHeroes(effectiveHeroes);
        for (EffectiveHero effectiveHero: effectiveHeroes) {
            Hero myHero = effectiveHero.getMyHero();
            doAction(world, myHero, effectiveHero.getTargetCell(), effectiveHero.getAbility());
        }
    }




    public void doAction(World world, Hero myHero, Cell targetCell, Ability ability) {
        world.castAbility(myHero, ability.getName(), targetCell);
        int row =targetCell.getRow();
        int column =targetCell.getColumn();
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

//                if (world.isInVision(myHero.getCurrentCell(), oppHero.getCurrentCell())) {
//                    Ability randomAbility = getRandomAbility(myHero);
                    Ability[] myHeroAbilities = myHero.getOffensiveAbilities();
                    for (Ability ability: myHeroAbilities) {
                        if (!ability.isLobbing()) {
                            if (!world.isInVision(myHero.getCurrentCell(), oppHero.getCurrentCell())) {
                                continue;
                            }
                        }
                        if (!ability.isReady()) {
                            continue;
                        }
                        int range = ability.getRange() + ability.getAreaOfEffect();
                        int distance = world.manhattanDistance(myHero.getCurrentCell(), oppHero.getCurrentCell());
                        if (distance <= range) {
                            if (distance > ability.getRange()) {
                                Cell targetCell = getCellInRangeOfHeroAttack(world, myHero, oppHero, ability);
                                effectiveHeroes.add(new EffectiveHero(myHero, oppHero, ability, targetCell));
                            } else {
                                effectiveHeroes.add(new EffectiveHero(myHero, oppHero, ability, oppHero.getCurrentCell()));
                            }
                        }
                    }
//                }
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

    public Cell getCellInRangeOfHeroAttack(World world, Hero myHero, Hero oppHero, Ability ability) {
        Cell myCell = myHero.getCurrentCell();
        int myRow = myCell.getRow();
        int myColumn = myCell.getColumn();

        Cell oppCell = oppHero.getCurrentCell();
        int oppRow = oppCell.getRow();
        int oppColumn = oppCell.getColumn();

        int areaEffect = ability.getAreaOfEffect();
        int distance = world.manhattanDistance(myHero.getCurrentCell(), oppHero.getCurrentCell());
        int bestPlaceRange = areaEffect - ((ability.getRange() + areaEffect) - distance);

        if (oppColumn > myColumn && Math.abs(oppColumn - myColumn) >= areaEffect) {
            return world.getMap().getCell(oppRow, oppColumn - bestPlaceRange);
        } else if (oppColumn < myColumn && Math.abs(oppColumn - myColumn) >= areaEffect) {
            return world.getMap().getCell(oppRow, oppColumn + bestPlaceRange);
        } else if (oppRow > myRow && Math.abs(oppRow - myRow) >= areaEffect) {
            return world.getMap().getCell(oppRow - bestPlaceRange, oppColumn);
        } else  {
            return world.getMap().getCell(oppRow + bestPlaceRange, oppColumn);
        }
    }

}
