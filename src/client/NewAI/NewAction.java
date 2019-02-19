package client.NewAI;

import client.RandomAI.EffectiveHero;
import client.model.Ability;
import client.model.Cell;
import client.model.Hero;
import client.model.World;

import java.util.ArrayList;
import java.util.List;

public class NewAction {

    public void Action(World world) {
        ArrayList<Hero> inVisionOppHeroes = getInVisionOppHeroes(world);
        ArrayList<Hero> activeMyHeroes = getActiveMyHeroes(world, inVisionOppHeroes);

        while (activeMyHeroes.size() != 0 ) {
            ArrayList<TakingParts> takingParts = initializeTakingParts(activeMyHeroes);
            List<OppHeroAction> oppHeroActions = new ArrayList<>();

            for (Hero oppHero: world.getOppHeroes()) {
                OppHeroAction oppHeroAction = new OppHeroAction(world, oppHero, activeMyHeroes);
                takingParts = oppHeroAction.getAllPossibleAbilities(takingParts);
                oppHeroActions.add(oppHeroAction);
            }
        }
    }

    public ArrayList<TakingParts> initializeTakingParts(ArrayList<Hero> activeMyHeroes) {
        ArrayList<TakingParts> takingParts = new ArrayList<>();
        for (Hero myHero : activeMyHeroes) {
            takingParts.add(new TakingParts(myHero, 10, 10));
        }
        return takingParts;
    }

    public ArrayList<Hero> getActiveMyHeroes(World world, ArrayList<Hero> inVisionOppHeroes) {
        ArrayList<Hero> activeMyHeroes = new ArrayList<>();
        for (Hero myHero : world.getMyHeroes()) {
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
                        if (!activeMyHeroes.contains(myHero)) {
                            activeMyHeroes.add(myHero);
                        }
                    }
                }

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

}
