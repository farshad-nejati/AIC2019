package client.IntelligentAI;

import client.model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ScoreStrategy {
    public static Integer distanceToZone(Hero myHero, MyDirection direction, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove) {
        Integer score = 0;
        Move move = Move.findByHero(copyOfMyHeroesMove, myHero);
        Integer index = copyOfMyHeroesMove.indexOf(move);
        Cell myherocell = move.getNextCell(); //TODO: this must be updated of my hero cell
        if (myherocell.isInObjectiveZone()) {
            score += Score.IN_ZONE;
        }
        if (move.getTargetZoneCell() != null) {
            Cell selectedObjectiveCell = move.getTargetZoneCell();
            //Todo:above code must be replace with nearest objective zone cell
            Direction[] distancepath = virtualWorld.getPathMoveDirections(myherocell, selectedObjectiveCell);
            //TODO: find distacePath based on block cell
            score += Score.DISTANCE_COST * (distancepath.length);
        }
        if (!direction.equals(MyDirection.FIX)) {
            score += Score.MOVE_COST;
        }
        System.out.println("score= " + score);
        return score;
    }

    public static Integer otherMyHeroCell(Hero myHero, MyDirection direction, Hero oppHero, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove) {
        Integer score = 0;
        Cell myHeroCurrentCell = null;
        Cell myHeroNextCell = null;
        for (Move myHeroMove : copyOfMyHeroesMove) {
            if (myHeroMove.getHero().equals(myHero)) {
                myHeroCurrentCell = myHeroMove.getCurrentCell();
                myHeroNextCell = myHeroMove.getNextCell();
                break;
            }
        }
        if (virtualWorld.getCurrentTurn() == 10) {
            int p = 0;
        }
        boolean flag = false;
        for (Move myOtherHeroMove : copyOfMyHeroesMove) {
            boolean checkMyHero = myOtherHeroMove.getHero().equals(myHero);
            boolean checkTwoHero = myOtherHeroMove.getCurrentCell().equals(myHeroNextCell);
            if (checkTwoHero && !(checkMyHero)) {
                score += Score.MY_HERO_CELL;
                flag = true;
//                if (myHero.getCurrentCell().equals(myHeroNextCell)){
//                    score += Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST;
//                }
                break;
            }
        }

        HashMap<Cell, Direction> cellNeighbors = Utility.getCellNeighbors(myHeroCurrentCell, virtualWorld.getMap());
        MyDirection directionCheck = Utility.getDirectionFromCells(myHeroCurrentCell, myHeroNextCell);


        for (Cell cellNeighbor : cellNeighbors.keySet()) {

            if (!copyOfMyHeroesMove.isEmpty()) {
                Integer i = 0;
                HashMap<Cell, Direction> cellNeighborsNeighbor = Utility.getCellNeighbors(cellNeighbor, virtualWorld.getMap());
                for (Cell cellNeighborNeighbor : cellNeighborsNeighbor.keySet()) {
                    for (Move heroMoveneighbor : copyOfMyHeroesMove) {
                        Hero otherOurHeroneighbor = heroMoveneighbor.getHero();
                        if (otherOurHeroneighbor.getCurrentCell().equals(cellNeighborNeighbor)) {
                            i++;
                        }
                    }
                }
                score += (i * Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST_IN_ZONE);

                if (cellNeighbor.isInObjectiveZone()) {
                    score += Score.IN_ZONE;
                }

                if (directionCheck.equals(MyDirection.FIX)) {
                    score += Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST_IN_ZONE;
                }


//                    if (otherOurHero.getCurrentCell().equals(cellNeighbor)) {
//                        Integer j = 0;
//                        for (Move ourheroMove: copyOfMyHeroesMove){
//                            Hero ourhero = ourheroMove.getHero();
//                            if (!ourhero.equals(myHero) && ourhero.getCurrentCell().equals(myHeroNextCell)){
//                                j++;
//                            }
//                        }
//                            if (myHeroCurrentCell.isInObjectiveZone()){
//                                score += (i * Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST_IN_ZONE);
//
//                            }else {
//                                score += Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST;
//
//                            }
//                    }

            }

        }


//        if (flag && directionCheck.equals(MyDirection.FIX)){
//            score += Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST;
//        }
        System.out.println("other hero cell score= " + score);
        return score;
    }

    public static Integer otherWallCell(Hero myHero, MyDirection direction, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove) {
        Integer score = 0;
        Cell myHeroNextCell = null;
        for (Move myHeroMove : copyOfMyHeroesMove) {
            if (myHeroMove.getHero().equals(myHero)) {
                myHeroNextCell = myHeroMove.getNextCell();
            }
        }
        if (myHeroNextCell.isWall()) {
            score += Score.WALL_SCORE;
        }
        System.out.println("wall cell score= " + score);

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
