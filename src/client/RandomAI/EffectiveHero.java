package client.RandomAI;

import client.model.Hero;

public class EffectiveHero {
    private Hero myHero;
    private Hero oppHero;

    public EffectiveHero(Hero myHero, Hero oppHero) {
        this.myHero = myHero;
        this.oppHero = oppHero;
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
}
