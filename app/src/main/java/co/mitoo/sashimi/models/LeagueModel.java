package co.mitoo.sashimi.models;

import co.mitoo.sashimi.models.jsonPojo.League;

/**
 * Created by david on 15-05-10.
 */
public class LeagueModel {

    private League league;
    private boolean leagueIsJoinable= true;

    public LeagueModel(League league) {
        this.league = league;
    }

    public League getLeague() {
        return league;
    }

    public void setLeagueIsJoinable(boolean leagueIsJoinable) {
        this.leagueIsJoinable = leagueIsJoinable;
    }

    public boolean isLeagueIsJoinable() {
        return leagueIsJoinable;
    }

    public LeagueModel(League league, boolean leagueIsJoinable) {
        this.league = league;
        this.leagueIsJoinable = leagueIsJoinable;
    }
}
