package client.NewAI.action.areaEffect;

import client.model.Hero;

import java.util.Collection;

public class PowerFullAbility {
    private int id;
    private int remCoolDown;

    public PowerFullAbility(int id, int remCoolDown) {
        this.id = id;
        this.remCoolDown = remCoolDown;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemCoolDown() {
        return remCoolDown;
    }

    public void setRemCoolDown(int remCoolDown) {
        this.remCoolDown = remCoolDown;
    }

    public void increaseRemCoolDown() {
        this.remCoolDown = this.remCoolDown + 1;
    }


    public static PowerFullAbility findByID(Collection<PowerFullAbility> list, int id) {
        return list.stream().filter(object -> id == object.getId()).findFirst().orElse(null);
    }
}
