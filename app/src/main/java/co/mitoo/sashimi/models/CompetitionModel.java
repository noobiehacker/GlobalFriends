package co.mitoo.sashimi.models;

import android.content.res.Resources;

import java.util.List;

import co.mitoo.sashimi.models.jsonPojo.League;

/**
 * Created by david on 14-12-08.
 */
public class CompetitionModel extends MitooModel implements ICompetitionModel{

    public CompetitionModel(Resources resources){
        super(resources);
    }

    @Override
    public List<League> getLeagueData() {
        return null;
    }

    @Override
    public void updateLeagueData(List<League> data) {
    }

    public void removeReferences(){
        super.removeReferences();
    }
}
