package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-04-23.
 */
public class LoadStandingsEvent {

    private int CompetitionSeasonID;

    public LoadStandingsEvent(int competitionSeasonID) {
        CompetitionSeasonID = competitionSeasonID;
    }

    public int getCompetitionSeasonID() {
        return CompetitionSeasonID;
    }
}
