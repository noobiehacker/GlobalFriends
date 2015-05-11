package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-08.
 */
public class ConfirmingUserRequestEvent {

    private String token;

    public ConfirmingUserRequestEvent(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
