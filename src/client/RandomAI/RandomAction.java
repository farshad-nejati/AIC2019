package client.RandomAI;

import client.model.*;

import java.util.ArrayList;
import java.util.Collections;
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
        System.out.println("\n\n" + ability.getName() + myHero.getId() + " ability used with " + myHero.getName());
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
            } else {
                EffectiveHero currentEffective = EffectiveHero.findByHero(targetForEachMyHero, effectiveHero.getMyHero());
                int currentHP =  currentEffective.getOppHero().getCurrentHP();
                int newHP = effectiveHero.getOppHero().getCurrentHP();
                if (newHP < currentHP) {
                    int index = targetForEachMyHero.indexOf(currentEffective);
                    targetForEachMyHero.set(index, effectiveHero);
                } else if (currentHP == newHP) {
                    int index = targetForEachMyHero.indexOf(currentEffective);
                    targetForEachMyHero.set(index, effectiveHero);
                    EffectiveHero effectiveHeroMaxOppHero = findMaxOppHeroWithEqualHP(targetForEachMyHero,currentEffective, effectiveHero);
                    targetForEachMyHero.set(index, effectiveHeroMaxOppHero);
                }
            }
        }
        return targetForEachMyHero;
    }

    private EffectiveHero findMaxOppHeroWithEqualHP(ArrayList<EffectiveHero> targetForEachMyHero, EffectiveHero currentEffective, EffectiveHero newEffective) {
        int currentChance = 0;
        int newChance = 0;
        Hero currentEffectiveTarget = currentEffective.getOppHero();
        Hero newEffectiveTarget = newEffective.getOppHero();

        for (EffectiveHero effectiveHero : targetForEachMyHero) {
            Hero heroEffective = effectiveHero.getOppHero();
            if (heroEffective.getId() == currentEffectiveTarget.getId()) {
                currentChance++;
            } else if (heroEffective.getId() == newEffectiveTarget.getId()) {
                newChance++;
            }
        }
        if (newChance > currentChance) {
            return newEffective;
        }

        return currentEffective;
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
                            if (distance <= ability.getRange()) {
                                Cell targetCell = getBestCellToAttack(world, inVisionOppHeroes, myHero, oppHero, ability);
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
    public Cell getBestCellToAttack(World world, ArrayList<Hero> inVisionOppHeroes, Hero myHero, Hero oppHero, Ability ability) {
        if (! ability.isLobbing()) {
            return oppHero.getCurrentCell();
        }

        ArrayList<CandidateActionCell> candidateCells = new ArrayList<>();
        ArrayList<Cell> inDistanceCells = new ArrayList<>();

        // TODO: for each cell in objective cell find cells with range < max range of ability
        inDistanceCells = findInRangeObjectiveCells(world, myHero, ability);

        // TODO: for each findded cell calculate lobbing ability impact cell
        for (Cell cell : inDistanceCells) {
            candidateCells.add(new CandidateActionCell(cell, 0));
            ArrayList<Cell> impactCells = LobbingAbilityImpactCells(world, cell, ability);
            for (Hero inVisionHero : inVisionOppHeroes) {

                // TODO: if see any enemy in impact cells, chance of select of findded cell increased
                if (impactCells.contains(inVisionHero.getCurrentCell())) {
                    CandidateActionCell candidateCell = CandidateActionCell.findByCell(candidateCells, cell);
                    int index = candidateCells.indexOf(candidateCell);
                    candidateCell.increaseAffectedHeroes();
                    candidateCells.set(index, candidateCell);

                }
            }
        }

        if (candidateCells.size() > 0 ) {
            Collections.sort(candidateCells, CandidateActionCell.affectedNumberOppHeroComparator);
            int lastIndex = candidateCells.size()-1;
            return candidateCells.get(lastIndex).getImpactCell();
        }
        return null;
    }

    private ArrayList<Cell> findInRangeObjectiveCells(World world, Hero myHero, Ability ability) {
        ArrayList<Cell> inRangeCells = new ArrayList<>();
        for (Cell cell : world.getMap().getObjectiveZone()) {
            int range = ability.getRange() + ability.getAreaOfEffect();
            int distance = world.manhattanDistance(myHero.getCurrentCell(), cell);
            if (distance <= range) {
                inRangeCells.add(cell);
            }
        }
        return inRangeCells;
    }

    public ArrayList<Cell> LobbingAbilityImpactCells(World world, Cell targetCell, Ability ability) {
        ArrayList<Cell> lobbingAbilityImpactCells = new ArrayList<>();
        int raw = targetCell.getRow();
        int column = targetCell.getColumn();
        int areaEffect = ability.getAreaOfEffect() /2;
        int negativeAreaEffect = areaEffect * (-1);
        for (int i = negativeAreaEffect ; i <= areaEffect; i++) {
            for (int j = negativeAreaEffect; j <= areaEffect; j++) {
                Cell neighborCell = world.getMap().getCell(raw+i, column + j);
                if (neighborCell.isInObjectiveZone()) {
                    lobbingAbilityImpactCells.add(neighborCell);
                }
            }
        }
        return lobbingAbilityImpactCells;
    }

}
