package client.NewAI.move.inZone;

import client.model.Ability;
import client.model.Cell;
import client.model.Hero;
import client.model.World;

import java.util.ArrayList;

public class InZoneMoving {
    private ArrayList<Hero> myHeroes;
    private World world;
    private Cell[] objectiveCells;
    private ArrayList<ObjectiveCellThreat> objectiveCellThreats;
    private ArrayList<Hero> inVisionOppHeroes;
    private int phaseNumber;

    public InZoneMoving(ArrayList<Hero> inZoneHeroes, World world) {
        this.myHeroes = inZoneHeroes;
        this.world = world;
        this.objectiveCells = world.getMap().getObjectiveZone();
        this.phaseNumber = world.getMovePhaseNum();
        findObjectiveCellThreat();
        getInVisionOppHeroes();
        initializeObjectiveCellThreats();
    }

    public void move(World world) {
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


    public void getInVisionOppHeroes() {
        Hero[] oppHeroes = world.getOppHeroes();
        for (Hero hero: oppHeroes) {
            Cell heroCell = hero.getCurrentCell();
            if (heroCell.isInVision() && heroCell.getColumn() != -1) {
                inVisionOppHeroes.add(hero);
            }
        }
    }


}
