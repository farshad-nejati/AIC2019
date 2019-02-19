package client.NewAI;

import client.model.Hero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TakingParts {
    private Hero myHero;
    private Integer numberOFCollaboration;
    private Integer minPartners;


    public TakingParts() {}

    public TakingParts(Hero myHero, Integer numberOFCollaboration, Integer minPartners) {
        this.myHero = myHero;
        this.numberOFCollaboration = numberOFCollaboration;
        this.minPartners = minPartners;
    }

    public Hero getMyHero() {
        return myHero;
    }

    public void setMyHero(Hero myHero) {
        this.myHero = myHero;
    }

    public Integer getNumberOFCollaboration() {
        return numberOFCollaboration;
    }

    public void setNumberOFCollaboration(Integer numberOFCollaboration) {
        this.numberOFCollaboration = numberOFCollaboration;
    }

    public Integer getMinPartners() {
        return minPartners;
    }

    public void setMinPartners(Integer minPartners) {
        this.minPartners = minPartners;
    }

}
