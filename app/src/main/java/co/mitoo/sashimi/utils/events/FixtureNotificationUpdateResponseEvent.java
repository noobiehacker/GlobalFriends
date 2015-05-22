package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.FixtureModel;

/**
 * Created by david on 15-04-09.
 */
public class FixtureNotificationUpdateResponseEvent {

    private FixtureModel fixtureModel;

    public FixtureNotificationUpdateResponseEvent(FixtureModel fixtureModel) {
        this.fixtureModel = fixtureModel;
    }

    public FixtureModel getFixtureModel() {
        return fixtureModel;
    }
}
