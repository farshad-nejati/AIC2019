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

    public static Integer hitByOppHeroes(Hero myHero, MyDirection direction, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove) {

        Integer losingHealthSum = 0;
        Integer killDistanceSum = 0;
        Integer canHitSum = 0;

        Integer losingHealthScore = 0;
        Integer killDistanceScore = 0;
        Integer canHitScore = 0;

        Move move = Move.findByHero(copyOfMyHeroesMove, myHero);
        Cell myHeroCell = move.getNextCell(); //TODO: this must be updated of my hero cell

        for (Move oppHeroMove : copyOfOppHeroesMove) {
            Hero hero = oppHeroMove.getHero();
            Cell oppHeroCell = oppHeroMove.getNextCell();
            if (oppHeroCell.isInVision()) {
                Ability maxLosingHealthAbility = null;
                int maxLosingHealth = 0;
                for (Ability ability : hero.getOffensiveAbilities()) {
                    if (maxLosingHealthAbility == null) {
                        maxLosingHealthAbility = ability;
                    }
                    int distance = virtualWorld.manhattanDistance(myHeroCell, oppHeroCell);

                    boolean canHit = distance < (maxLosingHealthAbility.getRange() + maxLosingHealthAbility.getAreaOfEffect());

                    if (canHit) {
                        canHitSum++;
                        if (ability.getPower() > maxLosingHealth) {
                            killDistanceSum += distance;
                            maxLosingHealthAbility = ability;
                            maxLosingHealth = ability.getPower();
                        }
                    }
                }
                if (maxLosingHealthAbility != null) {
                    losingHealthSum += maxLosingHealth;
                }
            }
        }

        canHitScore = canHitSum * Score.CAN_HIT_COST;

        if (losingHealthSum > myHero.getCurrentHP()) {
            losingHealthScore = Score.KILL_COST;
            if (direction.equals(MyDirection.FIX)) {
                losingHealthScore += 2 * Score.MOVE_COST;
            }
            killDistanceScore = killDistanceSum * Score.KILL_DISTANCE_COST;
        } else {
            losingHealthScore = Score.HEALTH_COST * losingHealthSum;
        }

        return losingHealthScore + killDistanceScore + canHitScore;
    }
}
