package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;

/**
 * Created by david on 14-11-26.
 */
public class SessionModelResponseEvent {

    public SessionModelResponseEvent(SessionRecieve token){
        this.session = token;
    }

    public SessionRecieve getSession() {
        return session;
    }

    private SessionRecieve session;

}
