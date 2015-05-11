package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-08.
 */
public class MobileTokenAssociateRequestEvent {

    private int userID;

    public int getUserID() {
        return userID;
    }

    public MobileTokenAssociateRequestEvent(int userID) {
        this.userID = userID;
    }
}
