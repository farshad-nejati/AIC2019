package client.IntelligentAI;

import client.NewAI.move.noneZone.RespawnObjectiveZoneCell;
import client.model.Cell;
import client.model.Hero;
import client.model.World;

import java.util.*;

class MinMaxMove {

    private Hero myHero;
    private ArrayList<Hero> otherOurHeroes;
    private ArrayList<Hero> oppHeroes;
    ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells = new ArrayList<>();

    private World virtualWorld;

    public MinMaxMove(Hero myHero, ArrayList<Hero> otherOurHeroes, ArrayList<Hero> oppHeroes, World virtualWorld, ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells) {
        this.myHero = myHero;
        this.otherOurHeroes = otherOurHeroes;
        this.oppHeroes = oppHeroes;
        this.virtualWorld = virtualWorld;
        this.respawnObjectiveZoneCells = respawnObjectiveZoneCells;
    }

    public MyDirection getDirection(ArrayList<Move> myHeroesMove) {

        ArrayList<Move> oppHeroesMove = new ArrayList<>();
        for (Hero hero : this.oppHeroes) {
            Move move = new Move(hero, hero.getCurrentCell());
            oppHeroesMove.add(move);
        }

        Hero oppHero = null;
//        if (!oppHeroes.isEmpty()) {
//            oppHero = oppHeroes.remove(0);
//        }

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

            Integer score = eval(this.myHero, direction, MyDirection.FIX, this.otherOurHeroes, oppHero, this.oppHeroes, this.virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, Integer.MAX_VALUE);
            System.out.println("direction = " + direction + " and Score= " + score + "\n");
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

    private Integer eval(Hero myHero, MyDirection mydirection, MyDirection oppHeroDirection, ArrayList<Hero> otherOurHeroes, Hero oppHero, ArrayList<Hero> oppHeroes, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove, int minScore) {
        if (oppHeroes.isEmpty()) {
            return evaluateScore(myHero, mydirection, otherOurHeroes, oppHero, oppHeroes, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, minScore);
        }

        Hero newOppHero = oppHeroes.remove(0);

//        ArrayList<MyDirection> possibleDirections = Utility.getPossibleDirections(oppHero, this.virtualWorld, oppHeroes);
//        if (!oppHeroes.isEmpty()){
        ArrayList<Hero> otherOurOppHeroes = new ArrayList<>(Arrays.asList(virtualWorld.getOppHeroes()));
        otherOurOppHeroes.removeIf(obj -> (obj.getCurrentCell().getColumn() == -1 || obj.getCurrentCell().getRow() == -1));

        ArrayList<MyDirection> possibleDirections = Utility.getPossibleDirections(newOppHero, this.virtualWorld, otherOurOppHeroes);

        for (MyDirection oppHeroDir : possibleDirections) {

            Cell heroMoveNextCell = Utility.getCellFromDirection(newOppHero.getCurrentCell(), oppHeroDir, this.virtualWorld.getMap());
            Move move = Move.findByHero(copyOfOppHeroesMove, newOppHero);
            Integer index = copyOfOppHeroesMove.indexOf(move);
            move.setNextCell(heroMoveNextCell);
            copyOfOppHeroesMove.set(index, move);

            ArrayList<Move> copyOfOppHeroesMove2 = new ArrayList<>(copyOfOppHeroesMove);

            Integer score = eval(myHero, mydirection, oppHeroDir, otherOurHeroes, newOppHero, oppHeroes, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove2, minScore);
            if (score < minScore) {
                minScore = score;
            }
        }
//        }

        return minScore;
    }

    private Integer evaluateScore(Hero myHero, MyDirection myHeroDirection, ArrayList<Hero> otherOurHeroes, Hero oppHero, ArrayList<Hero> oppHeroes, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove, int minScore) {
        // TODO: evaluateScore()
//        return new Random().nextInt(10);
        ArrayList<Cell> blocks = new ArrayList<>();
        blocks = getBlockCells(myHero);

        Integer score = 0;

        score += ScoreStrategy.distanceToZone(myHero, myHeroDirection, virtualWorld, copyOfMyHeroesMove, blocks);
        score += ScoreStrategy.otherWallCell(myHero, myHeroDirection, virtualWorld, copyOfMyHeroesMove);
//
        score += ScoreStrategy.hitByOppHeroes(myHero, myHeroDirection, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove);
//        score += ScoreStrategy.otherMyHeroCell(myHero, myHeroDirection, oppHero, virtualWorld, copyOfMyHeroesMove);
        score += ScoreStrategy.reduceDistanceWithOppHeroesInObjectiveZone(myHero, myHeroDirection, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove);
        score += ScoreStrategy.reduceDistanceToOppHeroesWithMinimumHealth(myHero, myHeroDirection, otherOurHeroes, oppHero, oppHeroes, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, blocks);

        return score;

    }

    private ArrayList<Cell> getBlockCells(Hero myHero) {
        ArrayList<Cell> blocks = new ArrayList<>();
        for (RespawnObjectiveZoneCell respawnObjectiveZoneCell : this.respawnObjectiveZoneCells) {
            if (!respawnObjectiveZoneCell.getHero().equals(myHero)) {
                blocks.add(respawnObjectiveZoneCell.getObjectiveZoneCell());
            }
        }
        return blocks;
    }
}
