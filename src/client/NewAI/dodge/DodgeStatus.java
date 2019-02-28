package client.NewAI.dodge;

import client.model.Ability;
import client.model.Hero;

import java.util.Collection;

public class DodgeStatus {
    private Hero hero;
    private boolean active;
    private Ability ability;

    public DodgeStatus(Hero hero, boolean active, Ability ability) {
        this.hero = hero;
        this.active = active;
        this.ability = ability;
    }

    public Ability getAbility() {

        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static DodgeStatus findByHero(Collection<DodgeStatus> list, Hero hero) {
        return list.stream().filter(object -> hero.getId() == object.getHero().getId()).findFirst().orElse(null);
    }

}
