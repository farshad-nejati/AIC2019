package client;

import client.IntelligentAI.MinMaxAlgorithm;
import client.NewAI.NewAction;
import client.RandomAI.Moving;
import client.RandomAI.RandomAction;
import client.RandomAI.RandomMove;
import client.model.*;

import java.util.ArrayList;
import java.util.Arrays;

public class AI
{
    static RandomMove randomMove;
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


    public void preProcess(World world) {
        System.out.println("pre process started");
        System.out.println("world Columns: " + world.getMap().getColumnNum());
        System.out.println("world Columns: " + world.getMap().getRowNum());
        randomAction = new RandomAction();
        newAction = new NewAction();
        printer = new Printer();
    }

    public void pickTurn(World world) {
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());

        HeroName heroName = pickHero();
        world.pickHero(heroName);

        pickNumber++;
    }

    public void moveTurn(World world) {
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getMovePhaseNum());

//        if (world.getCurrentTurn() == 4 && world.getMovePhaseNum() == 0) {
//            randomMove = new RandomMove(world);
//        }

//        randomMove.moveToObjectiveZone(world);

        ArrayList<Hero> myHeros = new ArrayList<>(Arrays.asList(world.getMyHeroes()));

        ArrayList<Hero> oppHeros = new ArrayList<>(Arrays.asList(world.getOppHeroes()));
        oppHeros.removeIf(obj -> (obj.getCurrentCell().getColumn()== -1 || obj.getCurrentCell().getRow()== -1));


        MinMaxAlgorithm minMaxAlgorithm = new MinMaxAlgorithm(myHeros,oppHeros,world);
        minMaxAlgorithm.maxMove();


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
}
