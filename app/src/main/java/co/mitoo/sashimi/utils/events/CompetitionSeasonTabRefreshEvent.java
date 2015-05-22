package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-21.
 */
public class CompetitionSeasonTabRefreshEvent {

    private int CompetitionSeasonID;

    public CompetitionSeasonTabRefreshEvent(int competitionSeasonID) {
        CompetitionSeasonID = competitionSeasonID;
    }

    public int getCompetitionSeasonID() {
        return CompetitionSeasonID;
    }

    public void setCompetitionSeasonID(int competitionSeasonID) {
        CompetitionSeasonID = competitionSeasonID;
    }
}
