package co.mitoo.sashimi.utils.events;

import java.util.List;

import co.mitoo.sashimi.models.jsonPojo.Team;

/**
 * Created by david on 15-05-07.
 */
public class TeamListResponseEvent {

    private List<Team> lsitOfTeams;

    public List<Team> getLsitOfTeams() {
        return lsitOfTeams;
    }

    public TeamListResponseEvent(List<Team> lsitOfTeams) {
        this.lsitOfTeams = lsitOfTeams;
    }
}
