package client.RandomAI;

import client.model.Cell;

import java.util.Collection;
import java.util.Comparator;

public class ImpactCell {
    private Cell impactCell;
    private int affectedOppHeroes;

    public ImpactCell(Cell impactCell, int affectedOppHeroes) {

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


    public static ImpactCell findByCell(Collection<ImpactCell> list, Cell cell) {
        return list.stream().filter(object -> cell.equals(object.getImpactCell())).findFirst().orElse(null);
    }


    /*Comparator for sorting the list by Student Name*/
    public static Comparator<ImpactCell> affectedNumberOppHeroComparator = new Comparator<ImpactCell>() {

        public int compare(ImpactCell o1, ImpactCell o2) {
            int killerOppHeroSize1 = o1.affectedOppHeroes;
            int killerOppHeroSize2 = o2.affectedOppHeroes;

            //ascending order
            return killerOppHeroSize1-killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

}
