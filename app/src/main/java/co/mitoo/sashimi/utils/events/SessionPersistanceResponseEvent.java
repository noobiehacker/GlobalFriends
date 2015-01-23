package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;

/**
 * Created by david on 15-01-22.
 */
public class SessionPersistanceResponseEvent {
    
    private SessionRecieve persistedObject;

    public SessionPersistanceResponseEvent(SessionRecieve persistedObject) {
        this.persistedObject = persistedObject;
    }

    public SessionRecieve getPersistedObject() {
        return persistedObject;
    }

    public void setPersistedObject(SessionRecieve persistedObject) {
        this.persistedObject = persistedObject;
    }
}
