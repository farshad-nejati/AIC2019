package client.NewAI.defense;

import client.model.Hero;

import java.util.Comparator;

public class HeroLosingHealth {
    private Hero hero;
    private int losingHealth;
    private int hittingHealth;

    public HeroLosingHealth(Hero hero, int losingHealth, int hittingHealth) {
        this.hero = hero;
        this.losingHealth = losingHealth;
        this.hittingHealth = hittingHealth;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public int getLosingHealth() {
        return losingHealth;
    }

    public void setLosingHealth(int losingHealth) {
        this.losingHealth = losingHealth;
    }

    public int getHittingHealth() {
        return hittingHealth;
    }

    public void setHittingHealth(int hittingHealth) {
        this.hittingHealth = hittingHealth;
    }

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<HeroLosingHealth> losingHealthComparator = new Comparator<HeroLosingHealth>() {

        public int compare(HeroLosingHealth o1, HeroLosingHealth o2) {
            int killerOppHeroSize1 = o1.getLosingHealth();
            int killerOppHeroSize2 = o2.getLosingHealth();

            //ascending order
            return killerOppHeroSize1 - killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
}
