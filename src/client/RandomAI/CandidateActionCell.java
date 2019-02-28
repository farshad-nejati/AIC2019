package client.RandomAI;

import client.model.Cell;
import client.model.Hero;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class CandidateActionCell {
    private Cell impactCell;
    private ArrayList<Hero> affectedHeroes = new ArrayList<>();
    private int affectedNumber;

    public CandidateActionCell(Cell impactCell, int affectedOppHeroes) {

        this.impactCell = impactCell;
        this.affectedNumber = affectedOppHeroes;
    }

    public CandidateActionCell(Cell impactCell, ArrayList<Hero> affectedHeroes, int affectedNumber) {
        this.impactCell = impactCell;
        this.affectedHeroes = affectedHeroes;
        this.affectedNumber = affectedNumber;
    }

    public Cell getImpactCell() {
        return impactCell;
    }

    public void setImpactCell(Cell impactCell) {
        this.impactCell = impactCell;
    }

    public ArrayList<Hero> getAffectedHeroes() {
        return affectedHeroes;
    }

    public void setAffectedHeroes(ArrayList<Hero> affectedHeroes) {
        this.affectedHeroes = affectedHeroes;
    }

    public int getAffectedNumber() {
        return affectedNumber;
    }

    public void setAffectedNumber(int affectedNumber) {
        this.affectedNumber = affectedNumber;
    }

    public int getSumHP(){
        int sum = 0;
        for (Hero hero : this.affectedHeroes) {
            sum += hero.getCurrentHP();
        }
        return sum;
    }

    public static CandidateActionCell findByCell(Collection<CandidateActionCell> list, Cell cell) {
        return list.stream().filter(object -> cell.equals(object.getImpactCell())).findFirst().orElse(null);
    }

    public void increaseAffectedHeroes() {
        this.affectedNumber  = this.affectedNumber+ 1;
    }

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<CandidateActionCell> affectedNumberOppHeroComparator = new Comparator<CandidateActionCell>() {

        public int compare(CandidateActionCell o1, CandidateActionCell o2) {
            int killerOppHeroSize1 = o1.affectedNumber;
            int killerOppHeroSize2 = o2.affectedNumber;

            //ascending order
            return killerOppHeroSize1-killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
    /*Comparator for sorting the list by Student Name*/
    public static Comparator<CandidateActionCell> sumHPComparator = new Comparator<CandidateActionCell>() {

        public int compare(CandidateActionCell o1, CandidateActionCell o2) {
            int killerOppHeroSize1 = o1.getSumHP();
            int killerOppHeroSize2 = o2.getSumHP();

            //ascending order
            return killerOppHeroSize1-killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

}
