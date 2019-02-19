package client.NewAI;

import java.util.ArrayList;

public class KillerOppHero {
    private ArrayList<HeroAbility> heroAbilities;
    private Integer apSum;
    private Integer numOfHeroes;

    public KillerOppHero(ArrayList<HeroAbility> heroAbilities, Integer apSum, Integer numOfHeroes) {
        this.heroAbilities = heroAbilities;
        this.apSum = apSum;
        this.numOfHeroes = numOfHeroes;
    }

    public ArrayList<HeroAbility> getHeroAbilities() {
        return heroAbilities;
    }

    public void setHeroAbilities(ArrayList<HeroAbility> heroAbilities) {
        this.heroAbilities = heroAbilities;
    }

    public Integer getApSum() {
        return apSum;
    }

    public void setApSum(Integer apSum) {
        this.apSum = apSum;
    }

    public Integer getNumOfHeroes() {
        return numOfHeroes;
    }

    public void setNumOfHeroes(Integer numOfHeroes) {
        this.numOfHeroes = numOfHeroes;
    }
}
