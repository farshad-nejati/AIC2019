package client.IntelligentAI;

import client.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MinMaxAlgorithm {
    private HashMap<Hero, MyDirection> heroDirectionHashMap = new HashMap<>();
    private ArrayList<Hero> myHeroes;
    private ArrayList<Hero> oppHeroes; // in vision opp heroes in world
    private Map virtualMap; // update by assumptive my hero directions

    public MinMaxAlgorithm(ArrayList<Hero> myHeroes, ArrayList<Hero> oppHeroes, Map virtualMap) {
        this.myHeroes = myHeroes;
        this.oppHeroes = oppHeroes;
        this.virtualMap = virtualMap;
    }

    public HashMap<Hero, MyDirection> getHeroDirectionHashMap() {
        return heroDirectionHashMap;
    }

    public void maxMove() {
        for (Hero myHero : this.myHeroes) {
            ArrayList<Hero> otherOurHeroes = new ArrayList<>(this.myHeroes);
            otherOurHeroes.remove(myHero);
            MinMaxMove minMaxMove = new MinMaxMove(myHero, otherOurHeroes, oppHeroes, virtualMap);
            MyDirection direction = minMaxMove.getDirection();
            heroDirectionHashMap.put(myHero, direction);
            // TODO: updateVirtualMapByHeroMove()
        }
    }

}
