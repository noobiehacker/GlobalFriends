package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.FixtureModel;

/**
 * Created by david on 15-04-09.
 */
public class NotificationUpdateResponseEvent {

    private FixtureModel fixtureModel;

    public NotificationUpdateResponseEvent(FixtureModel fixtureModel) {
        this.fixtureModel = fixtureModel;
    }

    public FixtureModel getFixtureModel() {
        return fixtureModel;
    }
}
