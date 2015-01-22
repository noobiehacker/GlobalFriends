package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.send.JsonLoginSend;

/**
 * Created by david on 14-12-16.
 */
public class JoinRequestEvent  extends TokenRequestEvent {

    private JsonLoginSend credentials;

    public JoinRequestEvent(String email, String password){
        this.credentials = new JsonLoginSend(email,password);
    }

    public JsonLoginSend getCredentials() {
        return credentials;
    }
}
