package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationReceive;
import co.mitoo.sashimi.utils.FixtureWrapper;

/**
 * Created by david on 15-04-09.
 */
public class NotificationUpdateResponseEvent {

    private FixtureWrapper fixtureWrapper;

    public NotificationUpdateResponseEvent(FixtureWrapper fixtureWrapper) {
        this.fixtureWrapper = fixtureWrapper;
    }

    public FixtureWrapper getFixtureWrapper() {
        return fixtureWrapper;
    }
}
