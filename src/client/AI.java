package client;

import client.model.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class AI
{
    RandomAI randomAI;
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
        randomAI = new RandomAI();
        printer = new Printer();
    }

    public void pickTurn(World world) {
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());

        HeroName heroName = pickHero();
        world.pickHero(heroName);

        pickNumber++;
    }

    public void moveTurn(World world) {
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());

        randomAI.randomMove(world);


        printer.printMap(world);
    }

    public void actionTurn(World world) {
        System.out.println("current turn: " + world.getCurrentTurn() + "   current phase: " + world.getCurrentPhase());
        printer.printHeroList(world);
        printer.printOppHeroList(world);

        randomAI.randomAction(world);
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
