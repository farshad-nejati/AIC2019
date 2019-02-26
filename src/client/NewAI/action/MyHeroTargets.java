package client.NewAI.action;

import client.model.Hero;

import java.util.Comparator;

public class MyHeroTargets {
    private Hero oppHero;
    private KillerOppHero killerOppHero;
    private Integer apSum;

    public MyHeroTargets(Hero oppHero, KillerOppHero killerOppHero, Integer apSum) {
        this.oppHero = oppHero;
        this.killerOppHero = killerOppHero;
        this.apSum = apSum;
    }

    public Hero getOppHero() {
        return oppHero;
    }

    public void setOppHero(Hero oppHero) {
        this.oppHero = oppHero;
    }

    public KillerOppHero getKillerOppHero() {
        return killerOppHero;
    }

    public void setKillerOppHero(KillerOppHero killerOppHero) {
        this.killerOppHero = killerOppHero;
    }

    public Integer getApSum() {
        return apSum;
    }

    public void setApSum(Integer apSum) {
        this.apSum = apSum;
    }


    /*Comparator for sorting the list by Student Name*/
    public static Comparator<MyHeroTargets> myHeroTargetsComparator = new Comparator<MyHeroTargets>() {

        public int compare(MyHeroTargets o1, MyHeroTargets o2) {
            int killerOppHeroSize1 = o1.getApSum();
            int killerOppHeroSize2 = o2.getApSum();

            //ascending order
            return killerOppHeroSize1-killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

}
