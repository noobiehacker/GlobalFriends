package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-09.
 */
public class CompetitionRequestByUserID {

    private int userID;

    public CompetitionRequestByUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
