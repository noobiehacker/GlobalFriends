package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-21.
 */
public class CompetitionNotificationRequestEvent {

    private int competitionSeasonID;

    public int getCompetitionSeasonID() {
        return competitionSeasonID;
    }

    public void setCompetitionSeasonID(int competitionSeasonID) {
        this.competitionSeasonID = competitionSeasonID;
    }

    public CompetitionNotificationRequestEvent(int competitionSeasonID) {
        this.competitionSeasonID = competitionSeasonID;
    }
}


