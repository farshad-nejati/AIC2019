package client.DodgeGeuss;

import client.model.Hero;

public class DodjeDescriber {
    private Hero hero;
    private Integer id;
    private boolean DodgeActive;
    Integer remainTime;

    public DodjeDescriber(Hero hero, boolean dodgeActive, Integer remainTime) {
        this.hero = hero;
        DodgeActive = dodgeActive;
        this.remainTime = remainTime;
        this.id = hero.getId();
    }

    public Integer getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(Integer remainTime) {
        this.remainTime = remainTime;
    }


    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public boolean isDodgeActive() {
        return DodgeActive;
    }

    public void setDodgeActive(boolean dodgeActive) {
        DodgeActive = dodgeActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
