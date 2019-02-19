package client.NewAI;

import client.model.Ability;
import client.model.Hero;

import java.util.ArrayList;

public class HeroPossibleAbilities {
    private Hero hero;
    private ArrayList<Ability> abilities;

    public HeroPossibleAbilities(Hero hero, ArrayList<Ability> abilities) {
        this.hero = hero;
        this.abilities = abilities;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<Ability> abilities) {
        this.abilities = abilities;
    }
}
