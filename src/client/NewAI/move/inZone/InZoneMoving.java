package client.NewAI.move.inZone;

import client.NewAI.Utility;
import client.NewAI.action.areaEffect.AreaEffect;
import client.Printer;
import client.model.*;

import java.util.ArrayList;
import java.util.Collections;

public class InZoneMoving {

    private Utility utility;
    public InZoneMoving(ArrayList<Hero> inZoneHeroes, World world, ArrayList<AreaEffect> areaEffects) {
        this.utility = new Utility(inZoneHeroes, world, areaEffects);
    }
    public void move(World world, ArrayList<Hero> noneZoneHeroes) {
        for (HeroPosition heroPosition : utility.heroPositions){
            Hero myHero = heroPosition.getHero();
//        for (Hero myHero : myHeroes) {
//            utility.addBlockedCells(utility.myHeroes, myHero, blockedCellsNoneZoneHeroes);
            Cell bestCell = utility.findBestCellToMove(myHero, noneZoneHeroes);
            if (bestCell != null) {
//                Direction[] directions = world.getPathMoveDirections(myHero.getCurrentCell(), );
//                new Printer().printDirections(directions);
//                if (directions.length > 0) {
                System.out.println("Hero Move " + myHero.getName() + myHero.getId() + " in ");
//                    world.moveHero(myHero, directions[0]);
                if (utility.updateHeroPosition(myHero, bestCell)) {
                    Direction bestDirection = world.getPathMoveDirections(myHero.getCurrentCell(), bestCell)[0];
                    world.moveHero(myHero, bestDirection);
                    //                updateHeroPosition(myHero, bestDirection);
                    System.out.println("Hero Move successfully");
                }

//                }
            } else {
                System.out.println("\n\nBest Cell not Found! \n");
            }
//        }
        }
    }


}
