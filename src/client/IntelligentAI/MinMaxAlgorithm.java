package client.IntelligentAI;

import client.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MinMaxAlgorithm {
    private ArrayList<Move> myHeroesMove = new ArrayList<>();
    private HashMap<Hero, MyDirection> heroDirectionHashMap = new HashMap<>();
    private ArrayList<Hero> myHeroes;
    private ArrayList<Hero> oppHeroes; // in vision opp heroes in world
    private World virtualWorld; // update by assumptive my hero directions

    public MinMaxAlgorithm(ArrayList<Hero> myHeroes, ArrayList<Hero> oppHeroes, World virtualWorld) {
        this.myHeroes = myHeroes;
        this.oppHeroes = oppHeroes;
        this.virtualWorld = virtualWorld;
    }

    public HashMap<Hero, MyDirection> getHeroDirectionHashMap() {
        return heroDirectionHashMap;
    }

    public void maxMove() {
        for (Hero myHero : this.myHeroes) {
            Move move = new Move(myHero,myHero.getCurrentCell());
            myHeroesMove.add(move);
        }
        for (Hero myHero : this.myHeroes) {
            ArrayList<Hero> otherOurHeroes = new ArrayList<>(this.myHeroes);
            otherOurHeroes.remove(myHero);
            //TODO: check call by reference
            MinMaxMove minMaxMove = new MinMaxMove(myHero, otherOurHeroes, oppHeroes, virtualWorld);
            MyDirection direction = minMaxMove.getDirection(myHeroesMove);
            heroDirectionHashMap.put(myHero, direction);
        }
    }

}
