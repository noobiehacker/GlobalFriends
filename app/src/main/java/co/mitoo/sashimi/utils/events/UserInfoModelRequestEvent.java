package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-01-22.
 */
public class UserInfoModelRequestEvent {
    
    private int userID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public UserInfoModelRequestEvent(int userID) {
        this.userID = userID;
    }
}
