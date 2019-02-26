package client.NewAI.move.inZone;

import client.model.Cell;
import client.model.Hero;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class ObjectiveCellThreat {
    private Cell cell;
    private int threatNumber;
    private int threatHP;
    ArrayList<Hero> oppHeroSees = new ArrayList<>();

    public ObjectiveCellThreat(Cell cell) {
        this.cell = cell;
        this.threatNumber = 0;
        this.threatHP = 0;

    }

    public ObjectiveCellThreat(Cell cell, int threatNumber, int threatHP) {
        this.cell = cell;
        this.threatNumber = threatNumber;
        this.threatHP = threatHP;
    }

    public Cell getCell() {

        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public int getThreatNumber() {
        return threatNumber;
    }

    public void setThreatNumber(int threatNumber) {
        this.threatNumber = threatNumber;
    }

    public int getThreatHP() {
        return threatHP;
    }

    public void setThreatHP(int threatHP) {
        this.threatHP = threatHP;
    }

    public static ObjectiveCellThreat findByCell(Collection<ObjectiveCellThreat> list, Cell cell) {
        return list.stream().filter(object -> cell.equals(object.getCell())).findFirst().orElse(null);
    }

    public ArrayList<Hero> getOppHeroSees() {
        return oppHeroSees;
    }

    public void setOppHeroSees(ArrayList<Hero> oppHeroSees) {
        this.oppHeroSees = oppHeroSees;
    }

    public void addToOppHeroSees(Hero oppHero) {
        this.oppHeroSees.add(oppHero);
    }

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<ObjectiveCellThreat> threatHPComparator = new Comparator<ObjectiveCellThreat>() {

        public int compare(ObjectiveCellThreat o1, ObjectiveCellThreat o2) {
            int killerOppHeroSize1 = o1.threatHP;
            int killerOppHeroSize2 = o2.threatHP;

            //ascending order
            return killerOppHeroSize1-killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<ObjectiveCellThreat> threatNumberComparator = new Comparator<ObjectiveCellThreat>() {

        public int compare(ObjectiveCellThreat o1, ObjectiveCellThreat o2) {
            int killerOppHeroSize1 = o1.threatNumber;
            int killerOppHeroSize2 = o2.threatNumber;

            //ascending order
            return killerOppHeroSize1-killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };


}
