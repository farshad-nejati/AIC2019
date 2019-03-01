package client.NewAI.dodge;

import client.NewAI.Helper;
import client.model.Ability;
import client.model.Hero;
import client.model.World;

import java.util.ArrayList;

public class DodgeHelper {


    public static ArrayList<DodgeStatus> getDodgeStatuses(World world, ArrayList<Hero> heroes, boolean skipDeathChecking) {
        ArrayList<DodgeStatus> dodgeStatuses = new ArrayList<>();
        for (Hero myHero : heroes) {
            boolean isDead = Helper.isPossibleDead(world, myHero);
            Ability[] dodgeAbilities = myHero.getDodgeAbilities();
            for (Ability dodgeAbility : dodgeAbilities) {
                if (dodgeAbility.isReady()) {
                    if (isDead || skipDeathChecking) {
                        dodgeStatuses.add(new DodgeStatus(myHero, true, dodgeAbility));
                    } else {
                        dodgeStatuses.add(new DodgeStatus(myHero, false, dodgeAbility));
                    }
                } else {
                    dodgeStatuses.add(new DodgeStatus(myHero, false, dodgeAbility));
                }
            }
        }
        return dodgeStatuses;
    }



    public static ArrayList<Hero> removeEnableDodgeFromList(ArrayList<DodgeStatus> noneZoneDodgeStatuses, ArrayList<Hero> noneZoneHeroes) {
        for (DodgeStatus dodgeStatus : noneZoneDodgeStatuses) {
            if (dodgeStatus.isActive()) { noneZoneHeroes.remove(dodgeStatus.getHero()); }
        }
        return  noneZoneHeroes;
    }
}
