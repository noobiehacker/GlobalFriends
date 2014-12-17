package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.send.UserSend;

/**
 * Created by david on 14-12-16.
 */
public class JoinRequestEvent  extends MitooRequestEvent{

    private UserSend usersend;

    public JoinRequestEvent(String email, String password){
        this.usersend = new UserSend(email,password);
    }

    public UserSend getLogin() {
        return usersend;
    }
}
