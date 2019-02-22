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

    private ArrayList<NoneZoneHero> noneZoneHeroes = new ArrayList<>();;
    private ArrayList<Hero> inZoneHeroes;

    private NoneZoneMoving noneZoneMoving = new NoneZoneMoving();
    private InZoneMoving inZoneMoving = new InZoneMoving();
    private Cell[] objectiveZoneCells;
    private ArrayList<RespawnObjectiveZoneCell> respawnObjectiveZoneCells = new ArrayList<>();


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
        findZoneStatusOfHeroes(world);

        if (noneZoneHeroes.size() > 0 ) {
            noneZoneMoving.move(world, noneZoneHeroes);
        }
        if (inZoneHeroes.size() > 0) {
            inZoneMoving.move(world, inZoneMoving);
        }

//        newMove.move(world);



        if (world.getCurrentTurn() == 4 && world.getMovePhaseNum() == 0) {
            randomMove = new RandomMove(world);
        }
        randomMove.moveToObjectiveZone(world);


        printer.printMap(world);
    }

    public void actionTurn(World world) {
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());
        printer.printHeroList(world);
        printer.printOppHeroList(world);

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
        Cell nearestCell;
        ArrayList<Cell> blockedCells = new ArrayList<>();
        for (Cell objectiveZoneCell : objectiveZoneCells) {

        }
    }


    private void findZoneStatusOfHeroes(World world) {
        for (Hero hero: world.getMyHeroes()) {
            if (hero.getCurrentCell().isInObjectiveZone()) {
                inZoneHeroes.add(hero);
                noneZoneHeroes.removeIf(obj -> obj.getHero().getId() == hero.getId());
            } else {
                if (!isInNoneZoneHeroList(noneZoneHeroes, hero))
                    noneZoneHeroes.add(new NoneZoneHero(hero, null, false));
            }
        }
    }

    public boolean isInNoneZoneHeroList(ArrayList<NoneZoneHero> noneZoneHeroes, Hero hero) {
        for (NoneZoneHero noneZoneHero: noneZoneHeroes) {
            if (noneZoneHero.getHero().getId() == hero.getId()) {
                return true;
            }
        }
        return false;

    }

}
