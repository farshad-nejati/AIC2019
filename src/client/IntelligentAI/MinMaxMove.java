package client.IntelligentAI;

import client.NewAI.action.areaEffect.AreaEffect;
import client.NewAI.move.noneZone.RespawnObjectiveZoneCell;
import client.model.Ability;
import client.model.Cell;
import client.model.Hero;
import client.model.World;

import java.util.*;

class MinMaxMove {

    private Hero myHero;
    private ArrayList<Hero> otherOurHeroes;
    private ArrayList<Hero> oppHeroes;
    ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells = new ArrayList<>();
    private ArrayList<Move> myHeroesMoves = new ArrayList<>();
    ArrayList<AreaEffect> areaEffectListAIAlgorithm = new ArrayList<>();
    private World virtualWorld;

    public MinMaxMove(Hero myHero, ArrayList<Hero> otherOurHeroes, ArrayList<Hero> oppHeroes, World virtualWorld, ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells, ArrayList<AreaEffect> areaEffectListAIAlgorithm) {
        this.myHero = myHero;
        this.otherOurHeroes = otherOurHeroes;
        this.oppHeroes = oppHeroes;
        this.virtualWorld = virtualWorld;
        this.respawnObjectiveZoneCells = respawnObjectiveZoneCells;
        this.areaEffectListAIAlgorithm = areaEffectListAIAlgorithm;
    }

    public MyDirection getDirection(ArrayList<Move> myHeroesMove) {

        this.myHeroesMoves = myHeroesMove;
        ArrayList<Move> oppHeroesMove = new ArrayList<>();
        for (Hero hero : this.oppHeroes) {
            Ability ability = null;
            Integer maxRange = 0;
            for (AreaEffect areaEffect : areaEffectListAIAlgorithm) {
                if (areaEffect.getHero().equals(hero)) {
                    ability = areaEffect.getAbility();
                    maxRange = areaEffect.getMaxRange();
                }
            }
            Move move = new Move(hero, hero.getCurrentCell(), ability, maxRange);
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

            ArrayList<Hero> recursiveOppHeroes = new ArrayList<>(this.oppHeroes);

            Integer score = eval(this.myHero, direction, MyDirection.FIX, this.otherOurHeroes, oppHero, recursiveOppHeroes, this.virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, Integer.MAX_VALUE);
//            System.out.println("direction = " + direction + " and Score= " + score + "\n");
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
//        otherOurHeroes.remove(newOppHero);

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
        Integer score = 0;


        ArrayList<Cell> blocks = new ArrayList<>();
        blocks = getBlockCells(myHero, copyOfMyHeroesMove);
        Integer oppHeroInObjZone = getOppHeroZoneNumber(copyOfOppHeroesMove);
        // TODO: if opphero dar objzone nabod hameye khodiha beran be target haye khod.

        Integer oppHeroMaxAreaEffect = getMaxAreaOppHero(copyOfOppHeroesMove);
        boolean flag = false;
        if (myHero.getCurrentCell().isInObjectiveZone()) {
            ArrayList<Cell> copyBlocks = new ArrayList<>(blocks);
//            copyBlocks = getoutOfZoneBlockCells(virtualWorld, copyBlocks);
//            score += ScoreStrategy.reduceDistanceWithOppHeroesInObjectiveZone(myHero, myHeroDirection, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, oppHero, oppHeroInObjZone, oppHeroMaxAreaEffect, this.myHeroesMoves, copyBlocks);
            if (score != 0) {
//                flag = true;
            }
        }
        if (oppHeroInObjZone == 0) {
//            resetAllMyHeroTargetCell(this.myHeroesMoves, this.respawnObjectiveZoneCells);
        }
        score += ScoreStrategy.distanceToZone(myHero, myHeroDirection, virtualWorld, copyOfMyHeroesMove, blocks);
//        score += ScoreStrategy.otherWallCell(myHero, myHeroDirection, virtualWorld, copyOfMyHeroesMove);

        score += ScoreStrategy.hitByOppHeroes(myHero, myHeroDirection, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove,blocks);
//        score += ScoreStrategy.otherMyHeroCell(myHero, myHeroDirection, oppHero, virtualWorld, copyOfMyHeroesMove);
        //TODO: when there are no one in obj zone of opphero, pakhsh shan va beran to faseleye moshakhas ke albate age faseleye dorost vaystadan piade she khodesh hal mishe.
//        score += ScoreStrategy.reduceDistanceToOppHeroesWithMinimumHealth(myHero, myHeroDirection,otherOurHeroes, oppHero, oppHeroes, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, blocks);
// score += ScoreStrategy.reduceDistanceToOppHeroesWithMinimumHealth(myHero, myHeroDirection,otherOurHeroes, oppHero, oppHeroes, virtualWorld, copyOfMyHeroesMove, copyOfOppHeroesMove, blocks);

        return score;

    }

    private void resetAllMyHeroTargetCell(ArrayList<Move> myHeroesMoves, ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells) {
        ArrayList<Hero> mustMoveHero = new ArrayList<>();
        ArrayList<RespawnObjectiveZoneCell> freeFromHero = new ArrayList<>();
        for (Move moveHero : myHeroesMoves) {
            Hero myMoveHero = moveHero.getHero();
            if (myMoveHero.getCurrentCell().isInObjectiveZone()) {
                mustMoveHero.add(myMoveHero);
                for (RespawnObjectiveZoneCell res : respawnObjectiveZoneCells) {
                    if (res.getHero().equals(myMoveHero)) {
                        Move move = Move.findByHero(myHeroesMoves, myMoveHero);
                        Integer index = myHeroesMoves.indexOf(move);
                        Cell targetcell = res.getObjectiveZoneCell();
                        move.setTargetZoneCell(targetcell);
                        myHeroesMoves.set(index, move);
                        res.setArrival(false);
                    }
                }
            }
        }
//
//        if (freeFromHero.size() != 0) {
//            for (RespawnObjectiveZoneCell res : freeFromHero) {
//                if (freeFromHero.size() == 0) {
//                    break;
//                }
//                if (mustMoveHero.size() == 0) {
//                    break;
//                } else {
//                    for (Hero hero : mustMoveHero) {
//                        Move move = Move.findByHero(myHeroesMoves, myHero);
//                        Integer index = myHeroesMoves.indexOf(move);
//                        Cell targetcell = res.getObjectiveZoneCell();
//                        move.setTargetZoneCell(targetcell);
//                        myHeroesMoves.set(index, move);
//                        res.setArrival(false);
//                        if (mustMoveHero.size() == 0) {
//                            break;
//                        }
//                    }
//                }
////                freeFromHero.remove(res);
//            }
//        }
        System.out.println("mustMoveHero.size() = " + mustMoveHero.size());
        System.out.println("freeFromHero.size() = " + freeFromHero.size() + "\n\n\n");
        return;
    }


    private Integer getMaxAreaOppHero(ArrayList<Move> copyOfOppHeroesMove) {
        Integer areaEffect = 0;
        if (copyOfOppHeroesMove.size() != 0) {
            for (Move heroMove : copyOfOppHeroesMove) {
                Hero hero = heroMove.getHero();
                Integer thisArea = 0;
                if (hero.getCurrentCell().isInVision()) {
//                    for (Ability ability : hero.getAbilities()) {
//                        if (ability.isReady()) {
//                            if (ability.getAreaOfEffect() > thisArea) {
//                                thisArea = ability.getAreaOfEffect();
//                            }
//                        }
//                    }
                    Ability ability = heroMove.getAbility();
                    Integer maxRange = heroMove.getMaxRange();
                    if (ability.getAreaOfEffect() > thisArea) {
                        thisArea = ability.getAreaOfEffect();
                    }
                }
                if (thisArea > areaEffect) {
                    areaEffect = thisArea;
                }
            }
        }
        return (areaEffect * 2) + 1;
    }

    private Integer getOppHeroZoneNumber(ArrayList<Move> copyOfOppHeroesMove) {
        Integer oppHeroInObjZone = 0;
        for (Move oppHeroMove : copyOfOppHeroesMove) {
            Hero hero = oppHeroMove.getHero();
            if (hero.getCurrentCell().isInObjectiveZone()) {
                oppHeroInObjZone++;
            }
        }
        return oppHeroInObjZone;
    }

    private ArrayList<Cell> getBlockCells(Hero myHero, ArrayList<Move> copyOfMyHeroesMove) {
        ArrayList<Cell> blocks = new ArrayList<>();
//        for (RespawnObjectiveZoneCell respawnObjectiveZoneCell : this.respawnObjectiveZoneCells) {
//            if (!respawnObjectiveZoneCell.getHero().equals(myHero)) {
//                blocks.add(respawnObjectiveZoneCell.getObjectiveZoneCell());
//            }
//        }


//        for (Move myHeroMOve:copyOfMyHeroesMove){
//            if (!myHeroMOve.getHero().equals(myHero)){
//                if (myHeroMOve.getTargetZoneCell()!=null){
//                    blocks.add(myHeroMOve.getTargetZoneCell());
//                }
//            }
//        }


        Cell myHeroNextCell;
        Cell myHeroCurrentCell;
        for (Move myHeroMove : copyOfMyHeroesMove) {
            if (myHeroMove.getHero().equals(myHero)) {
                myHeroCurrentCell = myHeroMove.getCurrentCell();
                myHeroNextCell = myHeroMove.getNextCell();
                break;
            }
        }
        for (Move myHeroMove : copyOfMyHeroesMove) {
            Hero hero = myHeroMove.getHero();

            if (!hero.equals(myHero)) {
                blocks.add(hero.getCurrentCell());

            }
        }

        return blocks;
    }

    public ArrayList<Cell> getoutOfZoneBlockCells(World virtualWorld, ArrayList<Cell> blocks) {
        Cell[] objzoneCell = virtualWorld.getMap().getObjectiveZone();
        int minRow = objzoneCell[0].getRow();
        int maxRow = objzoneCell[0].getRow();
        int mincolumn = objzoneCell[0].getColumn();
        int maxcolumn = objzoneCell[0].getColumn();
        for (Cell obj : objzoneCell) {
            if (obj.getRow() > maxRow) {
                maxRow = obj.getRow();
            }
            if (obj.getRow() < minRow) {
                minRow = obj.getRow();
            }
            if (obj.getColumn() > maxcolumn) {
                maxcolumn = obj.getColumn();
            }
            if (obj.getColumn() < mincolumn) {
                mincolumn = obj.getColumn();
            }
        }
        for (int i = minRow - 2; i <= maxRow + 2; i++) {
            for (int j = mincolumn - 2; j <= maxcolumn + 2; j++) {
                Cell mapCell = virtualWorld.getMap().getCell(i, j);
                if (!mapCell.isInObjectiveZone()) {
                    blocks.add(mapCell);
                }
            }
        }
        return blocks;
    }
}
