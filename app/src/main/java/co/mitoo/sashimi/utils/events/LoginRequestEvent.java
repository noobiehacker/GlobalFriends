package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.send.JsonLoginSend;

/**
 * Created by david on 14-11-26.
 */
public class LoginRequestEvent  extends TokenRequestEvent {

    private JsonLoginSend user;

    public LoginRequestEvent(String email, String password){
        this.user = new JsonLoginSend(email,password);
    }

    public JsonLoginSend getLogin() {

        return user;
    }
}
