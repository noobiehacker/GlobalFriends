package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.send.UserSend;

/**
 * Created by david on 14-11-26.
 */
public class LoginRequestEvent  extends MitooRequestEvent{

    private UserSend user;

    public LoginRequestEvent(String email, String password){
        this.user = new UserSend(email,password);
    }

    public UserSend getLogin() {
        return user;
    }
}
