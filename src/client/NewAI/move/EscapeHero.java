package client.NewAI.move;

import client.model.Hero;

public class EscapeHero {
    private Hero myHero;
    private boolean status;

    public EscapeHero(Hero myHero, boolean status) {
        this.myHero = myHero;
        this.status = status;
    }

    public Hero getMyHero() {

        return myHero;
    }

    public void setMyHero(Hero myHero) {
        this.myHero = myHero;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
