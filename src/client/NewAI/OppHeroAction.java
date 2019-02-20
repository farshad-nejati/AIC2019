package client.NewAI;

import client.RandomAI.EffectiveHero;
import client.model.Ability;
import client.model.Cell;
import client.model.Hero;
import client.model.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OppHeroAction {
    private Hero oppHero;
    ArrayList<HeroPossibleAbilities> candidateMyHeroes;
    private List<KillerOppHero> killerOppHeroes;
    private Integer virtualHP ;
    private boolean possibleDead = false;

    public OppHeroAction(World world, Hero oppHero, ArrayList<ActiveMyHeroes> activeMyHeroes) {
        this.oppHero = oppHero;
        this.virtualHP = oppHero.getCurrentHP();
        this.setCandidateMyHeroes(world, activeMyHeroes);
        killerOppHeroes = new ArrayList<>();
    }


    public ArrayList<TakingParts> getAllPossibleAbilities(ArrayList<TakingParts> takingParts) {
        // TODO: full killerOppHeroes based on candidateHeroes
        // TODO: possibleDead must set to true if size of killerOppHeroes > 0
            // TODO: set target for use ability
        Hero oppHero = this.oppHero;


        return takingParts;
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
    public ArrayList<HeroPossibleAbilities> getCandidateMyHeroes() {
        return this.candidateMyHeroes;
    }

    public void setCandidateMyHeroes(World world, ArrayList<ActiveMyHeroes> activeMyHeroes) {
        Hero oppHero = this.oppHero;
        for (ActiveMyHeroes activeMyHero : activeMyHeroes) {
            Hero myHero = activeMyHero.getMyHero();
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

    public boolean isPossibleDead() {
        return possibleDead;
    }

    public void setPossibleDead(boolean possibleDead) {
        this.possibleDead = possibleDead;
    }

    public List<KillerOppHero> getKillerOppHeroes() {
        return killerOppHeroes;
    }

    public void setKillerOppHeroes(List<KillerOppHero> killerOppHeroes) {
        this.killerOppHeroes = killerOppHeroes;
    }

    public Hero getOppHero() {
        return oppHero;
    }

    public void setOppHero(Hero oppHero) {
        this.oppHero = oppHero;
    }

    public void setCandidateMyHeroes(ArrayList<HeroPossibleAbilities> candidateMyHeroes) {
        this.candidateMyHeroes = candidateMyHeroes;
    }

    public static Comparator<OppHeroAction> getKillerOppHeroesComparator() {
        return KillerOppHeroesComparator;
    }

    public static void setKillerOppHeroesComparator(Comparator<OppHeroAction> killerOppHeroesComparator) {
        KillerOppHeroesComparator = killerOppHeroesComparator;
    }

    public Integer getVirtualHP() {

        return virtualHP;
    }

    public void setVirtualHP(Integer virtualHP) {
        this.virtualHP = virtualHP;
    }

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<OppHeroAction> KillerOppHeroesComparator = new Comparator<OppHeroAction>() {

        public int compare(OppHeroAction o1, OppHeroAction o2) {
            int killerOppHeroSize1 = o1.getKillerOppHeroes().size();
            int killerOppHeroSize2 = o2.getKillerOppHeroes().size();

            //ascending order
            return killerOppHeroSize1-killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

//
//    @Override
//    public int compareTo(Object o) {
//        int killerSize=((OppHeroAction)o).getKillerOppHeroes().size();
//        /* For Ascending order*/
//        return this.size-killerSize;
//
//        /* For Descending order do like this */
//        //return compareage-this.studentage;
//    }
//
//    @Override
//    public int compareTo(Object o) {
//        return 0;
//    }
}
