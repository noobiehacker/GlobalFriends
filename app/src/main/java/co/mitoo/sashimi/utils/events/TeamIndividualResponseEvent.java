package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.Team;

/**
 * Created by david on 15-03-10.
 */
public class TeamIndividualResponseEvent {

    private Team team;

    public Team getTeam() {
        return team;
    }

    public TeamIndividualResponseEvent(Team team) {
        this.team = team;
    }
}
