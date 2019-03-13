package client.NewAI.action.areaEffect;

import client.NewAI.Helper;
import client.Printer;
import client.model.*;

import java.util.ArrayList;
import java.util.Arrays;

public class AreaEffectHelper {

    public static ArrayList<AreaEffect> initialAffectArea(World world) {
        ArrayList<AreaEffect> areaEffects = new ArrayList<>();
        for (Hero hero : world.getOppHeroes()) {
            Ability ability = getPowerFullAbility(hero);
            AbilityConstants  abilityConstants = getAbilityConstant(world, ability);
            int maxRange = abilityConstants.getRange() + abilityConstants.getAreaOfEffect();

            areaEffects.add(new AreaEffect(hero, hero.getId(), ability, maxRange));
        }
        return areaEffects;
    }

    public static void updateAreaEffects(World world, ArrayList<AreaEffect> areaEffectList, ArrayList<PowerFullAbility> powerFullAbilities) {
        for (PowerFullAbility powerFullAbility : powerFullAbilities) {
            int id = powerFullAbility.getId();
            int remCoolDown = powerFullAbility.getRemCoolDown();

            Hero updatedHero = Helper.getOppHeroByID(world, id);
            AreaEffect areaEffect = AreaEffect.findByID(areaEffectList, id);
            int index = areaEffectList.indexOf(areaEffect);
            areaEffect.setHero(updatedHero);

            Ability ability = getPowerFullAbility(areaEffect.getHero());
            AbilityConstants  abilityConstants = getAbilityConstant(world, ability);
            if (remCoolDown == abilityConstants.getCooldown()-1 ) {
                areaEffect = setPowerFullAreaEffect(world, areaEffect);
            } else {
                areaEffect = setLowerPowerFullAreaEffect(world, areaEffect);
            }
            areaEffectList.set(index, areaEffect);
        }
    }

    private static AreaEffect setPowerFullAreaEffect(World world, AreaEffect areaEffect) {

        Hero hero = areaEffect.getHero();
        Ability ability = getPowerFullAbility(hero);
        AbilityConstants  abilityConstants = getAbilityConstant(world, ability);
        int maxRange = abilityConstants.getRange() + abilityConstants.getAreaOfEffect();

        return new AreaEffect(hero, hero.getId(), ability, maxRange);
    }

    private static AreaEffect setLowerPowerFullAreaEffect(World world, AreaEffect areaEffect) {

        Hero hero = areaEffect.getHero();
        Ability ability = getLowerPowerFullAbility(hero);
        AbilityConstants  abilityConstants = getAbilityConstant(world, ability);
        int maxRange = abilityConstants.getRange() + abilityConstants.getAreaOfEffect();

        return new AreaEffect(hero, hero.getId(), ability, maxRange);
    }

    public static ArrayList<PowerFullAbility> initialPowerFullAbility(World world) {
        ArrayList<PowerFullAbility> powerFullAbilities = new ArrayList<>();
        for (Hero hero : world.getOppHeroes()) {
            powerFullAbilities.add(new PowerFullAbility(hero.getId(), 0));
        }
        return powerFullAbilities;
    }

    public static void updatePowerFullAbility(World world, ArrayList<PowerFullAbility> powerFullAbilities, ArrayList<AreaEffect> areaEffectList) {
        ArrayList<CastAbility> castAbilities = new ArrayList<>(Arrays.asList(world.getOppCastAbilities()));
        for (CastAbility castAbility : castAbilities) {

            int id = castAbility.getCasterId();
            if (id == -1){
                continue;
            }
            AreaEffect areaEffect = AreaEffect.findByID(areaEffectList, id);
            PowerFullAbility powerFullAbility = PowerFullAbility.findByID(powerFullAbilities, id);
            int index = powerFullAbilities.indexOf(powerFullAbility);

//            new Printer().printAreaEffectList(areaEffectList);
            Ability powerAbility = AreaEffectHelper.getPowerFullAbility(areaEffect.getHero());
            AbilityName powerFullAbilityName = powerAbility.getName();
            if (castAbility.getAbilityName().equals(powerFullAbilityName)) {
                powerFullAbility.setRemCoolDown(0);
            } else {
                powerFullAbility.increaseRemCoolDown();
                AbilityConstants  abilityConstants = getAbilityConstant(world, powerFullAbilityName);
                if (powerFullAbility.getRemCoolDown() >  abilityConstants.getCooldown() - 1) {
                    powerFullAbility.setRemCoolDown(0);
                }
            }
            powerFullAbilities.set(index, powerFullAbility);
        }

        }
    private static AbilityConstants getAbilityConstant(World world, Ability ability) {
        AbilityConstants[] abilityConstants = world.getAbilityConstants();
        for (AbilityConstants abilityConstant : abilityConstants) {
            if (abilityConstant.getName().equals(ability.getName())) {
                return abilityConstant;
            }
        }
        return null;
    }
    private static AbilityConstants getAbilityConstant(World world, AbilityName abilityName) {
        AbilityConstants[] abilityConstants = world.getAbilityConstants();
        for (AbilityConstants abilityConstant : abilityConstants) {
            if (abilityConstant.getName().equals(abilityName)) {
                return abilityConstant;
            }
        }
        return null;
    }

    public static void printCastAbilities(World world) {
        for (CastAbility castAbility : world.getOppCastAbilities()) {
            System.out.println("\n\ncastAbility.getCasterId() = " + castAbility.getCasterId());
            System.out.println("castAbility.getAbilityName() = " + castAbility.getAbilityName());
        }
    }


    public static Ability getPowerFullAbility(Hero hero) {
        if (hero.getName().equals(HeroName.BLASTER)) {
            return hero.getAbility(AbilityName.BLASTER_BOMB);
        } else if (hero.getName().equals(HeroName.GUARDIAN)) {
            return hero.getAbility(AbilityName.GUARDIAN_ATTACK);
        } else if (hero.getName().equals(HeroName.HEALER)) {
            return hero.getAbility(AbilityName.HEALER_ATTACK);
        } else {
            return hero.getAbility(AbilityName.SENTRY_RAY);
        }
    }
    private static Ability getLowerPowerFullAbility(Hero hero) {
        if (hero.getName().equals(HeroName.BLASTER)) {
            return hero.getAbility(AbilityName.BLASTER_ATTACK);
        } else if (hero.getName().equals(HeroName.GUARDIAN)) {
            return hero.getAbility(AbilityName.GUARDIAN_ATTACK);
        } else if (hero.getName().equals(HeroName.HEALER)) {
            return hero.getAbility(AbilityName.HEALER_ATTACK);
        } else {
            return hero.getAbility(AbilityName.SENTRY_ATTACK);
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
