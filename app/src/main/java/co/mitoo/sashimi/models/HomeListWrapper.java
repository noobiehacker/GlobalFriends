package co.mitoo.sashimi.models;

import co.mitoo.sashimi.models.jsonPojo.Competition;

/**
 * Created by david on 15-05-22.
 */
public class HomeListWrapper {

    private LeagueModel leagueModel;
    private Competition competition;

    public HomeListWrapper(LeagueModel leagueModel) {
        this.leagueModel = leagueModel;
    }

    public HomeListWrapper(Competition competition) {
        this.competition = competition;
    }
}
