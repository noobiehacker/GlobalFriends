package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-07.
 */
public class NotificationRequestEvent {

    private int userID;
    private int competitionSeasonID;

    public NotificationRequestEvent(int userID, int competitionSeasonID) {
        this.userID = userID;
        this.competitionSeasonID = competitionSeasonID;
    }

    public int getUserID() {
        return userID;
    }

    public int getCompetitionSeasonID() {
        return competitionSeasonID;
    }
}
