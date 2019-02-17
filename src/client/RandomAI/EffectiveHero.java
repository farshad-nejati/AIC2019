package client.RandomAI;

import client.model.Ability;
import client.model.Hero;

public class EffectiveHero {
    private Hero myHero;
    private Hero oppHero;
    private Ability randomAbility;

    public EffectiveHero(Hero myHero, Hero oppHero, Ability randomAbility) {
        this.myHero = myHero;
        this.oppHero = oppHero;
        this.randomAbility = randomAbility;
    }

    public Hero getMyHero() {
        return myHero;
    }

    public void setMyHero(Hero meHero) {
        this.myHero = meHero;
    }

    public Hero getOppHero() {
        return oppHero;
    }

    public void setOppHero(Hero oppHero) {
        this.oppHero = oppHero;
    }

    public Ability getRandomAbility() {
        return randomAbility;
    }

    public void setRandomAbility(Ability randomAbility) {
        this.randomAbility = randomAbility;
    }
}
