package co.mitoo.sashimi.models;

import android.location.Location;

import java.util.List;

import co.mitoo.sashimi.models.jsonPojo.League;

/**
 * Created by david on 14-11-21.
 */
public interface ICompetitionModel {

    List<League> getLeagueData();

    void updateLeagueData(List<League> data);

    void requestLocation();
}
