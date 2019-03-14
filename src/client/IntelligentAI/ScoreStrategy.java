package client.IntelligentAI;

import client.model.*;

import java.lang.Math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ScoreStrategy {
    public static Integer distanceToZone(Hero myHero, MyDirection direction, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Cell> blockCells) {
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

            Direction[] distancepath = virtualWorld.getPathMoveDirections(myherocell, selectedObjectiveCell, blockCells);
            //TODO: find distacePath based on block cell
            Integer distanceLenghtCost = Score.DISTANCE_COST * (distancepath.length);
            score += distanceLenghtCost;
//            System.out.println("distanceLenghtCost = " + distanceLenghtCost);
        }
        if (!direction.equals(MyDirection.FIX)) {
            score += Score.MOVE_COST;
        }
//        System.out.println("distance zone Score= " + score);
        return score;
    }

    public static Integer otherMyHeroCell(Hero myHero, MyDirection direction, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove, Integer oppHeroInObjZone, Integer oppHeroMaxAreaEffect, ArrayList<Move> myHeroesMoves, ArrayList<Cell> blocks) {
        Integer score = 0;

        Cell myHeroCurrentCell = null;
        Cell myHeroNextCell = null;
        Move myMove = null;
        Integer distanceToOtherMyhero = 0;
        for (Move myMoveHero : copyOfMyHeroesMove) {
            Hero hero = myMoveHero.getHero();
            if (hero.equals(myHero)) {
                myHeroCurrentCell = myMoveHero.getCurrentCell();
                myHeroNextCell = myMoveHero.getNextCell();
                myMove = myMoveHero;
                break;
            }
        }
        Integer copyOppHeroMaxAreaEffect = oppHeroMaxAreaEffect;
        if (copyOppHeroMaxAreaEffect < 3) {
            copyOppHeroMaxAreaEffect = 3;
        }//TODO: target set konam

        Integer myAbilityRangeAndEffect = 0;
        for (Ability myability : myHero.getOffensiveAbilities()) {
            if (virtualWorld.getCurrentTurn() >= 24) {
                int p = 0;
            }
            if (myability.getRemCooldown() == 0 && (myability.getRange() + myability.getAreaOfEffect()) > myAbilityRangeAndEffect) {
//                myAbilityRangeAndEffect = ability.getRange() + ability.getAreaOfEffect();
                myAbilityRangeAndEffect = myability.getRange();
            }
        }

        Hero oppHero = null;
//        Cell oppHeroCell = null;
        Integer oppHeroCellDistance = 31;
//        for (Move moveOppHero : copyOfOppHeroesMove) {
//            Hero Hero = moveOppHero.getHero();
//            if (Hero.getCurrentCell().isInObjectiveZone()) {
//                Integer distance = virtualWorld.manhattanDistance(oppHeroCell, myHeroCurrentCell);
//                if (oppHeroCell == null) {
//                    oppHeroCell = Hero.getCurrentCell();
//                    oppHeroCellDistance = distance;
//                    oppHero=Hero;
//                    break; //TODO: first opphero seen and choose for attack
//                } else if (distance < oppHeroCellDistance) {
//                    oppHeroCellDistance = distance;
//                    oppHero = Hero;
//                }
//            }
//        }


        for (Move moveOppHero : copyOfOppHeroesMove) {

            Hero Hero = moveOppHero.getHero();
            Cell oppHeroCell = Hero.getCurrentCell();

            boolean findFlag = false;
            ArrayList<Cell> condidateObjCells = new ArrayList<>(); //(Arrays.asList(virtualWorld.getMap().getObjectiveZone()));
            if (Hero.getCurrentCell().isInObjectiveZone()) {
//                ArrayList<Cell> condidateObjCells = new ArrayList<>(); //(Arrays.asList(virtualWorld.getMap().getObjectiveZone()));
//                if (oppHeroCellDistance != 31) {
                int row = oppHeroCell.getRow();
                int column = oppHeroCell.getColumn();
                for (int i = (row - myAbilityRangeAndEffect); i <= (row + myAbilityRangeAndEffect); i++) {
                    for (int j = (column - myAbilityRangeAndEffect); j <= (column + myAbilityRangeAndEffect); j++) {
                        if (virtualWorld.getMap().isInMap(i, j)) {

                            Cell mapCell = virtualWorld.getMap().getCell(i, j);
                            if (mapCell.isInObjectiveZone() && (!mapCell.isWall())) { //TODO: can added out of obj zone
                                Integer manhatanDis = virtualWorld.manhattanDistance(oppHeroCell, mapCell);
                                if (manhatanDis <= myAbilityRangeAndEffect) {
                                    condidateObjCells.add(mapCell);
                                }
                            }
                        }

                    }
                }
//                for (Move myHeromove : copyOfMyHeroesMove) {
//                    if (!myHeromove.getHero().equals(myHero)) {
//                        if (condidateObjCells.contains(myHeromove.getCurrentCell())) {
//                            condidateObjCells.remove(myHeromove.getCurrentCell());
//                        }
//                    }
//                }
//                }

                Integer oppAbilityRangeEffect = 0;
                oppAbilityRangeEffect = 2 * moveOppHero.getAbility().getAreaOfEffect() + 1;
                if (oppAbilityRangeEffect <= 3) {
//                    oppAbilityRangeEffect = 4;
                }
//                for (Ability ability : Hero.getOffensiveAbilities()) {
//                    if (ability.isReady()) {
//                        if (ability.getAreaOfEffect() > oppAbilityRangeEffect)
//                            oppAbilityRangeEffect = 2 * ability.getAreaOfEffect() + 1;
//                    }
//                }


//                if (oppAbilityRangeEffect == 0) {
//                    oppAbilityRangeEffect = 3;
//                }



                ArrayList<Cell> condidateObjCellstest = new ArrayList<>(condidateObjCells);
                for (Cell condidateCell : condidateObjCellstest) {
                    for (Move myHeroMove : copyOfMyHeroesMove) {
                        Hero myOtherHero = myHeroMove.getHero();
                        if (!myOtherHero.equals(myHero)) {
                            Cell myOtherHeroCell = myOtherHero.getCurrentCell();

                            if (myHeroMove.getTargetZoneCell()== null){
                                int myotherRow = myOtherHeroCell.getRow();
                                int myotherColumn = myOtherHeroCell.getColumn();
                                if ((myotherRow == condidateCell.getRow()) && (myotherColumn == condidateCell.getColumn())) {
                                    for (int i = myotherRow - oppAbilityRangeEffect; i <= myotherRow + oppAbilityRangeEffect; i++) {
                                        for (int j = myotherColumn - oppAbilityRangeEffect; j <= myotherColumn + oppAbilityRangeEffect; j++) {
                                            Cell mapCell = virtualWorld.getMap().getCell(i, j);
                                            Integer manhatanDis = virtualWorld.manhattanDistance(myOtherHeroCell, mapCell);
                                            if (manhatanDis <= oppAbilityRangeEffect) {
                                                if (condidateObjCells.contains(mapCell)) {
                                                    condidateObjCells.remove(mapCell);
                                                    if (myHeroCurrentCell.getRow() == 19 && myHeroCurrentCell.getColumn() ==14){
                                                        if (virtualWorld.getCurrentTurn()>= 24){
                                                            int ip = 0;
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }else{
                                int myotherRow = myHeroMove.getTargetZoneCell().getRow();
                                int myotherColumn = myHeroMove.getTargetZoneCell().getColumn();
                                if ((myotherRow == condidateCell.getRow()) && (myotherColumn == condidateCell.getColumn())) {
                                    for (int i = myotherRow - oppAbilityRangeEffect; i <= myotherRow + oppAbilityRangeEffect; i++) {
                                        for (int j = myotherColumn - oppAbilityRangeEffect; j <= myotherColumn + oppAbilityRangeEffect; j++) {
                                            Cell mapCell = virtualWorld.getMap().getCell(i, j);
                                            Integer manhatanDis = virtualWorld.manhattanDistance(myHeroMove.getTargetZoneCell(), mapCell);
                                            if (manhatanDis <= oppAbilityRangeEffect) {
                                                if (condidateObjCells.contains(mapCell)) {
                                                    condidateObjCells.remove(mapCell);
                                                    if (myHeroCurrentCell.getRow() == 19 && myHeroCurrentCell.getColumn() ==14){
                                                        if (virtualWorld.getCurrentTurn()>= 24){
                                                            int ip = 0;
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }

//                            int myotherRow = myOtherHeroCell.getRow();
//                            int myotherColumn = myOtherHeroCell.getColumn();
//                            if ((myotherRow == condidateCell.getRow()) && (myotherColumn == condidateCell.getColumn())) {
//                                for (int i = myotherRow - oppAbilityRangeEffect; i <= myotherRow + oppAbilityRangeEffect; i++) {
//                                    for (int j = myotherColumn - oppAbilityRangeEffect; j <= myotherColumn + oppAbilityRangeEffect; j++) {
//                                        Cell mapCell = virtualWorld.getMap().getCell(i, j);
//                                        Integer manhatanDis = virtualWorld.manhattanDistance(myOtherHeroCell, mapCell);
//                                        if (manhatanDis <= oppAbilityRangeEffect) {
//                                            if (condidateObjCells.contains(mapCell)) {
//                                                condidateObjCells.remove(mapCell);
//                                                if (myHeroCurrentCell.getRow() == 19 && myHeroCurrentCell.getColumn() ==14){
//                                                    if (virtualWorld.getCurrentTurn()>= 24){
//                                                        int ip = 0;
//                                                    }
//                                                }
//                                            }
//
//                                        }
//                                    }
//                                }
//                            }

//                            Utility.getAroundHitCells(myHeroCurrentCell,myOtherHeroCell, condidateCell, oppAbilityRangeEffect, condidateObjCells, virtualWorld);


//                            Cell heroCell = myOtherHero.getCurrentCell();
//                            for (int i = (heroCell.getRow() - oppAbilityRangeEffect); i <= (heroCell.getRow() + oppAbilityRangeEffect); i++) {
//                                for (int j = (heroCell.getColumn() - oppAbilityRangeEffect); j <= (heroCell.getColumn() + oppAbilityRangeEffect); j++) {
//                                    if (virtualWorld.getMap().isInMap(i, j)) {
//                                        Cell mapCell = virtualWorld.getMap().getCell(i, j);
//                                        Integer manhatanDis = virtualWorld.manhattanDistance(myOtherHeroCell, mapCell);
//                                        if (manhatanDis <= oppAbilityRangeEffect) {
//                                            if (condidateObjCells.contains(mapCell)) {
//                                                condidateObjCells.remove(mapCell);
//                                            }
//                                        }
//                                    }
//
//                                }
//                            }
                        }
                    }
                }


//                condidateObjCells = condidateObjCellstest;

//                for (Move myHeroMove : copyOfMyHeroesMove) {
//                    Hero myOtherHero = myHeroMove.getHero();
//                    if (!myOtherHero.equals(myHero)) {
//                        Integer myOtherAbilityRange = 0;
//                        for (Ability ability : myOtherHero.getOffensiveAbilities()) {
//                            if (ability.isReady() && ability.getRange() > myOtherAbilityRange) {
//                                myOtherAbilityRange = ability.getRange();
//                            }
//                        }
//                        Cell myOtherCell = myOtherHero.getCurrentCell();
//                        boolean canHit = false;
//                        for (int i = (myOtherCell.getRow() - myOtherAbilityRange); i <= (myOtherCell.getRow() + myOtherAbilityRange); i++) {
//                            for (int j = (myOtherCell.getColumn() - myOtherAbilityRange); j <= (myOtherCell.getColumn() + myOtherAbilityRange); j++) {
//                                Cell mapCell = virtualWorld.getMap().getCell(i, j);
//                                Integer manhatanDis = virtualWorld.manhattanDistance(myOtherCell, mapCell);
//                                if (manhatanDis <= myOtherAbilityRange) {
//                                    for (Move ooppHeroMovee : copyOfOppHeroesMove) {
//                                        Hero oppher = ooppHeroMovee.getHero();
//                                        int oppRow = oppher.getCurrentCell().getRow();
//                                        int oppColumn = oppher.getCurrentCell().getColumn();
//                                        if ((i == oppRow) && (j == oppColumn)) {
//                                            canHit = true;
//                                            break;
//                                        }
//                                    }
//                                }
//
//                            }
//                        }
//                        if (canHit) {
//                            Cell heroCell = myOtherHero.getCurrentCell();
//                            for (int i = (heroCell.getRow() - oppAbilityRangeEffect); i <= (heroCell.getRow() + oppAbilityRangeEffect); i++) {
//                                for (int j = (heroCell.getColumn() - oppAbilityRangeEffect); j <= (heroCell.getColumn() + oppAbilityRangeEffect); j++) {
//                                    if (virtualWorld.getMap().isInMap(i, j)) {
//                                        Cell mapCell = virtualWorld.getMap().getCell(i, j);
//                                        if (condidateObjCells.contains(mapCell)) {
//                                            condidateObjCells.remove(mapCell);
//                                        }
//                                    }
//
//                                }
//                            }
//
//                        }
//                    }
//                }
            }
            if (condidateObjCells.size() != 0) {


                Cell targetcell = null;
                boolean flagzonecondid = false;
                ArrayList<BestTargetCell> bestTargetCells = new ArrayList<>();
                for (Cell condidCell : condidateObjCells) {
//                    if (condidCell.isInObjectiveZone()) {
//                        targetcell = condidCell;
//                        flagzonecondid = true;
//                        break;
//                    }
                    Integer distance = Utility.distanceNUmber(myHeroCurrentCell, targetcell, virtualWorld);
                    Integer theartNum = Utility.threatNumber(myHeroCurrentCell, copyOfOppHeroesMove, targetcell, virtualWorld);
                    BestTargetCell targetCellforBest = new BestTargetCell(condidCell, theartNum, distance);
                    bestTargetCells.add(targetCellforBest);

                }
                bestTargetCells.sort((o1, o2) -> {
                    if (o1.getThreat() == o2.getThreat())
                        return 0;
                    return o1.getThreat() < o2.getThreat() ? -1 : 1;
                });
                if (!flagzonecondid) {
//                    targetcell = condidateObjCells.get(0);
                }
                targetcell = condidateObjCells.get(0);

//                Direction[] pathToObj = virtualWorld.getPathMoveDirections(myHeroCurrentCell, targetcell);
                Direction[] pathToObj = virtualWorld.getPathMoveDirections(myHeroCurrentCell, targetcell, blocks);
                Direction dir = null;
                MyDirection mydir = null;
                if (pathToObj.length != 0) {

                    dir = pathToObj[0];
                    mydir = Utility.castDirectionToMyDirection(dir);
                }
                MyDirection myDirection = Utility.getDirectionFromCells(myHeroCurrentCell, myHeroNextCell);
                if (myDirection.equals(mydir)) {
//                    score += 1200000;
                }
                Move move = Move.findByHero(myHeroesMoves, myHero);
                Integer index = myHeroesMoves.indexOf(move);
                move.setTargetZoneCell(targetcell);
                myHeroesMoves.set(index, move);
                findFlag = true;
            }
            if (findFlag) {
                break;
            }
        }


//        for (Move myHeroMove : copyOfMyHeroesMove) {
//            Hero hero = myHeroMove.getHero();
//            if (!hero.equals(myHero)) {
//                Integer manhatanDistanceFromMyHeroes = 0;
//                manhatanDistanceFromMyHeroes += virtualWorld.manhattanDistance(myHeroNextCell, hero.getCurrentCell());
//                Integer diffFromAreaEffectOFMyHero = Math.abs(manhatanDistanceFromMyHeroes - copyOppHeroMaxAreaEffect);
//                distanceToOtherMyhero += diffFromAreaEffectOFMyHero;
//            }
//        }
//        score += distanceToOtherMyhero * Score.DISTANCE_TO_My_HEROES;


//        if (virtualWorld.getCurrentTurn() == 10) {
//            int p = 0;
//        }
//        boolean flag = false;
//        for (Move myOtherHeroMove : copyOfMyHeroesMove) {
//            boolean checkMyHero = myOtherHeroMove.getHero().equals(myHero);
//            boolean checkTwoHero = myOtherHeroMove.getCurrentCell().equals(myHeroNextCell);
//            if (checkTwoHero && !(checkMyHero)) {
//                score += Score.MY_HERO_CELL;
//                flag = true;
////                if (myHero.getCurrentCell().equals(myHeroNextCell)){
////                    score += Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST;
////                }
//                break;
//            }
//        }//TODO: in bashe ya nabashe?

//        HashMap<Cell, Direction> cellNeighbors = Utility.getCellNeighbors(myHeroCurrentCell, virtualWorld.getMap());
//        MyDirection directionCheck = Utility.getDirectionFromCells(myHeroCurrentCell, myHeroNextCell);
//
//        for (Cell cellNeighbor : cellNeighbors.keySet()) {
//
//            if (!copyOfMyHeroesMove.isEmpty()) {
//                Integer i = 0;
//                HashMap<Cell, Direction> cellNeighborsNeighbor = Utility.getCellNeighbors(cellNeighbor, virtualWorld.getMap());
//                for (Cell cellNeighborNeighbor : cellNeighborsNeighbor.keySet()) {
//                    for (Move heroMoveneighbor : copyOfMyHeroesMove) {
//                        Hero otherOurHeroneighbor = heroMoveneighbor.getHero();
//                        if (otherOurHeroneighbor.getCurrentCell().equals(cellNeighborNeighbor)) {
//                            i++;
//                        }
//                    }
//                }
//                score += (i * Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST_IN_ZONE);
//
//                if (cellNeighbor.isInObjectiveZone()) {
//                    score += Score.IN_ZONE;
//                }
//
//                if (directionCheck.equals(MyDirection.FIX)) {
////                    score += Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST_IN_ZONE;
//                }
//
//
////                    if (otherOurHero.getCurrentCell().equals(cellNeighbor)) {
////                        Integer j = 0;
////                        for (Move ourheroMove: copyOfMyHeroesMove){
////                            Hero ourhero = ourheroMove.getHero();
////                            if (!ourhero.equals(myHero) && ourhero.getCurrentCell().equals(myHeroNextCell)){
////                                j++;
////                            }
////                        }
////                            if (myHeroCurrentCell.isInObjectiveZone()){
////                                score += (i * Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST_IN_ZONE);
////
////                            }else {
////                                score += Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST;
////
////                            }
////                    }
//
//            }
//
//        }


//        if (flag && directionCheck.equals(MyDirection.FIX)){
//            score += Score.MY_OTHER_HERO_AROUND_NEGATIVE_COST;
//        }
//        System.out.println("other hero cell score= " + score);
        return score;
    }


    public static Integer reduceDistanceWithOppHeroesInObjectiveZone(Hero myHero, MyDirection myHeroDirection, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove, Hero oppHero, Integer oppHeroInObjZone, Integer oppHeroMaxAreaEffect, ArrayList<Move> myHeroesMoves, ArrayList<Cell> blocks) {

        Integer score = 0;
        Integer distanceSum = 0;
        Integer distanceScore = 0;

        Integer myHeroCanHitMaxDistance = 0;
        boolean myHeroCanHitAnyone = false;
        boolean canNextCellAttack = false;
        Move oppHeroMoveWithMinimumHealth = null;

        Move move = Move.findByHero(copyOfMyHeroesMove, myHero);
        Cell myHeroMoveNextCell = move.getNextCell();
        Cell myHeroMoveCurrentCell = myHero.getCurrentCell();

        if (myHeroMoveCurrentCell.isInObjectiveZone()) {

            for (Ability myHeroAbility : myHero.getOffensiveAbilities()) {
                int canHitDistance = myHeroAbility.getRange() + myHeroAbility.getAreaOfEffect();
                if (myHeroAbility.getRemCooldown() == 0 && (canHitDistance > myHeroCanHitMaxDistance)) {
//                    System.out.println("Hero: " + myHero.getId() + " myHeroAbility = " + myHeroAbility.getName());
                    myHeroCanHitMaxDistance = canHitDistance;
                }
            }

            if (virtualWorld.getCurrentTurn() >= 24) {
                int i = 0;
            }

            for (Move oppHeroMove : copyOfOppHeroesMove) {

                Hero oppHeroMoveHero = oppHeroMove.getHero();
                Cell oppHeroMoveNextCell = oppHeroMove.getNextCell();
//                Cell oppHeroMoveCurrentCell = oppHeroMove.getCurrentCell();
                Cell oppHeroMoveCurrentCell = oppHeroMoveHero.getCurrentCell();
                if (oppHeroMoveCurrentCell.isInVision() && oppHeroMoveCurrentCell.isInObjectiveZone()) {
                    int distance = virtualWorld.manhattanDistance(myHeroMoveCurrentCell, oppHeroMoveCurrentCell);
                    int distanceForSum = virtualWorld.manhattanDistance(myHeroMoveNextCell, oppHeroMoveNextCell);
                    distanceSum += distanceForSum;
                    if (distance <= myHeroCanHitMaxDistance) {
                        if (virtualWorld.isInVision(myHeroMoveCurrentCell, oppHeroMoveCurrentCell)) {
                            myHeroCanHitAnyone = true;
                        }

                        int distance2 = virtualWorld.manhattanDistance(myHeroMoveCurrentCell, oppHeroMoveCurrentCell);
                        if (distance2 > myHeroCanHitMaxDistance) {
                            canNextCellAttack = true;
//                            score += 50000;
//                            break; //if break doesn't exist my hero go to cells that have more enemy;
                        }
                    }

//                    if (oppHeroMoveWithMinimumHealth == null) {
//                        oppHeroMoveWithMinimumHealth = oppHeroMove;
//                    } else {
//                        if (oppHeroMoveHero.getCurrentHP() < oppHeroMoveWithMinimumHealth.getHero().getCurrentHP()) {
//                            oppHeroMoveWithMinimumHealth = oppHeroMove;
//                        }
//                    }
                }
            }

//            for (Move myHeroMove : copyOfMyHeroesMove) {
//                Hero myOtherHero = myHeroMove.getHero();
//                if (myHeroMove.getTargetZoneCell() != null) {
//                    if (myOtherHero.getCurrentCell().isInObjectiveZone()) {
//                        Utility.getAroundHitCells(myHeroMove.getTargetZoneCell(), condidateCell, oppAbilityRangeEffect, condidateObjCells, virtualWorld);
//
//                    }
//
//                } else {
//                    Utility.getAroundHitCells(myOtherHeroCell, condidateCell, oppAbilityRangeEffect, condidateObjCells, virtualWorld);
//                }
//            }




            if (!myHeroCanHitAnyone) {


                score += ScoreStrategy.otherMyHeroCell(myHero, myHeroDirection, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, oppHeroInObjZone, oppHeroMaxAreaEffect, myHeroesMoves, blocks);
                if (virtualWorld.getCurrentTurn() >= 24) {
                    int i = 0;
                }

//                score += ((virtualWorld.getMap().getColumnNum() + virtualWorld.getMap().getRowNum() ) - distanceSum ) * 2000;
//                if (oppHeroMoveWithMinimumHealth != null) {
//                    Direction[] directions = virtualWorld.getPathMoveDirections(myHeroMoveNextCell, oppHeroMoveWithMinimumHealth.getNextCell());
//                    Direction direction = directions[0];

//                score += directions.length * (-2100);
//                    MyDirection directionThatShouldGo = Utility.castDirectionToMyDirection(direction);

//                    if (myHeroDirection.equals(directionThatShouldGo)) {
//                        Integer distance = virtualWorld.manhattanDistance(myHeroMoveNextCell, oppHeroMoveWithMinimumHealth.getNextCell());
////                        score += -Score.DISTANCE_TO_OPP_HEROES * distance;
//                        score =  2 * (-Score.MOVE_COST);
//                    }
//                }
//

//                if (move.getBeforeCell() != null) {
//                    if (move.getBeforeCell().equals(Utility.getCellFromDirection(move.getCurrentCell(), myHeroDirection, virtualWorld.getMap()))) {
//                        score += -1000;
//                    }
//                }
//                if (myHeroDirection.equals(MyDirection.FIX)) {
//                    score += 3 * Score.MOVE_COST;
//                }


//                score += distanceSum * 20000 * Score.MOVE_COST;
                if (canNextCellAttack) {
//                    score += 50000;
                }

            }


        }
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
//        System.out.println("wall cell score= " + score);

        return score;
    }

//    public static Integer hitByOppHeroes(Hero myHero, MyDirection direction, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove) {
//
//        Integer losingHealthSum = 0;
//        Integer killDistanceSum = 0;
//        Integer canHitSum = 0;
//
//        Integer beforeCellScore = 0;
//        Integer losingHealthScore = 0;
//        Integer killDistanceScore = 0;
//        Integer canHitScore = 0;
//
//        Move move = Move.findByHero(copyOfMyHeroesMove, myHero);
//        Cell myHeroCell2 = move.getNextCell(); //TODO: this must be updated of my hero cell
////        System.out.println("myHeroCell2 = " +myHeroCell2.getRow() + " "+ myHeroCell2.getColumn());
//        Cell myHeroCell = Utility.getCellFromDirection(move.getCurrentCell(), direction, virtualWorld.getMap());
////        System.out.println("myHeroCell = " + myHeroCell.getRow() + " "+ myHeroCell.getColumn());
//        for (Move oppHeroMove : copyOfOppHeroesMove) {
//            boolean canHit = false;
//            Hero hero = oppHeroMove.getHero();
////            Cell oppHeroCell = oppHeroMove.getNextCell();
//            Cell oppHeroCell = oppHeroMove.getNextCell();
//            if (oppHeroCell.isInVision()) {
//                Ability maxLosingHealthAbility = null;
//                int maxLosingHealth = 0;
//                int distance = virtualWorld.manhattanDistance(myHeroCell, oppHeroCell);
//                for (Ability ability : hero.getOffensiveAbilities()) {
//                    if (maxLosingHealthAbility == null) {
//                        maxLosingHealthAbility = ability;
//                    }
//
//
//                    boolean abilityCanHit = distance <= (maxLosingHealthAbility.getRange() + maxLosingHealthAbility.getAreaOfEffect());
//
//                    if (abilityCanHit) {
//                        canHit = true;
//                        if (ability.getPower() > maxLosingHealth) {
//                            maxLosingHealthAbility = ability;
//                            maxLosingHealth = ability.getPower();
//                        }
//                    }
//                }
//                if (canHit) {
//                    canHitSum++;
//                    killDistanceSum += distance;
//
//                }
//                if (maxLosingHealthAbility != null) {
//                    losingHealthSum += maxLosingHealth;
//                }
//            }
//        }
//
//        canHitScore = canHitSum * Score.CAN_HIT_COST;
//
//        if (losingHealthSum > myHero.getCurrentHP()) {
//            losingHealthScore = Score.KILL_COST;
//            if (direction.equals(MyDirection.FIX)) {
//                losingHealthScore += 2 * Score.MOVE_COST;
//            }
//            killDistanceScore = killDistanceSum * Score.KILL_DISTANCE_COST;
//
//            Cell moveCell = myHeroCell;
//            Cell beforeCell = move.getBeforeCell();
//            if (moveCell.equals(beforeCell)) {
//                beforeCellScore = Score.BEFORE_CELL_SCORE;
//            }
//        } else {
//            losingHealthScore = Score.HEALTH_COST * losingHealthSum;
//        }
////        System.out.println("losingHealthScore = " + losingHealthScore);
////        System.out.println("k = " + killDistanceScore);
////        System.out.println("canHitScore = " + canHitScore);
//
//        return losingHealthScore + killDistanceScore + canHitScore + beforeCellScore;
////        return losingHealthScore ;
//    }

    public static Integer hitByOppHeroes(Hero myHero, MyDirection direction, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove, ArrayList<Cell> blocks) {

        Integer losingHealthSum = 0;
        Integer killDistanceSum = 0;
        Integer killScore = 0;

        Move move = Move.findByHero(copyOfMyHeroesMove, myHero);

        Cell myHeroNextCell = move.getNextCell();
        Cell myHeroCurrentCell = move.getCurrentCell();
        Cell myHeroBeforeCell = move.getBeforeCell();

        Cell[] objectiveZoneCells = virtualWorld.getMap().getObjectiveZone();
        Cell safeCell = null;
        int safeCellDistance = 0;

        for (Cell objectiveZoneCell : objectiveZoneCells) {

            int objectiveZoneCellLosingHealthSum = 0;
            for (Move oppHeroMove : copyOfOppHeroesMove) {
                boolean canHit = false;
                Cell oppHeroCurrentCell = oppHeroMove.getCurrentCell();

                if (oppHeroCurrentCell.isInVision()) {
                    int distance = virtualWorld.manhattanDistance(objectiveZoneCell, oppHeroCurrentCell);
                    Ability oppHeroMaximumAbility = oppHeroMove.getAbility();
                    int oppHeroMaximumPower = oppHeroMaximumAbility.getPower();
                    Integer distanceThatCanHit = oppHeroMaximumAbility.getRange() + oppHeroMaximumAbility.getAreaOfEffect();
                    canHit = distance <= distanceThatCanHit;
                    if (canHit) {
                        objectiveZoneCellLosingHealthSum += oppHeroMaximumPower;
                    }
                }
            }
            if (objectiveZoneCellLosingHealthSum < myHero.getCurrentHP()) {
                int distance = virtualWorld.manhattanDistance(myHeroCurrentCell, objectiveZoneCell);
                if (safeCell == null) {
                    safeCell = objectiveZoneCell;
                    safeCellDistance = distance;
                } else {
                    if (distance < safeCellDistance) {
                        safeCell = objectiveZoneCell;
                        safeCellDistance = distance;
                    }
                }
            }
        }

        for (Move oppHeroMove : copyOfOppHeroesMove) {
            boolean canHit = false;
            Cell oppHeroCurrentCell = oppHeroMove.getCurrentCell();

            if (oppHeroCurrentCell.isInVision()) {
                int distance = virtualWorld.manhattanDistance(myHeroNextCell, oppHeroCurrentCell);
                Ability oppHeroMaximumAbility = oppHeroMove.getAbility();
                int oppHeroMaximumPower = oppHeroMaximumAbility.getPower();
                Integer distanceThatCanHit = oppHeroMaximumAbility.getRange() + oppHeroMaximumAbility.getAreaOfEffect();
                canHit = distance <= distanceThatCanHit;
                if (canHit) {
                    killDistanceSum += distance;
                    losingHealthSum += oppHeroMaximumPower;
                }
            }
        }

        if (losingHealthSum >= myHero.getCurrentHP()) {

            killScore = Score.KILL_COST;

            if (safeCell != null) {
                Direction[] distancePath = virtualWorld.getPathMoveDirections(myHeroCurrentCell, safeCell, blocks);
                if (distancePath.length != 0) {
                    Direction bestDirection = distancePath[0];
                    MyDirection bestMyDirection = Utility.castDirectionToMyDirection(bestDirection);
                    if (direction.equals(bestMyDirection)) {
                        killScore += Score.SAFE_CELL_SCORE;
                    }
                }

            }
        }

        return killScore;
    }

    public static Integer reduceDistanceToOppHeroesWithMinimumHealth(Hero myHero, MyDirection myHeroDirection, ArrayList<Hero> otherOurHeroes, Hero oppHero, ArrayList<Hero> oppHeroes, World virtualWorld, ArrayList<Move> copyOfMyHeroesMove, ArrayList<Move> copyOfOppHeroesMove, ArrayList<Cell> blocks) {

        Integer distanceSum;
        Integer distanceScore = 0;

        Move oppHeroMoveWithMinimumHealth = null;

        Move move = Move.findByHero(copyOfMyHeroesMove, myHero);
        Cell myHeroMoveNextCell = move.getNextCell();
        Cell myHeroMoveCurrentCell = myHero.getCurrentCell();

        for (Move oppHeroMove : copyOfOppHeroesMove) {
            Hero oppHeroMoveHero = oppHeroMove.getHero();
            Cell oppHeroMoveNextCell = oppHeroMove.getNextCell();
            Cell oppHeroMoveCurrentCell = oppHeroMove.getCurrentCell();

            if (oppHeroMoveNextCell.isInVision()) {
                if (oppHeroMoveWithMinimumHealth == null) {
                    oppHeroMoveWithMinimumHealth = oppHeroMove;
                } else {
                    if (oppHeroMoveHero.getCurrentHP() < oppHeroMoveWithMinimumHealth.getHero().getCurrentHP()) {
                        oppHeroMoveWithMinimumHealth = oppHeroMove;
                    }
                }
            }
        }

        if (oppHeroMoveWithMinimumHealth != null) {

            Cell oppHeroMoveWithMinimumHealthNextCell = oppHeroMoveWithMinimumHealth.getNextCell();
            Cell oppHeroMoveWithMinimumHealthCurrentCell = oppHeroMoveWithMinimumHealth.getCurrentCell();

            distanceSum = virtualWorld.manhattanDistance(myHeroMoveNextCell, oppHeroMoveWithMinimumHealthNextCell);

            int currentDistance = virtualWorld.manhattanDistance(myHeroMoveCurrentCell, oppHeroMoveWithMinimumHealthCurrentCell);
            int myHeroCanHitMaxDistance = Utility.CanHitMaxDistance(myHero);
            boolean canHitOppHeroWithMinimumHealth = myHeroCanHitMaxDistance < currentDistance && myHeroCanHitMaxDistance != -1;

            if (!canHitOppHeroWithMinimumHealth) {
                distanceScore += distanceSum * Score.DISTANCE_COST;
                if (myHeroDirection.equals(MyDirection.FIX)) {
                    distanceScore += 2 * Score.MOVE_COST;
                }
            }
        }

        return distanceScore;
    }
}
