package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-04.
 */
public class CheckUserEvent {

    private String userIdentifier;

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public CheckUserEvent(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }
}
