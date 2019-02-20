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
    private Integer virtualHP;
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
        takingParts = createKillerOppHeroes(takingParts);
        return takingParts;
    }

//    private Cell getCellInRangeOfHeroAttack(World world, Hero myHero, Hero oppHero, Ability ability) {
//        Cell myCell = myHero.getCurrentCell();
//        int myRow = myCell.getRow();
//        int myColumn = myCell.getColumn();
//
//        Cell oppCell = oppHero.getCurrentCell();
//        int oppRow = oppCell.getRow();
//        int oppColumn = oppCell.getColumn();
//
//        int areaEffect = ability.getAreaOfEffect();
//        int distance = world.manhattanDistance(myHero.getCurrentCell(), oppHero.getCurrentCell());
//        int bestPlaceRange = areaEffect - ((ability.getRange() + areaEffect) - distance);
//
//        if (oppColumn > myColumn && Math.abs(oppColumn - myColumn) >= areaEffect) {
//            return world.getMap().getCell(oppRow, oppColumn - bestPlaceRange);
//        } else if (oppColumn < myColumn && Math.abs(oppColumn - myColumn) >= areaEffect) {
//            return world.getMap().getCell(oppRow, oppColumn + bestPlaceRange);
//        } else if (oppRow > myRow && Math.abs(oppRow - myRow) >= areaEffect) {
//            return world.getMap().getCell(oppRow - bestPlaceRange, oppColumn);
//        } else {
//            return world.getMap().getCell(oppRow + bestPlaceRange, oppColumn);
//        }
//    }

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
            return killerOppHeroSize1 - killerOppHeroSize2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

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

    public ArrayList<ArrayList<HeroPossibleAbilities>> getSubsets() {
        int n = this.candidateMyHeroes.size();
        ArrayList<ArrayList<HeroPossibleAbilities>> combinations = new ArrayList<>();

        // Run a loop for printing all 2^n
        // subsets one by obe
        for (int i = 0; i < (1 << n); i++) {
            ArrayList<HeroPossibleAbilities> combination = new ArrayList<>();

            // Print current subset
            for (int j = 0; j < n; j++)

                // (1<<j) is a number with jth bit 1
                // so when we 'and' them with the
                // subset number we get which numbers
                // are present in the subset and which
                // are not
                if ((i & (1 << j)) > 0)
                    combination.add(this.candidateMyHeroes.get(j));

            if (combination.size() != 0) {
                combinations.add(combination);
            }
        }
        return combinations;
    }


    public ArrayList<TakingParts> createKillerOppHeroes(ArrayList<TakingParts> takingParts) {

        ArrayList<ArrayList<HeroPossibleAbilities>> res = getSubsets();

        for (ArrayList<HeroPossibleAbilities> heroPossibleAbilitiesArrayList : res) {

            HeroPossibleAbilities heroPossibleAbility1, heroPossibleAbility2, heroPossibleAbility3, heroPossibleAbility4;

            switch (heroPossibleAbilitiesArrayList.size()) {
                case 1:
                    heroPossibleAbility1 = heroPossibleAbilitiesArrayList.get(0);

                    for (Ability ability : heroPossibleAbility1.getAbilities()) {
                        ArrayList<HeroAbility> heroAbilities = new ArrayList<>();
                        heroAbilities.add(new HeroAbility(heroPossibleAbility1.getHero(), ability));
                        takingParts = addKillerOppHeroes(heroAbilities, takingParts);
                    }

                    break;
                case 2:
                    heroPossibleAbility1 = heroPossibleAbilitiesArrayList.get(0);
                    heroPossibleAbility2 = heroPossibleAbilitiesArrayList.get(1);

                    for (Ability ability1 : heroPossibleAbility1.getAbilities()) {
                        for (Ability ability2 : heroPossibleAbility2.getAbilities()) {

                            ArrayList<HeroAbility> heroAbilities = new ArrayList<>();
                            heroAbilities.add(new HeroAbility(heroPossibleAbility1.getHero(), ability1));
                            heroAbilities.add(new HeroAbility(heroPossibleAbility2.getHero(), ability2));
                            takingParts = addKillerOppHeroes(heroAbilities, takingParts);
                        }
                    }

                    break;
                case 3:
                    heroPossibleAbility1 = heroPossibleAbilitiesArrayList.get(0);
                    heroPossibleAbility2 = heroPossibleAbilitiesArrayList.get(1);
                    heroPossibleAbility3 = heroPossibleAbilitiesArrayList.get(2);

                    for (Ability ability1 : heroPossibleAbility1.getAbilities()) {
                        for (Ability ability2 : heroPossibleAbility2.getAbilities()) {
                            for (Ability ability3 : heroPossibleAbility3.getAbilities()) {

                                ArrayList<HeroAbility> heroAbilities = new ArrayList<>();
                                heroAbilities.add(new HeroAbility(heroPossibleAbility1.getHero(), ability1));
                                heroAbilities.add(new HeroAbility(heroPossibleAbility2.getHero(), ability2));
                                heroAbilities.add(new HeroAbility(heroPossibleAbility3.getHero(), ability3));
                                takingParts = addKillerOppHeroes(heroAbilities, takingParts);

                            }
                        }
                    }

                    break;
                case 4:

                    heroPossibleAbility1 = heroPossibleAbilitiesArrayList.get(0);
                    heroPossibleAbility2 = heroPossibleAbilitiesArrayList.get(1);
                    heroPossibleAbility3 = heroPossibleAbilitiesArrayList.get(2);
                    heroPossibleAbility4 = heroPossibleAbilitiesArrayList.get(3);

                    for (Ability ability1 : heroPossibleAbility1.getAbilities()) {
                        for (Ability ability2 : heroPossibleAbility2.getAbilities()) {
                            for (Ability ability3 : heroPossibleAbility3.getAbilities()) {
                                for (Ability ability4 : heroPossibleAbility4.getAbilities()) {

                                    ArrayList<HeroAbility> heroAbilities = new ArrayList<>();
                                    heroAbilities.add(new HeroAbility(heroPossibleAbility1.getHero(), ability1));
                                    heroAbilities.add(new HeroAbility(heroPossibleAbility2.getHero(), ability2));
                                    heroAbilities.add(new HeroAbility(heroPossibleAbility3.getHero(), ability3));
                                    heroAbilities.add(new HeroAbility(heroPossibleAbility4.getHero(), ability4));
                                    takingParts = addKillerOppHeroes(heroAbilities, takingParts);

                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return takingParts;
    }

    public ArrayList<TakingParts> addKillerOppHeroes(ArrayList<HeroAbility> heroAbilities, ArrayList<TakingParts> takingParts) {
        Integer abilityPowerSum = 0;
        Integer apSum = 0;
        Integer numOfHeroes = heroAbilities.size();

        for (HeroAbility heroAbility : heroAbilities) {
            abilityPowerSum += heroAbility.getAbility().getPower();
            apSum += heroAbility.getAbility().getAPCost();
        }

        if (abilityPowerSum >= this.virtualHP) {
            this.possibleDead = true;
            KillerOppHero killerOppHero = new KillerOppHero(heroAbilities, apSum, numOfHeroes);
            killerOppHeroes.add(killerOppHero);

            for (HeroAbility heroAbility : heroAbilities) {
                for (TakingParts takingPart : takingParts) {
                    if (heroAbility.getMyHero() == takingPart.getMyHero()) {
                        if ((numOfHeroes - 1) < takingPart.getMinPartners()) {
                            takingPart.setMinPartners(numOfHeroes - 1);
                        }

                        Integer preNumberOfCollaboration = takingPart.getNumberOFCollaboration();
                        takingPart.setNumberOFCollaboration(preNumberOfCollaboration + 1);
                    }
                }
            }
        }
        return takingParts;
    }
}
