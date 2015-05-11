package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.FixtureModel;

/**
 * Created by david on 15-05-09.
 */
public class FixtureNotificationResponseEvent {

    private FixtureModel fixture;

    private Throwable e;

    public FixtureModel getFixture() {
        return fixture;
    }

    public FixtureNotificationResponseEvent(FixtureModel fixture) {
        this.fixture = fixture;
    }

    public FixtureNotificationResponseEvent(Throwable e) {
        this.e = e;
    }

    public Throwable getE() {
        return e;
    }

    public boolean hasError(){
        return this.e !=null;
    }
}
