package client;

import client.NewAI.Helper;
import client.NewAI.SortClass;
import client.NewAI.action.NewAction;
import client.NewAI.action.areaEffect.AreaEffect;
import client.NewAI.action.areaEffect.AreaEffectHelper;
import client.NewAI.action.areaEffect.PowerFullAbility;
import client.NewAI.dodge.*;
import client.NewAI.move.inZone.InZoneMoving;
import client.NewAI.move.Move;
import client.NewAI.move.noneZone.NoneZoneHero;
import client.NewAI.move.noneZone.NoneZoneMoving;
import client.NewAI.move.noneZone.ObjectiveCellsDistance;
import client.NewAI.move.noneZone.RespawnObjectiveZoneCell;
import client.IntelligentAI.MinMaxAlgorithm;
import client.NewAI.action.NewAction;
import client.RandomAI.Moving;
import client.RandomAI.RandomAction;
import client.RandomAI.RandomMove;
import client.model.*;

import java.util.*;

public class AI {
    static RandomMove randomMove;
    private Move newMove;

    static ArrayList<Moving> movingHeroes;
    RandomAction randomAction;
    NewAction newAction;
    Printer printer;
    private int pickNumber = 1;
    private String mapSizeStatus; // normal, small, big
    private String mapWallStatus; // low, much, normal
    private String mapZoneNumbersStatus; // low, much, normal
    private String mapPathStatus; // linear, Square, circle, rectangle
    private String mapPathNumberStatus; // low, much, normal

    private ArrayList<Hero> noneZoneHeroes;
    private ArrayList<Hero> inZoneHeroes;

    private NoneZoneMoving noneZoneMoving;
    private InZoneMoving inZoneMoving;
    private ArrayList<Cell> objectiveZoneCells;
    private ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells = new ArrayList<>();
    ArrayList<Cell> blockedCells = new ArrayList<>();


    //farshid add this things
    HashMap<Hero, Boolean> heroHashArrival = new HashMap<>();
    // to here


    int maxAreaEffect = 5;
    ArrayList<DodgeStatus> inZoneDodgeStatuses;
    ArrayList<DodgeStatus> noneZoneDodgeStatuses;
    ArrayList<NoneZoneDodge> noneZoneDodges = new ArrayList<>();
    ArrayList<AreaEffect> areaEffectList = new ArrayList<>();
    ArrayList<PowerFullAbility> powerFullAbilities = new ArrayList<>();

    public void preProcess(World world) {
        System.out.println("pre process started");
        System.out.println("world Columns: " + world.getMap().getColumnNum());
        System.out.println("world Columns: " + world.getMap().getRowNum());
        randomAction = new RandomAction();
        printer = new Printer();
        newMove = new Move();

//        objectiveZoneCells = world.getMap().getObjectiveZone();
        objectiveZoneCells = Helper.getSortedObjectiveCells(world);
        findNearestCellToHeroes(world);
        noneZoneMoving = new NoneZoneMoving(respawnObjectiveZoneCells);
    }

    public void pickTurn(World world) {
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());

        HeroName heroName = pickHero();
        world.pickHero(heroName);

        pickNumber++;
    }

    public void moveTurn(World world) {
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getMovePhaseNum());

        printer.printMap(world);

        inZoneHeroes = new ArrayList<>();
        noneZoneHeroes = new ArrayList<>();
        noneZoneDodgeStatuses = new ArrayList<>();
        inZoneDodgeStatuses = new ArrayList<>();

        if (world.getCurrentTurn() == 4 && world.getMovePhaseNum() == 0) {
            firstZoneStatusOfHeroes(world);
            setHeroesInReSpawnCell();
            areaEffectList = AreaEffectHelper.initialAffectArea(world);
            powerFullAbilities = AreaEffectHelper.initialPowerFullAbility(world);
        } else {
            setHeroesInReSpawnCell();
            findZoneStatusOfHeroes(world);
        }

        if (world.getMovePhaseNum() == 0) {
            try {
                AreaEffectHelper.updatePowerFullAbility(world, powerFullAbilities, areaEffectList);
                AreaEffectHelper.updateAreaEffects(world, areaEffectList, powerFullAbilities);
            } catch (Exception e) {
                e.printStackTrace();

                System.out.println("\nError: ");
                new Printer().printPowerAbilities(powerFullAbilities);
                new Printer().printAreaEffectList(areaEffectList);
            }
        }

        inZoneDodgeStatuses = DodgeHelper.getDodgeStatuses(world, inZoneHeroes, areaEffectList, false);
        noneZoneDodgeStatuses = DodgeHelper.getDodgeStatuses(world, noneZoneHeroes, areaEffectList, true);

        ArrayList<Hero> myHeros = new ArrayList<>();

        if (noneZoneHeroes.size() > 0) {
            ArrayList<RespawnObjectiveZoneCell> copyRespawnObjectiveCells = new ArrayList<>(this.respawnObjectiveZoneCells);
            DodgeHelper.removeEnableDodgeFromList(noneZoneDodgeStatuses, noneZoneHeroes); // update noneZone Heroes;
            noneZoneDodges = DodgeMove.executeMove(noneZoneDodges, world, noneZoneHeroes, inZoneHeroes, noneZoneDodgeStatuses, copyRespawnObjectiveCells);
            System.out.println("noneZoneHeroes = " + noneZoneHeroes);
        }

        if (inZoneHeroes.size() > 0) {
//            DodgeHelper.removeEnableDodgeFromList(inZoneDodgeStatuses, inZoneHeroes); // update inZone Heroes;
//            inZoneMoving = new InZoneMoving(inZoneHeroes, world, areaEffectList);
//            inZoneMoving.move(world, noneZoneHeroes);
        }

        if (noneZoneHeroes.size()!=0){
            int j=2;

            myHeros.addAll(noneZoneHeroes);
        }
        if (inZoneHeroes.size() !=0){
            for (Hero hero:inZoneHeroes){
                if (!myHeros.contains(hero)){
                    myHeros.add(hero);
                }
            }
        }

//        myHeros.addAll(inZoneHeroes);


//            noneZoneMoving.move(world, noneZoneHeroes, inZoneHeroes);
//        if (world.getCurrentTurn() == 4 && world.getMovePhaseNum() == 0) {
//            randomMove = new RandomMove(world);
//        }

//        randomMove.moveToObjectiveZone(world);

        printer.printMap(world);

        if (world.getCurrentTurn() == 1 || world.getCurrentTurn() == 4) {
            for (Hero myhero : world.getMyHeroes()) {
                this.heroHashArrival.put(myhero, false);
            }
        }
        for (Hero hero: world.getMyHeroes()){
            if (hero.getCurrentHP() <= 0){
                if (this.heroHashArrival.containsKey(hero)){
                    this.heroHashArrival.put(hero, false);
                }
            }
        }
        for (Hero myhero : myHeros) {
            if (myhero.getCurrentHP() > 0) {
//                myHeros.add(myhero);
            } else {
//                for (Hero hashHero : this.heroHashArrival.keySet()) {
//                    if (hashHero.equals(myhero)) {
//                        this.heroHashArrival.put(hashHero, false);
//                    }
//                }
                if (this.heroHashArrival.containsKey(myhero)){
                    this.heroHashArrival.put(myhero, false);
                }
            }

        }
//        if (noneZoneHeroes.size() > 0 ) {
//            noneZoneMoving.move(world, noneZoneHeroes, blockedCells);
//        }
//        if (inZoneHeroes.size() > 0) {
//            inZoneMoving = new InZoneMoving(inZoneHeroes, world);
//            inZoneMoving.move(world, this.blockedCells);
//        }

//        newMove.move(world);
        ArrayList<Hero> oppHeros = new ArrayList<>(Arrays.asList(world.getOppHeroes()));
        oppHeros.removeIf(obj -> (obj.getCurrentCell().getColumn() == -1 || obj.getCurrentCell().getRow() == -1));

//        for (Hero opphero : oppHeros){
//            if (!opphero.getCurrentCell().isInVision()){
//                oppHeros.remove(opphero);
//            }
//        }
        MinMaxAlgorithm minMaxAlgorithm = new MinMaxAlgorithm(myHeros, oppHeros, respawnObjectiveZoneCells, world, heroHashArrival);
        minMaxAlgorithm.maxMove();

    }


    public void actionTurn(World world) {
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());
        printer.printHeroList(world);
        printer.printOppHeroList(world);
        printer.printMap(world);

        DodgeAction.executeMove(world, noneZoneDodges); // moves with dodge operated
        DodgeHelper.removeEnableDodgeFromList(inZoneDodgeStatuses, inZoneHeroes); // update inZone Heroes;
        DodgeAction.executeAction(world, inZoneHeroes, inZoneDodgeStatuses); // action with dodge operated

        if (inZoneHeroes.size() > 0) {
            newAction = new NewAction(inZoneHeroes, world, areaEffectList);
            newAction.action(world);
        }

//        randomAction.randomAction(world);
    }

    public HeroName pickHero() {
        switch (pickNumber) {
            case 1: {
                return HeroName.BLASTER;
            }
            case 2: {
                return HeroName.BLASTER;
            }
            case 3: {
                return HeroName.BLASTER;
            }
            default: {
                return HeroName.BLASTER;

            }

        }
    }


    private void findNearestCellToHeroes(World world) {
        Cell[] respawnCells = world.getMap().getMyRespawnZone();
        int maxArea = this.maxAreaEffect;

        while (this.respawnObjectiveZoneCells.size() != 4) {
            int i = 0;
            ArrayList<Cell> blocked = new ArrayList<>();
            while (i < objectiveZoneCells.size()) {
                if (this.respawnObjectiveZoneCells.size() == 4) {
                    break;
                }
                this.respawnObjectiveZoneCells = new ArrayList<>();
                for (Cell respawnCell : respawnCells) {
                    ArrayList<ObjectiveCellsDistance> objectiveCellsDistances = new ArrayList<>();
                    System.out.println("ReSpawn Zeno Cell: " + respawnCell.getRow() + " , " + respawnCell.getColumn());
                    for (Cell objectiveZoneCell : objectiveZoneCells) {
                        if (blocked.contains(objectiveZoneCell)) {
                            continue;
                        }
                        int manhattanDistance = world.manhattanDistance(respawnCell, objectiveZoneCell);
                        objectiveCellsDistances.add(new ObjectiveCellsDistance(objectiveZoneCell, manhattanDistance));
                    }
                    Collections.sort(objectiveCellsDistances, SortClass.ObjectiveCellComparator);

                    if (this.respawnObjectiveZoneCells.size() == 0) {
                        ObjectiveCellsDistance bestObjectiveDistanceCell = objectiveCellsDistances.get(0);
                        this.respawnObjectiveZoneCells.add(new RespawnObjectiveZoneCell(bestObjectiveDistanceCell.getObjectiveCell(), respawnCell, false));
                        blocked.add(bestObjectiveDistanceCell.getObjectiveCell());
                        continue;
                    }
                    for (ObjectiveCellsDistance objectiveCellsDistance : objectiveCellsDistances) {
                        Cell bestCell = objectiveCellsDistance.getObjectiveCell();
                        boolean mustAdd = true;
                        for (RespawnObjectiveZoneCell obj : this.respawnObjectiveZoneCells) {
                            Cell selectedSoFarCell = obj.getObjectiveZoneCell();
                            int distance = world.manhattanDistance(selectedSoFarCell, bestCell);
                            if (distance <= maxArea) {
                                mustAdd = false;
                                break;
                            }
                        }
                        if (mustAdd) {
                            this.respawnObjectiveZoneCells.add(new RespawnObjectiveZoneCell(bestCell, respawnCell, false));
                            this.blockedCells.add(bestCell);
                            break;
                        }
                    }
                }
                i++;
            }
            maxArea--;

//            respawnObjectiveZoneCells.add(new RespawnObjectiveZoneCell(bestObjectiveCell, respawnCell));
        }
    }


    private void setHeroesInReSpawnCell() {
        ArrayList<RespawnObjectiveZoneCell> reSpawnObjectiveZoneCells = this.respawnObjectiveZoneCells;
        ArrayList<Hero> noneZoneHeroes = this.noneZoneHeroes;
        for (RespawnObjectiveZoneCell reSpawnObjectiveZoneCell : reSpawnObjectiveZoneCells) {
            Cell reSpawnCell = reSpawnObjectiveZoneCell.getRespawnZoneCell();
            for (Hero hero : noneZoneHeroes) {
                Cell heroCell = hero.getCurrentCell();
                if (reSpawnCell.equals(heroCell)) {
                    reSpawnObjectiveZoneCell.setHero(hero);
                    break;
                }

            }
        }
    }

    private void findZoneStatusOfHeroes(World world) {
        for (Hero hero : world.getMyHeroes()) {
            RespawnObjectiveZoneCell respawnObjectiveZoneCell = RespawnObjectiveZoneCell.findByHero(this.respawnObjectiveZoneCells, hero);
            if (hero.getCurrentCell().equals(respawnObjectiveZoneCell.getObjectiveZoneCell())) {
                int index = respawnObjectiveZoneCells.indexOf(respawnObjectiveZoneCell);
                respawnObjectiveZoneCell.setArrival(true);
                respawnObjectiveZoneCells.set(index, respawnObjectiveZoneCell);
            }
            if (respawnObjectiveZoneCell.isArrival()) {
                isArrivalStatus(hero, respawnObjectiveZoneCell, inZoneHeroes);
            } else {
                isArrivalStatus(hero, respawnObjectiveZoneCell, noneZoneHeroes);
            }
        }
    }

    private void isArrivalStatus(Hero hero, RespawnObjectiveZoneCell respawnObjectiveZoneCell, ArrayList<Hero> ZoneHeroes) {
        if (hero.getCurrentHP() > 0) {
            ZoneHeroes.add(hero);
        } else {
            int index = respawnObjectiveZoneCells.indexOf(respawnObjectiveZoneCell);
            respawnObjectiveZoneCell.setArrival(false);
            respawnObjectiveZoneCells.set(index, respawnObjectiveZoneCell);
        }
    }

    private void firstZoneStatusOfHeroes(World world) {
        for (Hero hero : world.getMyHeroes()) {
            if (hero.getCurrentCell().isInObjectiveZone()) {
                inZoneHeroes.add(hero);
//                noneZoneHeroes.removeIf(obj -> obj.getHero().getId() == hero.getId());
            } else {
                if (hero.getCurrentHP() > 0)
                    noneZoneHeroes.add(hero);
            }
        }
    }

}
