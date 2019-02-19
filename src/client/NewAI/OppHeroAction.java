package client.NewAI;

import client.model.Hero;

import java.util.ArrayList;
import java.util.List;

public class OppHeroAction {
    private Hero oppHero;
    ArrayList<HeroPossibleAbilities> candidateMyHeroes;
    private List<KillerOppHero> killerOppHeroes;
    private Integer virtualHP ;
    private boolean possibleDead = false;

    public OppHeroAction(Hero oppHero, ArrayList<HeroPossibleAbilities> candidateMyHeroes) {
        this.candidateMyHeroes = candidateMyHeroes;
        this.oppHero = oppHero;
        this.virtualHP = oppHero.getCurrentHP();
    }

    public void getAllPossibleAbilities() {
        // TODO: full killerOppHeroes based on candidateHeroes
        Hero oppHero = this.oppHero;
    }
}
