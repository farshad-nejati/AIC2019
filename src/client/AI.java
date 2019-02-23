package client;

import client.NewAI.NewAction;
import client.NewAI.move.inZone.InZoneMoving;
import client.NewAI.move.Move;
import client.NewAI.move.noneZone.NoneZoneHero;
import client.NewAI.move.noneZone.NoneZoneMoving;
import client.NewAI.move.noneZone.RespawnObjectiveZoneCell;
import client.RandomAI.Moving;
import client.RandomAI.RandomAction;
import client.RandomAI.RandomMove;
import client.model.*;

import java.util.ArrayList;

public class AI
{
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
    private Cell[] objectiveZoneCells;
    private ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells = new ArrayList<>();
    ArrayList<Cell> blockedCells = new ArrayList<>();

    public void preProcess(World world) {
        System.out.println("pre process started");
        System.out.println("world Columns: " + world.getMap().getColumnNum());
        System.out.println("world Columns: " + world.getMap().getRowNum());
        randomAction = new RandomAction();
        newAction = new NewAction();
        printer = new Printer();
        newMove = new Move();

        objectiveZoneCells = world.getMap().getObjectiveZone();
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

        inZoneHeroes = new ArrayList<>();
        noneZoneHeroes = new ArrayList<>();
        findZoneStatusOfHeroes(world);

        if (world.getCurrentTurn() == 4 && world.getMovePhaseNum() == 0) {
            setHeroesInReSpawnCell();
        }
        if (noneZoneHeroes.size() > 0 ) {
            noneZoneMoving.move(world, noneZoneHeroes, blockedCells);
        }
        if (inZoneHeroes.size() > 0) {
            inZoneMoving = new InZoneMoving(inZoneHeroes, world);
            inZoneMoving.move(world);
        }

//        newMove.move(world);



//        if (world.getCurrentTurn() == 4 && world.getMovePhaseNum() == 0) {
//            randomMove = new RandomMove(world);
//        }
//        randomMove.moveToObjectiveZone(world);


        printer.printMap(world);
    }

    public void actionTurn(World world) {
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());
        printer.printHeroList(world);
        printer.printOppHeroList(world);

        printer.printMap(world);

//        randomAction.randomAction(world);
        newAction.action(world);
    }

    public HeroName pickHero() {
        switch (pickNumber) {
            case 1:{
                return HeroName.BLASTER;
            }
            case 2: {
                return HeroName.BLASTER;
            }case 3:{
                return HeroName.BLASTER;
            }
            default: {
                return HeroName.BLASTER;

            }

        }
    }


    private void findNearestCellToHeroes(World world) {
        Cell[] respawnCells = world.getMap().getMyRespawnZone();

        for (Cell respawnCell : respawnCells) {
            Integer nearestCellColumn = 10000;
            Integer nearestCellRaw = 10000;
            Integer nearestManhattan = 10000;
            boolean flag = true;

            System.out.println("ReSpawn Zeno Cell: " + respawnCell.getRow() + " , " + respawnCell.getColumn());
            for (Cell objectiveZoneCell : objectiveZoneCells) {
                if (this.blockedCells.contains(objectiveZoneCell)) {
                    continue;
                }
                if (flag) {
                    nearestCellColumn = objectiveZoneCell.getColumn();
                    nearestCellRaw = objectiveZoneCell.getRow();
                    nearestManhattan = world.manhattanDistance(respawnCell, objectiveZoneCell);
                    flag = false;
                }
                int rawDistance = Math.abs(respawnCell.getRow() - objectiveZoneCell.getRow());
                int columnDistance = Math.abs(respawnCell.getColumn() - objectiveZoneCell.getColumn());
                int newManhattan = world.manhattanDistance(respawnCell, objectiveZoneCell);
                System.out.println("new distance: " + newManhattan);
                System.out.println("old distance: " + nearestManhattan);
                if (newManhattan < nearestManhattan) {
                    nearestCellColumn = objectiveZoneCell.getColumn();
                    nearestCellRaw = objectiveZoneCell.getRow();
                    nearestManhattan = newManhattan;
                    System.out.println(nearestManhattan + " selected! \n\n");
                }
            }
            Cell bestObjectiveCell = world.getMap().getCell(nearestCellRaw, nearestCellColumn);
            this.blockedCells.add(bestObjectiveCell);
            respawnObjectiveZoneCells.add(new RespawnObjectiveZoneCell(bestObjectiveCell, respawnCell));
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
        for (Hero hero: world.getMyHeroes()) {
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
