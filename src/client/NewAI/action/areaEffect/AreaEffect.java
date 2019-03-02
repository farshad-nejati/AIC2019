package client.NewAI.action.areaEffect;

import client.model.Ability;
import client.model.AbilityName;
import client.model.Hero;

import java.awt.geom.Area;
import java.util.Collection;

public class AreaEffect {
    private Hero hero;
    private int id;
    private Ability ability;
    private int maxRange;

    public AreaEffect(Hero hero, int id, Ability ability, int maxRange) {
        this.hero = hero;
        this.id = id;
        this.ability = ability;
        this.maxRange = maxRange;
    }
    public int getMaxRange() {

        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }


    public static AreaEffect findByHero(Collection<AreaEffect> list, Hero hero) {
        return list.stream().filter(object -> hero.getId() == object.getHero().getId()).findFirst().orElse(null);
    }

    public static AreaEffect findByID(Collection<AreaEffect> list, int id) {
        return list.stream().filter(object -> id == object.getId()).findFirst().orElse(null);
    }

}
