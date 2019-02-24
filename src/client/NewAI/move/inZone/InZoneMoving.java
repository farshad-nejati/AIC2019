package client.NewAI.move.inZone;

import client.Printer;
import client.model.*;

import java.util.ArrayList;
import java.util.Collections;

public class InZoneMoving {
    private ArrayList<Hero> myHeroes;
    private World world;
    private Cell[] objectiveCells;
    private ArrayList<ObjectiveCellThreat> objectiveCellThreats = new ArrayList<>();
    private ArrayList<Hero> inVisionOppHeroes = new ArrayList<>();
    private int phaseNumber;
    private ArrayList<HeroNeighbors> heroNeighbors = new ArrayList<>();
    private ArrayList<Cell> blockedCells;
    int maxAreaEffect = 5;

    public InZoneMoving(ArrayList<Hero> inZoneHeroes, World world) {
        this.myHeroes = inZoneHeroes;
        this.world = world;
        this.objectiveCells = world.getMap().getObjectiveZone();
        this.phaseNumber = world.getMovePhaseNum();
        getInVisionOppHeroes(world);
        initializeObjectiveCellThreats();
        findObjectiveCellThreat();
        findHeroNeighborCells(world);
    }


    public void move(World world, ArrayList<Cell> blockedCellsNoneZoneHeroes) {
        for (Hero myHero : myHeroes) {
            addBlockedCells(myHeroes,myHero, blockedCellsNoneZoneHeroes);
            Direction bestDirection = findBestCellToMove(myHero);
            if (bestDirection != null) {
//                Direction[] directions = world.getPathMoveDirections(myHero.getCurrentCell(), );
//                new Printer().printDirections(directions);
//                if (directions.length > 0) {
                    System.out.println("Hero Move " + myHero.getName() + myHero.getId() + " in ");
//                    world.moveHero(myHero, directions[0]);
                    world.moveHero(myHero, bestDirection);
                    System.out.println("Hero Move successfully");
//                }
            } else {
                System.out.println("\n\nBest Cell not Found! \n");
            }
        }
    }

    private Direction findBestCellToMove(Hero myHero) {
        HeroNeighbors myHeroNeighbors = HeroNeighbors.findByHero(this.heroNeighbors, myHero);
        ArrayList<Cell> neighbors = myHeroNeighbors.getNeighborCells();
        ArrayList<ObjectiveCellThreat> sortedThreatObjects = new ArrayList<>();
        Collections.sort(this.objectiveCellThreats, ObjectiveCellThreat.threatNumberComparator);
        int minNumber = 10;
        ArrayList<ObjectiveCellThreat> objectThreatCellThree = getObjectsWithThreeDistance(myHero, this.objectiveCellThreats);
        for (ObjectiveCellThreat objectThreat : objectThreatCellThree) {
            Cell cell = objectThreat.getCell();
//            if (!neighbors.contains(cell))
//                continue;

            int threatNumber = objectThreat.getThreatNumber();
            if (threatNumber < minNumber) {
                minNumber = threatNumber;
            }
            if (threatNumber == minNumber) {
                sortedThreatObjects.add(objectThreat);
            }
        }
        Collections.sort(sortedThreatObjects, ObjectiveCellThreat.threatHPComparator);
        for (ObjectiveCellThreat obj : sortedThreatObjects) {
            Cell cell = obj.getCell();
//            if (!blockedCells.contains(cell) && !myHero.getCurrentCell().equals(cell)) {
            ArrayList<Cell> newBlockedCells = new ArrayList<>();
            return recursiveFindDirection(myHero.getCurrentCell(), cell, newBlockedCells);
//            Direction returnDirection = recursiveFindDirection(myHero.getCurrentCell(), cell, newBlockedCells);
//            if (returnDirection != null) {
//                return returnDirection;
//            }
//                return cell;
//            }
//            ObjectiveCellThreat bestThreatObject = sortedThreatObjects.get(0);
        }
        return null;
    }

    private ArrayList<ObjectiveCellThreat> getObjectsWithThreeDistance(Hero currentHero, ArrayList<ObjectiveCellThreat> objectiveCellThreats) {
        ArrayList<ObjectiveCellThreat> returnObject = new ArrayList<>();

        ArrayList<Hero> currentMyHeroes = new ArrayList<>(this.myHeroes);
        currentMyHeroes.remove(currentHero);

        int maxEffect = this.maxAreaEffect;
        while (returnObject.size() == 0 && maxEffect > this.maxAreaEffect - 2) {

            for (ObjectiveCellThreat threatObject : objectiveCellThreats) {
                boolean flag = true;
                for (Hero hero : currentMyHeroes) {
                    int distance = world.manhattanDistance(threatObject.getCell(), hero.getCurrentCell());
                    if (distance < maxEffect) {
                        flag = false;
                    }
                }
                if (flag) {
                    returnObject.add(threatObject);
                }
                for (ObjectiveCellThreat obj : returnObject) {
                    int distance = world.manhattanDistance(obj.getCell(), threatObject.getCell());
                }
            }
            maxEffect--;
        }
        return returnObject;
    }

    public Direction recursiveFindDirection(Cell currentCell, Cell targetCell, ArrayList<Cell> blockCells) {
        ArrayList<Cell> copyOfBlockedCells = new ArrayList<>(blockCells);
        Direction[] directions;
        if (copyOfBlockedCells.size() > 0) {
            directions = world.getPathMoveDirections(currentCell, targetCell, copyOfBlockedCells);
        } else {
            directions = world.getPathMoveDirections(currentCell, targetCell);
        }
        if (directions.length > 0) {
            Direction direction = directions[0];
            Cell newCell = findCellWithDirection(currentCell, direction);
            if (newCell.isInObjectiveZone()) {
                return direction;
            } else {
                copyOfBlockedCells.add(newCell);
                return recursiveFindDirection(currentCell, targetCell, copyOfBlockedCells);
            }
        }
        return null;
    }

    private Cell findCellWithDirection(Cell currentCell, Direction direction) {
        if (direction.equals(Direction.DOWN))
            return world.getMap().getCell(currentCell.getRow() + 1, currentCell.getColumn());
        else if (direction.equals(Direction.UP))
            return world.getMap().getCell(currentCell.getRow() - 1, currentCell.getColumn());
        else if (direction.equals(Direction.RIGHT))
            return world.getMap().getCell(currentCell.getRow(), currentCell.getColumn() + 1);
        else
            return world.getMap().getCell(currentCell.getRow(), currentCell.getColumn() - 1);
    }

    private void addBlockedCells(ArrayList<Hero> myHeroes, Hero currentHero, ArrayList<Cell> blockedCellsNoneZoneHeroes) {
        this.blockedCells = new ArrayList<>();
        for (Cell cell: blockedCellsNoneZoneHeroes) {
            blockedCells.add(cell);
        }
        for (Hero myHero : myHeroes) {
            if (!myHero.equals(currentHero)) {
                blockedCells.add(myHero.getCurrentCell());
            }
        }
    }


    private void findObjectiveCellThreat() {
        for (Hero hero : inVisionOppHeroes) {
            Ability[] offensiveAbilities = hero.getOffensiveAbilities();
            Ability dangerousAbility = findDangerousAbility(offensiveAbilities);
            if (dangerousAbility != null) {
                updateObjectiveCellThreats(hero, dangerousAbility);
            }
        }
    }

    private Ability findDangerousAbility(Ability[] offensiveAbilities) {
        Ability dangerousAbility = null;
        int abilityPower = 1000;
        for (Ability ability : offensiveAbilities) {
            if (ability.getPower() < abilityPower) { // return min power
                dangerousAbility = ability;
            }
        }
        return dangerousAbility;
    }

    private void updateObjectiveCellThreats(Hero hero, Ability dangerousAbility) {
        Cell heroCell = hero.getCurrentCell();
        int heroColumn = heroCell.getColumn();
        int heroRaw = heroCell.getRow();
        int abilityRange = dangerousAbility.getRange() + dangerousAbility.getAreaOfEffect();
        ArrayList<Cell> inRangeCells = findInRangeCells(heroCell, abilityRange);
        for (Cell cell : inRangeCells) {
            ObjectiveCellThreat threatObject = ObjectiveCellThreat.findByCell(objectiveCellThreats, cell);
            int index = this.objectiveCellThreats.indexOf(threatObject);
            threatObject.setThreatNumber(threatObject.getThreatNumber() + 1);
            threatObject.setThreatHP(threatObject.getThreatHP() + dangerousAbility.getPower());
            this.objectiveCellThreats.set(index, threatObject);
        }

    }

    private ArrayList<Cell> findInRangeCells(Cell heroCell, int abilityRange) {
        int raw = heroCell.getRow();
        int column = heroCell.getColumn();
        ArrayList<Cell> inRangeCells = new ArrayList<>();
        for (Cell objectiveCell : this.objectiveCells) {
            int distance = this.world.manhattanDistance(heroCell, objectiveCell);
            if (distance <= abilityRange) {
                inRangeCells.add(objectiveCell);
            }
        }
        return inRangeCells;
    }


    private void initializeObjectiveCellThreats() {
        for (Cell objectiveCell : objectiveCells) {
            this.objectiveCellThreats.add(new ObjectiveCellThreat(objectiveCell));
        }
    }


    private void getInVisionOppHeroes(World world) {
        Hero[] oppHeroes = world.getOppHeroes();
        for (Hero hero: oppHeroes) {
            Cell heroCell = hero.getCurrentCell();
            if (heroCell.isInVision() && heroCell.getColumn() != -1) {
                this.inVisionOppHeroes.add(hero);
            }
        }
    }

    private void findHeroNeighborCells(World world) {
        for (Hero myHero : this.myHeroes) {
            ArrayList<Cell> neighborCells = new ArrayList<>();
            for (Cell cell : objectiveCells) {
                int distance = world.manhattanDistance(myHero.getCurrentCell(), cell);
                if (distance <= 1) {
                    neighborCells.add(cell);
                }
            }
            this.heroNeighbors.add(new HeroNeighbors(neighborCells, myHero));
        }
    }


    public void setInVisionOppHeroes(ArrayList<Hero> inVisionOppHeroes) {
        this.inVisionOppHeroes = inVisionOppHeroes;
    }
}
