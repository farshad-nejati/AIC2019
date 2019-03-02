package client.RandomAI;

import client.model.Cell;
import client.model.Hero;

import java.util.Collection;
import java.util.Comparator;

public class HeroAffects {
    private Hero hero;
    private int affectedNumber;

    public HeroAffects(Hero hero, int affectedNumber) {
        this.hero = hero;
        this.affectedNumber = affectedNumber;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public int getAffectedNumber() {
        return affectedNumber;
    }

    public void setAffectedNumber(int affectedNumber) {
        this.affectedNumber = affectedNumber;
    }

    public void increaseAffected(){
        this.affectedNumber = this.affectedNumber + 1;
    }


    public static HeroAffects findByHero(Collection<HeroAffects> list, Hero hero) {
        return list.stream().filter(object -> hero.equals(object.getHero())).findFirst().orElse(null);
    }

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<HeroAffects> affectsComparator = new Comparator<HeroAffects>() {

        public int compare(HeroAffects o1, HeroAffects o2) {
            int killerOppHeroSize1 = o1.affectedNumber;
            int killerOppHeroSize2 = o2.affectedNumber;

            //ascending order
            return killerOppHeroSize1-killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };


}
