package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.Team;

/**
 * Created by david on 15-03-10.
 */
public class TeamIndividualResponseEvent {

    private Team team;
    private int teamID;

    public Team getTeam() {
        return team;
    }

    public int getTeamID() {
        return teamID;
    }

    public TeamIndividualResponseEvent( int teamID , Team team) {
        this.team = team;
        this.teamID = teamID;
    }
}
