package client.IntelligentAI;

import client.NewAI.move.noneZone.RespawnObjectiveZoneCell;
import client.model.*;

import java.util.*;

public class MinMaxAlgorithm {
    private ArrayList<Move> myHeroesMove = new ArrayList<>();
    private HashMap<Hero, MyDirection> heroDirectionHashMap = new HashMap<>();
    private ArrayList<Hero> myHeroes;
    private ArrayList<Hero> oppHeroes; // in vision opp heroes in world
    private ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells;
    private World virtualWorld; // update by assumptive my hero directions
    private HashMap<Hero, Boolean> heroHashArrival = new HashMap<>();

    public MinMaxAlgorithm(ArrayList<Hero> myHeroes, ArrayList<Hero> oppHeroes, ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells, World virtualWorld, HashMap<Hero, Boolean> heroHashArrival) {
        this.myHeroes = myHeroes;
        this.oppHeroes = oppHeroes;
        this.respawnObjectiveZoneCells = respawnObjectiveZoneCells;
        this.virtualWorld = virtualWorld;
        this.heroHashArrival = heroHashArrival;

    }

    public HashMap<Hero, MyDirection> getHeroDirectionHashMap() {
        return heroDirectionHashMap;
    }

    public void maxMove() {

        this.myHeroes.sort((o1, o2) -> {
            if (o1.getCurrentHP() == o2.getCurrentHP())
                return 0;
            return o1.getCurrentHP() < o2.getCurrentHP() ? -1 : 1;
        });

        for (Hero myHero : this.myHeroes) {
            Move move = new Move(myHero, myHero.getCurrentCell());
            myHeroesMove.add(move);
        }
        for (Hero myHero : this.myHeroes) {
            ArrayList<Hero> otherOurHeroes = new ArrayList<>(this.myHeroes);
            otherOurHeroes.remove(myHero);
            //TODO: check call by reference
//            Cell[] objCell = virtualWorld.getMap().getObjectiveZone();
            Cell targetcell = Utility.getMyHeroTargetCell(myHero, this.respawnObjectiveZoneCells, virtualWorld, this.heroHashArrival);
//            Cell targetcell = null;
            Move move = Move.findByHero(myHeroesMove, myHero);
            Integer index = myHeroesMove.indexOf(move);
//            Cell checkTargetCell = move.getCurrentCell();
//            if (checkTargetCell.isInObjectiveZone()) {
//                targetcell = checkTargetCell;
//            }

            move.setTargetZoneCell(targetcell);
            myHeroesMove.set(index, move);
            // TODO: find best object cell for this hero

//            System.out.println("my hero = " + myHero.getId() + " row = " + myHero.getCurrentCell().getRow() + " column = " + myHero.getCurrentCell().getColumn() + "\n" + "target Cell = "+ targetcell.getRow() + " " + targetcell.getColumn());
            MinMaxMove minMaxMove = new MinMaxMove(myHero, otherOurHeroes, oppHeroes, virtualWorld, this.respawnObjectiveZoneCells);
            MyDirection direction = minMaxMove.getDirection(myHeroesMove);
            heroDirectionHashMap.put(myHero, direction);
            Move move2 = Move.findByHero(myHeroesMove, myHero);
            Integer index2 = myHeroesMove.indexOf(move2);
            Cell updatedCell = Utility.getCellFromDirection(myHero.getCurrentCell(), direction, virtualWorld.getMap());
            move2.setBeforeCell(move2.getCurrentCell());
            move2.setCurrentCell(updatedCell);

            myHeroesMove.set(index2, move2);

            System.out.println(" final direction= " + direction + "\n \n \n");
            if (!direction.equals(MyDirection.FIX)) {
                virtualWorld.moveHero(myHero, Utility.castMyDirectionToDirection(direction));
            }
        }
    }

}
