package co.mitoo.sashimi.utils.events;


/**
 * Created by david on 15-05-08.
 */
public class ConfirmInfoSetPasswordRequestEvent {

    private String token;
    private String password;

    public ConfirmInfoSetPasswordRequestEvent(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }
}
