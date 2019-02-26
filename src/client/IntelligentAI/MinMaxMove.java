package client.IntelligentAI;

import client.model.Cell;
import client.model.Hero;
import client.model.World;

import java.util.*;

class MinMaxMove {

    private Hero myHero;
    private ArrayList<Hero> otherOurHeroes;
    private ArrayList<Hero> oppHeroes;
    private World virtualWorld;

    public MinMaxMove(Hero myHero, ArrayList<Hero> otherOurHeroes, ArrayList<Hero> oppHeroes, World virtualWorld) {
        this.myHero = myHero;
        this.otherOurHeroes = otherOurHeroes;
        this.oppHeroes = oppHeroes;
        this.virtualWorld = virtualWorld;
    }

    public MyDirection getDirection(ArrayList<Move> myHeroesMove) {

        ArrayList<Move> oppHeroesMove = new ArrayList<>();
        for (Hero hero : this.oppHeroes) {
            Move move = new Move(hero, hero.getCurrentCell());
            oppHeroesMove.add(move);
        }

        Hero oppHero = null;
        if (!oppHeroes.isEmpty()) {
            oppHero = oppHeroes.remove(0);
        }

        ArrayList<MyDirection> possibleDirections = Utility.getPossibleDirections(this.myHero, this.virtualWorld, this.otherOurHeroes);
        HashMap<MyDirection, Integer> scoreHashMap = new HashMap<>();

        for (MyDirection direction : possibleDirections) {
            Cell heroMoveNextCell = Utility.getCellFromDirection(myHero.getCurrentCell(), direction, this.virtualWorld.getMap());
            Move move = Move.findByHero(myHeroesMove, myHero);
            Integer index = myHeroesMove.indexOf(move);
            move.setNextCell(heroMoveNextCell);
            myHeroesMove.set(index, move);

            ArrayList<Move> copyOfMyHeroesMove = new ArrayList<>(myHeroesMove);
            ArrayList<Move> copyOfOppHeroesMove = new ArrayList<>(oppHeroesMove);
            //TODO: setTarget

            scoreHashMap.put(direction, 0);

            Integer score = eval(this.myHero, MyDirection.FIX, this.otherOurHeroes, oppHero, this.oppHeroes, this.virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, Integer.MAX_VALUE);
            System.out.println("direction = " + direction + " and Score= " + score+ "\n");
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

    private Integer eval(Hero myHero, MyDirection mydirection, ArrayList<Hero> otherOurHeroes, Hero oppHero, ArrayList<Hero> oppHeroes, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove, int minScore) {
        if (oppHeroes.isEmpty()) {
            return evaluateScore(myHero, mydirection, otherOurHeroes, oppHero, oppHeroes, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, minScore);
        }

        Hero newOppHero = oppHeroes.remove(0);

//        ArrayList<MyDirection> possibleDirections = Utility.getPossibleDirections(oppHero, this.virtualWorld, oppHeroes);
//        if (!oppHeroes.isEmpty()){
            ArrayList<Hero> otherOurOppHeroes = new ArrayList<>(Arrays.asList(virtualWorld.getOppHeroes()));
            otherOurOppHeroes.removeIf(obj -> (obj.getCurrentCell().getColumn()== -1 || obj.getCurrentCell().getRow()== -1));

            ArrayList<MyDirection> possibleDirections = Utility.getPossibleDirections(oppHero, this.virtualWorld, otherOurOppHeroes);

            for (MyDirection direction : possibleDirections) {

                Cell heroMoveNextCell = Utility.getCellFromDirection(oppHero.getCurrentCell(), direction, this.virtualWorld.getMap());
                Move move = Move.findByHero(copyOfOppHeroesMove, oppHero);
                Integer index = copyOfOppHeroesMove.indexOf(move);
                move.setNextCell(heroMoveNextCell);
                copyOfOppHeroesMove.set(index, move);

                ArrayList<Move> copyOfOppHeroesMove2 = new ArrayList<>(copyOfOppHeroesMove);

                Integer score = eval(myHero, direction, otherOurHeroes, newOppHero, oppHeroes, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove2, minScore);
                if (score < minScore) {
                    minScore = score;
                }
            }
//        }

        return minScore;
    }

    private Integer evaluateScore(Hero myHero, MyDirection direction, ArrayList<Hero> otherOurHeroes, Hero oppHero, ArrayList<Hero> oppHeroes, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove, int minScore) {
        // TODO: evaluateScore()
//        return new Random().nextInt(10);

        Integer score = 0;

        score += ScoreStrategy.distanceToZone(myHero, direction, virtualWorld, copyOfMyHeroesMove);
//        score += ScoreStrategy.hitByOppHeroes(myHero,direction, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove);
        score += ScoreStrategy.otherMyHeroCell(myHero,direction, oppHero, virtualWorld, copyOfMyHeroesMove);
        score += ScoreStrategy.otherWallCell(myHero,direction,virtualWorld,copyOfMyHeroesMove);

        return score;

    }
}
