package client.IntelligentAI;

import client.model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ScoreStrategy {
    public static Integer distanceToZone(Hero myHero, MyDirection direction, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove) {
        Integer score = 0;
        Move move = Move.findByHero(copyOfMyHeroesMove, myHero);
        Integer index = copyOfMyHeroesMove.indexOf(move);
        Cell myherocell = move.getNextCell(); //TODO: this must be updated of my hero cell
        if (myherocell.isInObjectiveZone()) {
            score += Score.IN_ZONE;
        }
        Cell selectedObjectiveCell = move.getTargetZoneCell();
        //Todo:above code must be replace with nearest objective zone cell
        Direction[] distancepath = virtualWorld.getPathMoveDirections(myherocell, selectedObjectiveCell);
        score += Score.DISTANCE_COST * (distancepath.length);
        if (!direction.equals(MyDirection.FIX)) {
            score += Score.MOVE_COST;
        }
        System.out.println("score= " + score);
        return score;
    }

    public static Integer losingHealth(Hero myHero, Hero oppHero, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove) {

        Integer score = 0;

        Move move = Move.findByHero(copyOfMyHeroesMove, myHero);
        Integer index = copyOfMyHeroesMove.indexOf(move);
        Cell myHeroCell = move.getNextCell(); //TODO: this must be updated of my hero cell

        for (Move oppHeroMove : copyOfOppHeroesMove) {
            Hero hero = oppHeroMove.getHero();
            Cell oppHeroCell = oppHeroMove.getNextCell();
            if (oppHeroCell.isInVision()) {
                Ability maxLosingHealthAbility = null;
                int maxLosingHealth = 0;
                for (Ability ability : oppHero.getOffensiveAbilities()) {
                    if (ability.getPower() > maxLosingHealth) {
                        maxLosingHealthAbility = ability;
                        maxLosingHealth = ability.getPower();
                    }
                }
                int distance = virtualWorld.manhattanDistance(myHeroCell, oppHeroCell);
                if (maxLosingHealthAbility != null && (distance < (maxLosingHealthAbility.getRange() + maxLosingHealthAbility.getAreaOfEffect()))) {
                    score += maxLosingHealth;
                }
            }
        }

        if (score > myHero.getCurrentHP()) {
            score = Score.KILL_COST;
        } else {
            score = Score.HEALTH_COST * score;
        }

        return score;
    }
}