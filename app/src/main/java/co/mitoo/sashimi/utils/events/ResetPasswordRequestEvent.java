package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.send.UserSend;

/**
 * Created by david on 14-12-16.
 */
public class ResetPasswordRequestEvent extends MitooRequestEvent{

    private String email;

    public ResetPasswordRequestEvent(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
