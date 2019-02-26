package client.NewAI.move;

import client.model.Hero;

import java.time.chrono.HijrahEra;
import java.util.ArrayList;

public class OppHeroTarget {
    private Hero oppHEro;
    private ArrayList<Hero> myHeroes;

    public OppHeroTarget(Hero oppHEro, ArrayList<Hero> myHeroes) {
        this.oppHEro = oppHEro;
        this.myHeroes = myHeroes;
    }

    public Hero getOppHEro() {

        return oppHEro;
    }

    public void setOppHEro(Hero oppHEro) {
        this.oppHEro = oppHEro;
    }

    public ArrayList<Hero> getMyHeroes() {
        return myHeroes;
    }

    public void setMyHeroes(ArrayList<Hero> myHeroes) {
        this.myHeroes = myHeroes;
    }
}
