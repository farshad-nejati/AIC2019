package client.IntelligentAI;

import client.model.Cell;
import client.model.Hero;
import client.model.Map;
import client.model.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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

        Hero oppHero = oppHeroes.remove(0);
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
            Integer score = eval(this.myHero, this.otherOurHeroes, oppHero, this.oppHeroes, this.virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, Integer.MAX_VALUE);
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

    private Integer eval(Hero myHero, ArrayList<Hero> otherOurHeroes, Hero oppHero, ArrayList<Hero> oppHeroes, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove, int minScore) {
        if (oppHeroes.isEmpty()) {
            return evaluateScore();
        }

        Hero newOppHero = oppHeroes.remove(0);
        ArrayList<MyDirection> possibleDirections = Utility.getPossibleDirections(oppHero, this.virtualWorld, oppHeroes);

        for (MyDirection direction : possibleDirections) {

            Cell heroMoveNextCell = Utility.getCellFromDirection(oppHero.getCurrentCell(), direction, this.virtualWorld.getMap());
            Move move = Move.findByHero(copyOfOppHeroesMove, oppHero);
            Integer index = copyOfOppHeroesMove.indexOf(move);
            move.setNextCell(heroMoveNextCell);
            copyOfOppHeroesMove.set(index, move);

            ArrayList<Move> copyOfOppHeroesMove2 = new ArrayList<>(copyOfOppHeroesMove);

            Integer score = eval(myHero, otherOurHeroes, newOppHero, oppHeroes, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove2, minScore);
            if (score < minScore) {
                minScore = score;
            }
        }

        return minScore;
    }

    private Integer evaluateScore() {
        // TODO: evaluateScore()
        return new Random().nextInt(10);

//        Integer score = 0;
//        Cell myherocell = myHero.getCurrentCell(); //ToDo: this must be updated of my hero cell
//        if (myherocell.isInObjectiveZone()){
//            score += Score.inZone;
//        }
//        Cell selectedObjectiveCell = move.getRandomObjectiveCell(world, myHero.getCurrentCell(), new ArrayList<>());
//        //Todo:above code must be replace with nearest objective zone cell
//        score += -Score.distanceCost *(world.manhattanDistance(myherocell,selectedObjectiveCell));
//        if (!direction.equals(MyDirection.FIX)){
//            score += Score.movecost;
//        }
//        System.out.println("score= " +score);
//        return score;

    }
}
