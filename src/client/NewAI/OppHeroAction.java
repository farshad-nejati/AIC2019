package client.NewAI;

import client.RandomAI.EffectiveHero;
import client.model.Ability;
import client.model.Cell;
import client.model.Hero;
import client.model.World;

import java.util.ArrayList;
import java.util.List;

public class OppHeroAction {
    private Hero oppHero;
    private Cell targetCell;
    ArrayList<HeroPossibleAbilities> candidateMyHeroes;
    private List<KillerOppHero> killerOppHeroes;
    private Integer virtualHP ;
    private boolean possibleDead = false;

    public OppHeroAction(World world, Hero oppHero) {
        this.oppHero = oppHero;
        this.virtualHP = oppHero.getCurrentHP();
        this.setCandidateMyHeroes(world);
    }


    public void getAllPossibleAbilities() {
        // TODO: full killerOppHeroes based on candidateHeroes
        // TODO: possibleDead must set to true if size of killerOppHeroes > 0
            // TODO: set target for use ability
        Hero oppHero = this.oppHero;
    }

    private Cell getCellInRangeOfHeroAttack(World world, Hero myHero, Hero oppHero, Ability ability) {
        Cell myCell = myHero.getCurrentCell();
        int myRow = myCell.getRow();
        int myColumn = myCell.getColumn();

        Cell oppCell = oppHero.getCurrentCell();
        int oppRow = oppCell.getRow();
        int oppColumn = oppCell.getColumn();

        int areaEffect = ability.getAreaOfEffect();
        int distance = world.manhattanDistance(myHero.getCurrentCell(), oppHero.getCurrentCell());
        int bestPlaceRange = areaEffect - ((ability.getRange() + areaEffect) - distance);

        if (oppColumn > myColumn && Math.abs(oppColumn - myColumn) >= areaEffect) {
            return world.getMap().getCell(oppRow, oppColumn - bestPlaceRange);
        } else if (oppColumn < myColumn && Math.abs(oppColumn - myColumn) >= areaEffect) {
            return world.getMap().getCell(oppRow, oppColumn + bestPlaceRange);
        } else if (oppRow > myRow && Math.abs(oppRow - myRow) >= areaEffect) {
            return world.getMap().getCell(oppRow - bestPlaceRange, oppColumn);
        } else  {
            return world.getMap().getCell(oppRow + bestPlaceRange, oppColumn);
        }
    }

    public void setCandidateMyHeroes(World world) {
        Hero oppHero = this.oppHero;
        Hero[] myHeroes = world.getMyHeroes();
        for (Hero myHero : myHeroes) {

            ArrayList possibleAbilities = new ArrayList();
            Ability[] myHeroAbilities = myHero.getOffensiveAbilities();

            for (Ability ability : myHeroAbilities) {
                if (!ability.isLobbing()) {
                    if (!world.isInVision(myHero.getCurrentCell(), oppHero.getCurrentCell())) {
                        continue;
                    }
                }
                if (!ability.isReady()) {
                    continue;
                }

                int range = ability.getRange() + ability.getAreaOfEffect();
                int distance = world.manhattanDistance(myHero.getCurrentCell(), oppHero.getCurrentCell());
                if (distance <= range) {
                    possibleAbilities.add(ability);
                }
            }

            if (possibleAbilities.size() > 0) {
                this.candidateMyHeroes.add(new HeroPossibleAbilities(myHero, possibleAbilities));
            }
        }
    }
}
