package client.RandomAI;

import client.NewAI.Helper;
import client.NewAI.action.ActiveMyHeroes;
import client.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class RandomAction {

    public void randomAction(World world, ArrayList<Hero> activeMyHeroes) {
        ArrayList<Hero> inVisionOppHeroes = getInVisionOppHeroes(world);
        ArrayList<EffectiveTarget> effectiveTargets = getEffectiveTargetForInVisionOppHeroes(world, inVisionOppHeroes, activeMyHeroes);
        if (effectiveTargets.isEmpty()) {
            return;
        }

//        effectiveHeroes = getTargetForEachMyHeroes(effectiveHeroes);
        for (EffectiveTarget effectiveTarget: effectiveTargets) {
            Hero myHero = effectiveTarget.getMyHero();
            doAction(world, myHero, effectiveTarget.getTargetCell(), effectiveTarget.getAbility());
        }
    }




    public void doAction(World world, Hero myHero, Cell targetCell, Ability ability) {
        world.castAbility(myHero, ability, targetCell);
        int row =targetCell.getRow();
        int column =targetCell.getColumn();
//        System.out.println("\n\n" + ability.getName() + myHero.getId() + " ability used with " + myHero.getName());
//        System.out.println("in Position: " + row + " , " + column + "\n\n");
    }

//    public ArrayList<EffectiveHero> getTargetForEachMyHeroes(ArrayList<EffectiveHero> effectiveHeroes) {
//        ArrayList<EffectiveHero> targetForEachMyHero = new ArrayList<>();
//        ArrayList<Integer> targetHeroIDs = new ArrayList();
//
//        for (EffectiveHero effectiveHero: effectiveHeroes) {
//            Integer myHeroID = effectiveHero.getMyHero().getId();
//            if (!targetHeroIDs.contains(myHeroID)) {
//                targetForEachMyHero.add(effectiveHero);
//                targetHeroIDs.add(myHeroID);
//            } else {
//                EffectiveHero currentEffective = EffectiveHero.findByHero(targetForEachMyHero, effectiveHero.getMyHero());
//                int currentHP =  currentEffective.getOppHero().getCurrentHP();
//                int newHP = effectiveHero.getOppHero().getCurrentHP();
//                if (newHP < currentHP) {
//                    int index = targetForEachMyHero.indexOf(currentEffective);
//                    targetForEachMyHero.set(index, effectiveHero);
//                } else if (currentHP == newHP) {
//                    int index = targetForEachMyHero.indexOf(currentEffective);
//                    targetForEachMyHero.set(index, effectiveHero);
//                    EffectiveHero effectiveHeroMaxOppHero = findMaxOppHeroWithEqualHP(targetForEachMyHero,currentEffective, effectiveHero);
//                    targetForEachMyHero.set(index, effectiveHeroMaxOppHero);
//                }
//            }
//        }
//        return targetForEachMyHero;
//    }
//
//    private EffectiveHero findMaxOppHeroWithEqualHP(ArrayList<EffectiveHero> targetForEachMyHero, EffectiveHero currentEffective, EffectiveHero newEffective) {
//        int currentChance = 0;
//        int newChance = 0;
//        Hero currentEffectiveTarget = currentEffective.getOppHero();
//        Hero newEffectiveTarget = newEffective.getOppHero();
//
//        for (EffectiveHero effectiveHero : targetForEachMyHero) {
//            Hero heroEffective = effectiveHero.getOppHero();
//            if (heroEffective.getId() == currentEffectiveTarget.getId()) {
//                currentChance++;
//            } else if (heroEffective.getId() == newEffectiveTarget.getId()) {
//                newChance++;
//            }
//        }
//        if (newChance > currentChance) {
//            return newEffective;
//        }
//
//        return currentEffective;
//    }

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

    public ArrayList<EffectiveTarget> getEffectiveTargetForInVisionOppHeroes(World world, ArrayList<Hero> inVisionOppHeroes, ArrayList<Hero> activeMyHeroes) {
        ArrayList<EffectiveTarget> effectiveTargets = new ArrayList<>();
        int remainAP = world.getAP();
        for (Hero myHero: activeMyHeroes) {
            Ability ability = getPowerfulAbility(myHero, remainAP);
            if (ability == null) {
                continue;
            }
            remainAP -= ability.getAPCost();
            CandidateActionCell candidateActionCell = getBestCellToAttack(world, inVisionOppHeroes, myHero, ability, effectiveTargets);
            if (candidateActionCell != null) {
                effectiveTargets.add(new EffectiveTarget(myHero, ability, candidateActionCell));
            }
        }
        return effectiveTargets;
    }

    private Ability getPowerfulAbility(Hero myHero, int remainAP) {
        Ability[] abilities = myHero.getOffensiveAbilities();
        int maxPower = 0;
        Ability returnAbility = null;
        for (Ability ability : abilities) {
            if (!ability.isReady()) {
                continue;
            }
            int newPower = ability.getPower();
            if (newPower > maxPower) {
                if(ability.getAPCost() <= remainAP) {
                    maxPower = newPower;
                    returnAbility = ability;
                } else {
//                    System.out.println(myHero.getName() + Integer.toString(myHero.getId()) + " apCost not enough");
//                    System.out.println("current AP: " + remainAP);
//                    System.out.println("ability AP: " + ability.getAPCost());
                }
            }
        }

        return returnAbility;
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
    public CandidateActionCell getBestCellToAttack(World world, ArrayList<Hero> inVisionOppHeroes, Hero myHero, Ability ability, ArrayList<EffectiveTarget> effectiveTargets) {

        ArrayList<CandidateActionCell> candidateCells = new ArrayList<>();
        ArrayList<Cell> inDistanceCells = new ArrayList<>();

        ArrayList<Cell> zoneCells = new ArrayList<>(Helper.twoDArrayToList(world.getMap().getCells()));
        // TODO: for each cell in objective cell find cells with range < max range of ability
        inDistanceCells = Helper.findInRangeCells(world, zoneCells, myHero, ability);

        // TODO: for each findded cell calculate lobbing ability impact cell
        for (Cell cell : inDistanceCells) {
            candidateCells.add(new CandidateActionCell(cell, 0));
            ArrayList<Cell> impactCells = LobbingAbilityImpactCells(world, cell, ability);
            ArrayList<Hero> impactHeroes = new ArrayList<>();

            CandidateActionCell candidateCell = CandidateActionCell.findByCell(candidateCells, cell);
            int index = candidateCells.indexOf(candidateCell);
            for (Hero inVisionHero : world.getOppHeroes()) {

                // TODO: if see any enemy in impact cells, chance of select of findded cell increased
                if (impactCells.contains(inVisionHero.getCurrentCell())) {
                    candidateCell.increaseAffectedHeroes();
                    impactHeroes.add(inVisionHero);
                }
            }
            candidateCell.setAffectedHeroes(impactHeroes);
            candidateCells.set(index, candidateCell);
        }

        Collections.sort(candidateCells, CandidateActionCell.affectedNumberOppHeroComparator);
        Collections.reverse(candidateCells);
        ArrayList<CandidateActionCell> equalCandidates = new ArrayList<>();
        equalCandidates = findEqualCandidate(candidateCells);

        if (equalCandidates.size() > 0) {
            CandidateActionCell bestCandidateCell = findBestCandidateCell(equalCandidates, effectiveTargets);
            return bestCandidateCell;
        }
        return null;
    }

    private CandidateActionCell findBestCandidateCell(ArrayList<CandidateActionCell> equalCandidates, ArrayList<EffectiveTarget> effectiveTargets) {
        Collections.sort(equalCandidates, CandidateActionCell.sumHPComparator);
        ArrayList<CandidateActionCell> equalHPCandidates = new ArrayList<>();
        equalHPCandidates = findEqualSumHPCandidates(equalCandidates);

        CandidateActionCell bestCandidate = null;
        int minRepeat = 0;
        for (CandidateActionCell equalHPCandidate : equalHPCandidates) {
            // TODO: find heroAffect list of effectiveTargets
            ArrayList<HeroAffects> heroAffects = findHeroAffects(effectiveTargets);
            // TODO: find number of repeat current opp heroes in heroAffectList
            int repeatNumber = findNumberOfRepeatInHeroEffects(equalHPCandidate, heroAffects);
            if (repeatNumber >= minRepeat) {
                minRepeat = repeatNumber;
                bestCandidate = equalHPCandidate;
            }
        }
        return bestCandidate;
    }

    private int findNumberOfRepeatInHeroEffects(CandidateActionCell equalHPCandidate, ArrayList<HeroAffects> heroAffects) {
        int repeat = 0;
        for (Hero hero : equalHPCandidate.getAffectedHeroes()) {
            HeroAffects heroAffect = HeroAffects.findByHero(heroAffects, hero);
            if (heroAffect != null) {
                repeat += heroAffect.getAffectedNumber();
            }

        }
        return repeat;
    }

    private ArrayList<HeroAffects> findHeroAffects(ArrayList<EffectiveTarget> effectiveTargets) {
        ArrayList<HeroAffects> heroAffects = new ArrayList<>();
        for (EffectiveTarget effectiveTarget: effectiveTargets) {
            ArrayList<Hero> effectiveOppHeroes = effectiveTarget.getCandidateActionCell().getAffectedHeroes();

            for (Hero hero : effectiveOppHeroes) {
                HeroAffects heroAffect = HeroAffects.findByHero(heroAffects, hero);
                if (heroAffect != null) {
                    int index = heroAffects.indexOf(heroAffect);
                    heroAffect.increaseAffected();
                    heroAffects.set(index, heroAffect);
                } else {
                    heroAffects.add(new HeroAffects(hero, 1));
                }
            }
        }
        return heroAffects;
    }

    private ArrayList<CandidateActionCell> findEqualSumHPCandidates(ArrayList<CandidateActionCell> candidateCells) {
        ArrayList<CandidateActionCell> equalCandidates = new ArrayList<>();
        int maxHP = 1000;
        for (CandidateActionCell candidateActionCell : candidateCells) {
            int sumHP = candidateActionCell.getSumHP();
            if (sumHP <= maxHP) {
                maxHP = sumHP;
                equalCandidates.add(candidateActionCell);
            }
        }
        return equalCandidates;
    }

    private ArrayList<CandidateActionCell> findEqualCandidate(ArrayList<CandidateActionCell> candidateCells) {
        ArrayList<CandidateActionCell> equalCandidates = new ArrayList<>();
        int minImpact = 1;
        for (CandidateActionCell candidateActionCell : candidateCells) {
            int impactNumber = candidateActionCell.getAffectedNumber();
            if (impactNumber >= minImpact) {
                minImpact = impactNumber;
                equalCandidates.add(candidateActionCell);
            }
        }
        return equalCandidates;
    }

    public ArrayList<Cell> LobbingAbilityImpactCells(World world, Cell targetCell, Ability ability) {
        ArrayList<Cell> lobbingAbilityImpactCells = new ArrayList<>();
        for (Cell cell : Helper.twoDArrayToList(world.getMap().getCells())) {
            int distance = world.manhattanDistance(targetCell, cell);
            if (distance <= ability.getAreaOfEffect()) {
                lobbingAbilityImpactCells.add(cell);
            }
        }
        return lobbingAbilityImpactCells;
    }

}
