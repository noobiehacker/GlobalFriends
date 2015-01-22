package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;

/**
 * Created by david on 14-11-26.
 */
public class UserRecieveResponseEvent {

    public UserRecieveResponseEvent(SessionRecieve token){
        this.user = token;
    }

    public SessionRecieve getUser() {
        return user;
    }

    private SessionRecieve user;

}
