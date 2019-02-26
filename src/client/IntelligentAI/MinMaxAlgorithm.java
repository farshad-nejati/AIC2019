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
            Move move = new Move(myHero, myHero.getCurrentCell());
            myHeroesMove.add(move);
        }
        for (Hero myHero : this.myHeroes) {
            ArrayList<Hero> otherOurHeroes = new ArrayList<>(this.myHeroes);
            otherOurHeroes.remove(myHero);
            //TODO: check call by reference
            Cell[] objCell = virtualWorld.getMap().getObjectiveZone();
            Cell targetcell = objCell[0];
//            Cell targetcell = null;
            Move move = Move.findByHero(myHeroesMove, myHero);
            Integer index = myHeroesMove.indexOf(move);
            Cell checkTargetCell = move.getCurrentCell();
//            if (checkTargetCell.isInObjectiveZone()) {
//                targetcell = checkTargetCell;
//            }
            move.setTargetZoneCell(targetcell);
            myHeroesMove.set(index, move);
            // TODO: find best object cell for this hero

            System.out.println("my hero = " + myHero.getId() + " row = " + myHero.getCurrentCell().getRow() + " column = " + myHero.getCurrentCell().getColumn() +"\n" );
            MinMaxMove minMaxMove = new MinMaxMove(myHero, otherOurHeroes, oppHeroes, virtualWorld);
            MyDirection direction = minMaxMove.getDirection(myHeroesMove);
            heroDirectionHashMap.put(myHero, direction);

            Move move2 = Move.findByHero(myHeroesMove, myHero);
            Integer index2 = myHeroesMove.indexOf(move2);
            Cell updatedCell = Utility.getCellFromDirection(myHero.getCurrentCell(), direction, virtualWorld.getMap());
            move2.setCurrentCell(updatedCell);
            myHeroesMove.set(index2, move2);

            System.out.println(" final direction= "+ direction + "\n \n \n");
            if (!direction.equals(MyDirection.FIX)) {
                virtualWorld.moveHero(myHero, Utility.castMyDirectionToDirection(direction));
            }
        }
    }

}
