package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;

/**
 * Created by david on 15-01-21.
 */
public class SessionLoadedResponseEvent {

    private SessionRecieve userRecieve;

    public SessionLoadedResponseEvent(SessionRecieve sessionRecieve) {
        this.userRecieve = sessionRecieve;
    }

    public SessionRecieve getUserRecieve() {
        return userRecieve;
    }

    public void setUserRecieve(SessionRecieve sessionRecieve) {
        this.userRecieve = sessionRecieve;
    }
}
