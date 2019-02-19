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
