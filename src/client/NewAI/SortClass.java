package client.NewAI;

import client.NewAI.move.inZone.ObjectiveCellThreat;
import client.NewAI.move.noneZone.ObjectiveCellsDistance;

import java.util.Comparator;

public class SortClass {


    /*Comparator for sorting the list by Student Name*/
    public static Comparator<ObjectiveCellsDistance> ObjectiveCellComparator = new Comparator<ObjectiveCellsDistance>() {

        public int compare(ObjectiveCellsDistance o1, ObjectiveCellsDistance o2) {
            int killerOppHeroSize1 = o1.getDistance();
            int killerOppHeroSize2 = o2.getDistance();

            //ascending order
            return killerOppHeroSize1-killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};


}
