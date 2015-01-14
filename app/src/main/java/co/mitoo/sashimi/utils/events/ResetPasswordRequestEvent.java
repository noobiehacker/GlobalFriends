package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 14-12-16.
 */
public class ResetPasswordRequestEvent extends TokenRequestEvent {

    private String email;

    public ResetPasswordRequestEvent(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
