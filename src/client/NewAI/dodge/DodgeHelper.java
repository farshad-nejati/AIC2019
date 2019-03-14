package client.NewAI.dodge;

import client.NewAI.Helper;
import client.NewAI.action.areaEffect.AreaEffect;
import client.model.Ability;
import client.model.Hero;
import client.model.HeroName;
import client.model.World;

import java.util.ArrayList;

public class DodgeHelper {


    public static ArrayList<DodgeStatus> getDodgeStatuses(World world, ArrayList<Hero> heroes, ArrayList<AreaEffect> areaEffectList, boolean skipDeathChecking) {
        ArrayList<DodgeStatus> dodgeStatuses = new ArrayList<>();
        for (Hero myHero : heroes) {
            boolean copySkipping = skipDeathChecking;
            if (myHero.getName().equals(HeroName.SENTRY)) {
                copySkipping = true;
            }
            boolean isDead = Helper.isPossibleDead(world, myHero, areaEffectList);
            Ability[] dodgeAbilities = myHero.getDodgeAbilities();
            for (Ability dodgeAbility : dodgeAbilities) {
                if (dodgeAbility.isReady()) {
                    if (isDead || copySkipping) {
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
