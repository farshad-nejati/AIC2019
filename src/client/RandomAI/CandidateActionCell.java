package client.RandomAI;

import client.model.Cell;

import java.util.Collection;
import java.util.Comparator;

public class CandidateActionCell {
    private Cell impactCell;
    private int affectedOppHeroes;

    public CandidateActionCell(Cell impactCell, int affectedOppHeroes) {

        this.impactCell = impactCell;
        this.affectedOppHeroes = affectedOppHeroes;
    }

    public Cell getImpactCell() {
        return impactCell;
    }

    public void setImpactCell(Cell impactCell) {
        this.impactCell = impactCell;
    }

    public int getAffectedOppHeroes() {
        return affectedOppHeroes;
    }

    public void setAffectedOppHeroes(int affectedOppHeroes) {
        this.affectedOppHeroes = affectedOppHeroes;
    }


    public static CandidateActionCell findByCell(Collection<CandidateActionCell> list, Cell cell) {
        return list.stream().filter(object -> cell.equals(object.getImpactCell())).findFirst().orElse(null);
    }

    public void increaseAffectedHeroes() {
        this.affectedOppHeroes ++;
    }

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<CandidateActionCell> affectedNumberOppHeroComparator = new Comparator<CandidateActionCell>() {

        public int compare(CandidateActionCell o1, CandidateActionCell o2) {
            int killerOppHeroSize1 = o1.affectedOppHeroes;
            int killerOppHeroSize2 = o2.affectedOppHeroes;

            //ascending order
            return killerOppHeroSize1-killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

}
