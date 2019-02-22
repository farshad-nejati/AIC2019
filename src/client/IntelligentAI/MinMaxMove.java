package client.IntelligentAI;

import client.model.Hero;
import client.model.Map;

import java.util.ArrayList;
import java.util.HashMap;

class MinMaxMove {

    private Hero myHero;
    private ArrayList<Hero> otherOurHeroes;
    private ArrayList<Hero> oppHeroes;
    private Map virtualMap;

    public MinMaxMove(Hero myHero, ArrayList<Hero> otherOurHeroes, ArrayList<Hero> oppHeroes, Map virtualMap) {
        this.myHero = myHero;
        this.otherOurHeroes = otherOurHeroes;
        this.oppHeroes = oppHeroes;
        this.virtualMap = virtualMap;
    }

    public MyDirection getDirection() {
        Hero oppHero = oppHeroes.remove(0);
        ArrayList<MyDirection> possibleDirections = Utility.getPossibleDirections(this.myHero, this.virtualMap, this.otherOurHeroes);
        HashMap<MyDirection, Integer> scoreHashMap = new HashMap<>();

        for (MyDirection direction : possibleDirections) {
            scoreHashMap.put(direction, 0);
            Integer score = eval(this.myHero, this.otherOurHeroes, oppHero, this.oppHeroes, this.virtualMap, Integer.MAX_VALUE);
            scoreHashMap.put(direction, score);
        }

        HashMap.Entry<MyDirection, Integer> maxEntry = null;

        for (HashMap.Entry<MyDirection, Integer> entry : scoreHashMap.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        return maxEntry != null ? maxEntry.getKey() : null;
    }

    private Integer eval(Hero myHero, ArrayList<Hero> otherOurHeroes, Hero oppHero, ArrayList<Hero> oppHeroes, Map virtualMap, int minScore) {
        if (oppHeroes.isEmpty()) {
            return evaluateScore();
        }

        Hero newOppHero = oppHeroes.remove(0);
        ArrayList<MyDirection> possibleDirections = Utility.getPossibleDirections(oppHero, this.virtualMap, oppHeroes);

        for (MyDirection direction : possibleDirections) {
            // TODO: updateVirtualMapByHeroMove()
            Integer score = eval(myHero, otherOurHeroes, newOppHero, oppHeroes, virtualMap, minScore);
            if (score < minScore) {
                minScore = score;
            }
        }

        return minScore;
    }

    private Integer evaluateScore() {
        // TODO: evaluateScore()
        return 0;
    }
}
