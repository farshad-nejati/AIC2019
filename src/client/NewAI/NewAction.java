package client.NewAI;

import client.RandomAI.EffectiveHero;
import client.model.Ability;
import client.model.Cell;
import client.model.Hero;
import client.model.World;

import java.util.*;

public class NewAction {

    public void Action(World world) {
        ArrayList<Hero> inVisionOppHeroes = getInVisionOppHeroes(world);
        ArrayList<ActiveMyHeroes> activeMyHeroes = getActiveMyHeroes(world, inVisionOppHeroes);

        while (activeMyHeroes.size() != 0 ) {

            EffectiveHero effectiveHero = null;
            ArrayList<OppHeroAction> candidateOppHeroes = new ArrayList<>();

            ArrayList<TakingParts> takingParts = initializeTakingParts(activeMyHeroes);
            List<OppHeroAction> oppHeroActions = new ArrayList<>();

            for (Hero oppHero: world.getOppHeroes()) {
                OppHeroAction oppHeroAction = new OppHeroAction(world, oppHero, activeMyHeroes);
                takingParts = oppHeroAction.getAllPossibleAbilities(takingParts);
                oppHeroActions.add(oppHeroAction);
            }

            Collections.sort(takingParts, (o1, o2) -> o1.getMinPartners().compareTo(o2.getMinPartners()));

            TakingParts firstTakingPart = takingParts.get(0);
            Hero selectedMyHero = firstTakingPart.getMyHero();
            ActiveMyHeroes selectedActiveMyHero = findActiveMyHeroByHero(activeMyHeroes, selectedMyHero);
            boolean canKill = false;
            for (OppHeroAction oppHeroAction: oppHeroActions) {
                if (oppHeroAction.isPossibleDead()) {
                    canKill = true;
                }
            }

            if (canKill) {
                Hero selectedOppHero = null;
                if (selectedActiveMyHero != null) {
//                    Collections.sort(oppHeroActions, OppHeroAction.KillerOppHeroesComparator);
                    for (OppHeroAction oppHeroAction : oppHeroActions) {
                        Hero oppHero = oppHeroAction.getOppHero();
                        if (selectedActiveMyHero.getPossibleOppHeroes().contains(oppHero) && oppHeroAction.getKillerOppHeroes().size() > 0) {
                            candidateOppHeroes.add(oppHeroAction);
                        }
                    }
                }
                ArrayList<MyHeroTargets> myHeroTargets = new ArrayList<>();

                if (candidateOppHeroes.size() > 0) {
                    for (OppHeroAction oppHeroAction: candidateOppHeroes) {
                        ArrayList<KillerOppHero> candidateKillers = new ArrayList<>();
                        ArrayList<KillerOppHero> killerOppHeroes = (ArrayList<KillerOppHero>) oppHeroAction.getKillerOppHeroes();
                        for (KillerOppHero killerOppHero : killerOppHeroes) {
                            ArrayList<HeroAbility> innerKillers = killerOppHero.getHeroAbilities();
                            if (firstTakingPart.getMinPartners() + 1 == killerOppHero.getNumOfHeroes()) {
                                for (HeroAbility innerKiller : innerKillers) {
                                    if (innerKiller.getMyHero().equals(selectedMyHero)) {
                                        Hero oppHero = oppHeroAction.getOppHero();
                                        MyHeroTargets myHeroTarget = new MyHeroTargets(oppHero, killerOppHero, killerOppHero.getApSum());
                                        myHeroTargets.add(myHeroTarget);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                Collections.sort(myHeroTargets, MyHeroTargets.myHeroTargetsComparator);
                MyHeroTargets selectedMyHeroTarget = myHeroTargets.get(0);

                Hero myHero = selectedMyHero;
                Hero oppHero = selectedMyHeroTarget.getOppHero();
                ArrayList<HeroAbility> heroAbilities = selectedMyHeroTarget.getKillerOppHero().getHeroAbilities();
                Ability ability = null;
                for (HeroAbility heroAbility : heroAbilities) {
                    if (heroAbility.getMyHero().equals(myHero)) {
                        ability = heroAbility.getAbility();
                        break;
                    }
                }
                Cell targetCell = null;
                if (ability != null) {

                    targetCell = getCellInRangeOfHeroAttack(world, myHero, oppHero, ability);
                }
                effectiveHero = new EffectiveHero(myHero, oppHero, ability, targetCell);



                if (effectiveHero != null) {
                    Ability ability2 = effectiveHero.getAbility();
                    world.castAbility(effectiveHero.getMyHero(), ability2, effectiveHero.getTargetCell());
                    Hero oppHero2 = effectiveHero.getOppHero();
                    for (OppHeroAction oppHeroAction: candidateOppHeroes) {
                        if (oppHeroAction.getOppHero().equals(oppHero2)) {
                            Integer virtualHP = oppHeroAction.getVirtualHP();
                            oppHeroAction.setVirtualHP(virtualHP - ability2.getPower());
                            break;
                        }
                    }

                }
            } else {
                //TODO: my algorithm for if opp hero not dead
            }
            activeMyHeroes.remove(selectedActiveMyHero);
        }
    }



    public ActiveMyHeroes findActiveMyHeroByHero(ArrayList<ActiveMyHeroes> activeMyHeroes, Hero hero) {
        for (ActiveMyHeroes activeMyHero : activeMyHeroes) {
            if (activeMyHero.getMyHero().getId() == hero.getId()) {
                return activeMyHero;
            }
        }
        return null;
    }

    public ArrayList<TakingParts> initializeTakingParts(ArrayList<ActiveMyHeroes> activeMyHeroes) {
        ArrayList<TakingParts> takingParts = new ArrayList<>();
        for (ActiveMyHeroes activeMyHero : activeMyHeroes) {
            Hero myHero = activeMyHero.getMyHero();
            takingParts.add(new TakingParts(myHero, 10, 10));
        }
        return takingParts;
    }

    public ArrayList<ActiveMyHeroes> getActiveMyHeroes(World world, ArrayList<Hero> inVisionOppHeroes) {
        ArrayList<ActiveMyHeroes> activeMyHeroes = new ArrayList<>();
        for (Hero myHero : world.getMyHeroes()) {
            ArrayList<Hero> oppHeroes = new ArrayList<>();
            for (Hero oppHero: inVisionOppHeroes) {
                ArrayList possibleAbilities = new ArrayList();
                Ability[] myHeroAbilities = myHero.getOffensiveAbilities();

                for (Ability ability : myHeroAbilities) {
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
                        oppHeroes.add(oppHero);
                    }
                }
            }
            if (oppHeroes.size() > 0) {
                activeMyHeroes.add(new ActiveMyHeroes(myHero, oppHeroes));
            }
        }
        return activeMyHeroes;
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



    public ArrayList<TakingParts> sortTakingPartsByMinPartners(ArrayList<TakingParts> takingParts) {

        Collections.sort(takingParts, new Comparator<TakingParts>(){
            public int compare(TakingParts t1, TakingParts t2) {
                return t1.getMinPartners().compareTo(t2.getMinPartners());
            }
        });
        return takingParts;
    }

    public ArrayList<TakingParts> sortTakingPartsByNumberOfCollaborations(ArrayList<TakingParts> takingParts) {

        Collections.sort(takingParts, new Comparator<TakingParts>(){
            public int compare(TakingParts t1, TakingParts t2) {
                return t1.getNumberOFCollaboration().compareTo(t2.getNumberOFCollaboration());
            }
        });
        return takingParts;
    }

}
