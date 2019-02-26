package client.NewAI.action;

import client.model.Hero;

import java.util.ArrayList;

public class ActiveMyHeroes {
    private Hero myHero;
    private ArrayList<Hero> possibleOppHeroes;

    public ActiveMyHeroes(Hero myHero, ArrayList<Hero> possibleOppHeroes) {
        this.myHero = myHero;
        this.possibleOppHeroes = possibleOppHeroes;
    }

    public Hero getMyHero() {
        return myHero;
    }

    public void setMyHero(Hero myHero) {
        this.myHero = myHero;
    }

    public ArrayList<Hero> getPossibleOppHeroes() {
        return possibleOppHeroes;
    }

    public void setPossibleOppHeroes(ArrayList<Hero> possibleOppHeroes) {
        this.possibleOppHeroes = possibleOppHeroes;
    }
}
