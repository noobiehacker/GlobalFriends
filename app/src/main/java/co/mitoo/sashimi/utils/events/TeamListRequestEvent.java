package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-07.
 */
public class TeamListRequestEvent {

    private int competitionSeasonID;

    public TeamListRequestEvent(int competitionSeasonID) {
        this.competitionSeasonID = competitionSeasonID;
    }

    public int getCompetitionSeasonID() {
        return competitionSeasonID;
    }
}
