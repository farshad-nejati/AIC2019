package client.RandomAI;

import client.model.Ability;
import client.model.Cell;
import client.model.Hero;

import java.util.Collection;

public class EffectiveTarget {

    private Hero myHero;
    private Ability ability;
    private Cell targetCell;
    CandidateActionCell candidateActionCell;

    public EffectiveTarget(Hero myHero, Ability ability, CandidateActionCell candidateActionCell) {
        this.myHero = myHero;
        this.ability = ability;
        this.candidateActionCell = candidateActionCell;
        this.targetCell = this.candidateActionCell.getImpactCell();
    }

    public void setMyHero(Hero myHero) {
        this.myHero = myHero;
    }

    public void setTargetCell(Cell targetCell) {
        this.targetCell = targetCell;
    }

    public void setCandidateActionCell(CandidateActionCell candidateActionCell) {
        this.candidateActionCell = candidateActionCell;
    }

    public Hero getMyHero() {
        return myHero;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Cell getTargetCell() {
        return targetCell;
    }

    public CandidateActionCell getCandidateActionCell() {
        return candidateActionCell;
    }

    public static EffectiveTarget findByHero(Collection<EffectiveTarget> list, Hero hero) {
        return list.stream().filter(object -> hero.getId() == object.getMyHero().getId()).findFirst().orElse(null);
    }}

