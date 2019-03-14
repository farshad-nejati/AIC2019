package client.NewAI.defense;

import client.NewAI.Helper;
import client.NewAI.action.areaEffect.AreaEffect;
import client.NewAI.dodge.DodgeHelper;
import client.model.*;

import java.util.ArrayList;
import java.util.Collections;

public class DefenseAction {
    public static void executeAction(World world, ArrayList<Hero> inZoneHeroes, ArrayList<AreaEffect> areaEffectList) {
        ArrayList<Hero> copyHeroes = new ArrayList<>(inZoneHeroes);
        for (Hero myHero : copyHeroes) {
            int index = inZoneHeroes.indexOf(myHero);

            if (myHero.getName().equals(HeroName.GUARDIAN)) {
                boolean useDefenseAbility = executeGuardianDefense(myHero, world, areaEffectList, copyHeroes);
                if (useDefenseAbility) {
                    inZoneHeroes.remove(index);
                }
            } else if (myHero.getName().equals(HeroName.HEALER)) {
//                executeHealerDefense(myHero);
            }
        }
    }

    private static boolean executeGuardianDefense(Hero myHero, World world, ArrayList<AreaEffect> areaEffectList, ArrayList<Hero> inZoneHeroes) {
        Ability defenceAbility = myHero.getAbility(AbilityName.GUARDIAN_FORTIFY);
        if (!defenceAbility.isReady()) {
            return false;
        }

        ArrayList<Hero> heroesThatCanDefendFrom = getHeroesThatCanDefendFrom(myHero, world, defenceAbility, inZoneHeroes);
        ArrayList<HeroLosingHealth> losingHealthArrayList = getLosingHealthArraylist(heroesThatCanDefendFrom, areaEffectList, world);
        losingHealthArrayList.sort(HeroLosingHealth.losingHealthComparator);

        if (!losingHealthArrayList.isEmpty()) {
            Hero targetHero = losingHealthArrayList.get(losingHealthArrayList.size() - 1).getHero();
            world.castAbility(myHero, defenceAbility, targetHero.getCurrentCell());
            return true;
        }

        return false;
//        ArrayList<Cell> mapCells = new ArrayList<>(Helper.twoDArrayToList(world.getMap().getCells()));
//        Helper.findInRangeCells(world,mapCells,myHero,defenceAbility);
    }

    private static ArrayList<HeroLosingHealth> getLosingHealthArraylist(ArrayList<Hero> heroesThatCanDefendFrom, ArrayList<AreaEffect> areaEffectList, World world) {
        ArrayList<HeroLosingHealth> losingHealthArrayList = new ArrayList<>();

        for (Hero myHero : heroesThatCanDefendFrom) {
            Integer losingHealthSum = 0;

            for (AreaEffect areaEffect : areaEffectList) {
                boolean canHit = false;
                Hero oppHero = areaEffect.getHero();

                if (oppHero.getCurrentCell().isInVision()) {
                    int distance = world.manhattanDistance(oppHero.getCurrentCell(), myHero.getCurrentCell());
                    Ability oppHeroMaximumAbility = areaEffect.getAbility();
                    Integer distanceThatCanHit = oppHeroMaximumAbility.getRange() + oppHeroMaximumAbility.getAreaOfEffect();
                    canHit = distance <= distanceThatCanHit;
                    if (canHit) {
                        losingHealthSum += oppHeroMaximumAbility.getPower();
                    }
                }
            }

            Ability myHeroAbility = Helper.getPowerfulAbility(myHero);
            int hittingHealth = myHeroAbility.getPower();

            HeroLosingHealth heroLosingHealth;
            if (losingHealthSum >= myHero.getCurrentHP()) {
                heroLosingHealth = new HeroLosingHealth(myHero, myHero.getCurrentHP(), hittingHealth);
            } else {
                heroLosingHealth = new HeroLosingHealth(myHero, losingHealthSum, hittingHealth);
            }

            if (hittingHealth < losingHealthSum)
                losingHealthArrayList.add(heroLosingHealth);
        }
        return losingHealthArrayList;
    }

    private static ArrayList<Hero> getHeroesThatCanDefendFrom(Hero myHero, World world, Ability defenceAbility, ArrayList<Hero> inZoneHeroes) {
        ArrayList<Hero> heroesThatCanDefendFrom = new ArrayList<>();
        for (Hero hero : inZoneHeroes) {
            int distance = world.manhattanDistance(myHero.getCurrentCell(), hero.getCurrentCell());
            if (distance < defenceAbility.getRange()) {
                heroesThatCanDefendFrom.add(hero);
            }
        }
        return heroesThatCanDefendFrom;
    }

    private static void executeHealerDefense(Hero myHero) {
        Ability defenceAbility = myHero.getAbility(AbilityName.HEALER_HEAL);
    }
}
