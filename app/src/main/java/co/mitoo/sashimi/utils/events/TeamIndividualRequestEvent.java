package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-07.
 */
public class TeamIndividualRequestEvent {

    private int teamID;
    private int competitionSeasonId;

    public int getTeamID() {
        return teamID;
    }

    public int getCompetitionSeasonId() {
        return competitionSeasonId;
    }

    public TeamIndividualRequestEvent(int teamID, int competitionSeasonId) {
        this.teamID = teamID;
        this.competitionSeasonId = competitionSeasonId;
    }
}
