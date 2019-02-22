package client.NewAI.move;

import client.NewAI.ActiveMyHeroes;
import client.NewAI.OppHeroAction;
import client.model.Ability;
import client.model.Hero;
import client.model.World;

import java.util.ArrayList;

public class Move {
    ArrayList<OppHeroTarget> oppHeroTargets = new ArrayList<>();
    ArrayList<Hero> notMarkedMyHeroes = new ArrayList<>();

    ArrayList<Hero> myHeroesInZone = new ArrayList<>();


    public void move(World world) {
        fillHeroInZoneLists(world);
        setMarkedAndNotMarkedOppHero(world);
        if (areAllMyHeroesInObjectiveZone(world)) {

        } else {

        }
    }

    private void fillHeroInZoneLists(World world) {
    }

    private boolean areAllMyHeroesInObjectiveZone(World world) {
        for (Hero myHero : world.getMyHeroes()) {
            if (myHero.getCurrentCell().isInObjectiveZone()) {

            }
        }
        return true;
    }


    private void setMarkedAndNotMarkedOppHero(World world) {
        Hero[] oppHeroes = world.getOppHeroes();
        Hero[] myHeroes = world.getMyHeroes();

        for (Hero oppHero: oppHeroes) {
            ArrayList<Hero> targetMyHeroes = new ArrayList<>();
            for (Hero myHero : myHeroes) {
                Ability[] oopHeroAbilities = oppHero.getOffensiveAbilities();

                boolean flag = false;
                for (Ability ability : oopHeroAbilities) {
                    if (!ability.isLobbing()) {
                        if (!world.isInVision(oppHero.getCurrentCell(), myHero.getCurrentCell()))
                            continue;
                    }
                    if (!ability.isReady())
                        continue;

                    int range = ability.getRange() + ability.getAreaOfEffect();
                    int distance = world.manhattanDistance(oppHero.getCurrentCell(), myHero.getCurrentCell());
                    if (distance <= range) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    targetMyHeroes.add(myHero);
                    if (notMarkedMyHeroes.contains(myHero)) {
                        notMarkedMyHeroes.remove(myHero);
                    }
                } else {
                    if (notMarkedMyHeroes.contains(myHero)) {
                        notMarkedMyHeroes.add(myHero);
                    }
                }
            }
            if (targetMyHeroes.size() > 0) {
                oppHeroTargets.add(new OppHeroTarget(oppHero, targetMyHeroes));
            }
        }
    }
}
