package client.NewAI;

import client.NewAI.move.inZone.HeroNeighbors;
import client.NewAI.move.inZone.HeroPosition;
import client.NewAI.move.inZone.ObjectiveCellThreat;
import client.model.*;

import java.util.ArrayList;
import java.util.Collections;

public class Utility {

    public ArrayList<Hero> myHeroes;
    public World world;
    public Cell[] objectiveCells;
    public ArrayList<ObjectiveCellThreat> objectiveCellThreats = new ArrayList<>();
    public ArrayList<Hero> inVisionOppHeroes = new ArrayList<>();
    public int phaseNumber;
    public ArrayList<HeroNeighbors> heroNeighbors = new ArrayList<>();
    public ArrayList<Cell> blockedCells;
    // TODO: change maxAreaEffect to area effect of opp heroes
    public int maxAreaEffect = 5;
    public ArrayList<HeroPosition> heroPositions = new ArrayList<>();


    public Utility(ArrayList<Hero> inZoneHeroes, World world) {
        this.myHeroes = inZoneHeroes;
        this.world = world;
        this.objectiveCells = world.getMap().getObjectiveZone();
        this.phaseNumber = world.getMovePhaseNum();
        initializeHeroPositions();
        getInVisionOppHeroes(world);
        initializeObjectiveCellThreats();
        findObjectiveCellThreat();
        findHeroNeighborCells(world);
    }


    public void initializeHeroPositions() {
        for (Hero myHero : this.myHeroes) {
            heroPositions.add(new HeroPosition(myHero, myHero.getCurrentCell()));
        }
    }


    public boolean updateHeroPosition(Hero myHero, Cell bestCell) {
        Direction[] directions = world.getPathMoveDirections(myHero.getCurrentCell(), bestCell);
        if (directions.length > 0) {
            Direction bestDirection = directions[0];
            Cell newCell = findCellWithDirection(myHero.getCurrentCell(), bestDirection);

            HeroPosition heroPosition = HeroPosition.findByHero(this.heroPositions, myHero);
            ObjectiveCellThreat currentThreat = ObjectiveCellThreat.findByCell(this.objectiveCellThreats, myHero.getCurrentCell());
            ObjectiveCellThreat newCellThreat = ObjectiveCellThreat.findByCell(this.objectiveCellThreats, bestCell);
            if (currentThreat.getThreatNumber() > newCellThreat.getThreatNumber()) {
                if (currentThreat.getThreatNumber() == 1) {
                    if (currentThreat.getOppHeroSees().get(0).getCurrentHP() < myHero.getCurrentHP() ) {
                        return false;
                    }
                }
//            HeroPosition heroPosition = HeroPosition.findByHero(this.heroPositions, myHero);
                int index = this.heroPositions.indexOf(heroPosition);
                heroPosition.setCell(newCell);
                this.heroPositions.set(index, heroPosition);
                return true;
            } else
                return false;
        }
        return false;

    }

    public Cell findBestCellToMove(Hero myHero, ArrayList<Hero> noneZoneHeroes) {
        HeroNeighbors myHeroNeighbors = HeroNeighbors.findByHero(this.heroNeighbors, myHero);
        ArrayList<Cell> neighbors = myHeroNeighbors.getNeighborCells();
        ArrayList<ObjectiveCellThreat> sortedThreatObjects = new ArrayList<>();
        Collections.sort(this.objectiveCellThreats, ObjectiveCellThreat.threatNumberComparator);
        int minNumber = 10;
        ArrayList<ObjectiveCellThreat> objectThreatCellThree = getObjectsWithThreeDistance(myHero, this.objectiveCellThreats, noneZoneHeroes);
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

    public ArrayList<ObjectiveCellThreat> getObjectsWithThreeDistance(Hero currentHero, ArrayList<ObjectiveCellThreat> objectiveCellThreats, ArrayList<Hero> noneZoneHeroes) {
        ArrayList<ObjectiveCellThreat> returnObject = new ArrayList<>();

        ArrayList<HeroPosition> currentHeroPositions = new ArrayList<>(this.heroPositions);
        HeroPosition currentHeroPosition = HeroPosition.findByHero(currentHeroPositions, currentHero);
        currentHeroPositions.remove(currentHeroPosition);

        int maxEffect = this.maxAreaEffect;
        while (maxEffect > this.maxAreaEffect - 1) {

            boolean noneZeroFlag = true;
            for (Hero noneZeroHero : noneZoneHeroes) {
                for (HeroPosition heroPosition : currentHeroPositions) {
                    Hero hero = heroPosition.getHero();
                    int distance = world.manhattanDistance(hero.getCurrentCell(), heroPosition.getCell());
                    if (distance < maxEffect) {
                        noneZeroFlag = false;
                    }
                }
            }
            if (noneZeroFlag) {
                for (ObjectiveCellThreat threatObject : objectiveCellThreats) {
                    boolean flag = true;
                    for (HeroPosition heroPosition : currentHeroPositions) {
                        Hero hero = heroPosition.getHero();
                        int distance = world.manhattanDistance(threatObject.getCell(), heroPosition.getCell());
                        if (distance < maxEffect) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        returnObject.add(threatObject);
                    }
                }
            }
            if (returnObject.size() != 0) {
                break;
            }

            maxEffect--;
        }
        return returnObject;
    }

    public Cell recursiveFindDirection(Cell currentCell, Cell targetCell, ArrayList<Cell> blockCells) {
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
                return targetCell;
            } else {
                copyOfBlockedCells.add(newCell);
                return recursiveFindDirection(currentCell, targetCell, copyOfBlockedCells);
            }
        }
        return null;
    }

    public Cell findCellWithDirection(Cell currentCell, Direction direction) {
        if (direction.equals(Direction.DOWN))
            return world.getMap().getCell(currentCell.getRow() + 1, currentCell.getColumn());
        else if (direction.equals(Direction.UP))
            return world.getMap().getCell(currentCell.getRow() - 1, currentCell.getColumn());
        else if (direction.equals(Direction.RIGHT))
            return world.getMap().getCell(currentCell.getRow(), currentCell.getColumn() + 1);
        else
            return world.getMap().getCell(currentCell.getRow(), currentCell.getColumn() - 1);
    }

    public void addBlockedCells(ArrayList<Hero> myHeroes, Hero currentHero, ArrayList<Cell> blockedCellsNoneZoneHeroes) {
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


    public void findObjectiveCellThreat() {
        for (Hero hero : inVisionOppHeroes) {
            Ability[] offensiveAbilities = hero.getOffensiveAbilities();
            Ability dangerousAbility = findDangerousAbility(offensiveAbilities);
            if (dangerousAbility != null) {
                updateObjectiveCellThreats(hero, dangerousAbility);
            }
        }
    }

    public Ability findDangerousAbility(Ability[] offensiveAbilities) {
        Ability dangerousAbility = null;
        int abilityPower = 1000;
        for (Ability ability : offensiveAbilities) {
            if (ability.getPower() < abilityPower) { // return min power
                dangerousAbility = ability;
            }
        }
        return dangerousAbility;
    }

    public void updateObjectiveCellThreats(Hero hero, Ability dangerousAbility) {
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
            threatObject.addToOppHeroSees(hero);
            this.objectiveCellThreats.set(index, threatObject);
        }

    }

    public ArrayList<Cell> findInRangeCells(Cell heroCell, int abilityRange) {
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


    public void initializeObjectiveCellThreats() {
        for (Cell objectiveCell : objectiveCells) {
            this.objectiveCellThreats.add(new ObjectiveCellThreat(objectiveCell));
        }
    }


    public void getInVisionOppHeroes(World world) {
        Hero[] oppHeroes = world.getOppHeroes();
        for (Hero hero: oppHeroes) {
            Cell heroCell = hero.getCurrentCell();
            if (heroCell.isInVision() && heroCell.getColumn() != -1) {
                this.inVisionOppHeroes.add(hero);
            }
        }
    }

    public void findHeroNeighborCells(World world) {
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
