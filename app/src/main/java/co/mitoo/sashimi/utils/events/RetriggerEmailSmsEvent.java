package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-04.
 */
public class RetriggerEmailSmsEvent {

    private String userID;

    public String getUserID() {
        return userID;
    }

    public RetriggerEmailSmsEvent(String userID) {
        this.userID = userID;
    }
}
