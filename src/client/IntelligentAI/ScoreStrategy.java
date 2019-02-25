package client.IntelligentAI;

import client.model.Cell;
import client.model.Hero;
import client.model.World;

import java.util.ArrayList;

public class ScoreStrategy {
    public static Integer distanceToZone(Hero myHero, MyDirection direction, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove) {
        Integer score = 0;
        Move move = Move.findByHero(copyOfMyHeroesMove, myHero);
        Integer index = copyOfMyHeroesMove.indexOf(move);
        Cell myherocell = move.getNextCell(); //ToDo: this must be updated of my hero cell
        if (myherocell.isInObjectiveZone()) {
            score += Score.IN_ZONE;
        }
        Cell selectedObjectiveCell = move.getTargetZoneCell();
        //Todo:above code must be replace with nearest objective zone cell
        score += Score.DISTANCE_COST * (virtualWorld.manhattanDistance(myherocell, selectedObjectiveCell));
        if (!direction.equals(MyDirection.FIX)) {
            score += Score.MOVE_COST;
        }
        System.out.println("score= " + score);
        return score;
    }

    public static Integer losingHealth(){
        return 0;
    }
}
