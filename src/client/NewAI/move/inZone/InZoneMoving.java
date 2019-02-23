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
            Cell bestCell = findBestCellToMove(myHero);
            if (bestCell != null) {
                Direction[] directions = world.getPathMoveDirections(myHero.getCurrentCell(), bestCell);
                new Printer().printDirections(directions);
                if (directions.length > 0) {
                    System.out.println("Hero Move " + myHero.getName() + myHero.getId() + " in ");
                    world.moveHero(myHero, directions[0]);
                    System.out.println("Hero Move successfully");
                }
            } else {
                System.out.println("\n\nBest Cell not Found! \n");
            }
        }
    }

    private Cell findBestCellToMove(Hero myHero) {
        HeroNeighbors myHeroNeighbors = HeroNeighbors.findByHero(this.heroNeighbors, myHero);
        ArrayList<Cell> neighbors = myHeroNeighbors.getNeighborCells();
        ArrayList<ObjectiveCellThreat> sortedThreatObjects = new ArrayList<>();
        Collections.sort(this.objectiveCellThreats, ObjectiveCellThreat.threatNumberComparator);
        int minNumber = 10;
        for (ObjectiveCellThreat objectThreat : this.objectiveCellThreats) {
            Cell cell = objectThreat.getCell();
            if (!neighbors.contains(cell))
                continue;

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
            if (!blockedCells.contains(cell)) {
                return cell;
            }
//            ObjectiveCellThreat bestThreatObject = sortedThreatObjects.get(0);
        }
        return null;
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
        int abilityPower = 0;
        for (Ability ability : offensiveAbilities) {
            if (ability.isReady() && ability.getPower() > abilityPower) {
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
