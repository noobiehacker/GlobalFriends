package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.utils.FixtureWrapper;

/**
 * Created by david on 15-05-09.
 */
public class FixtureNotificationResponseEvent {

    private FixtureWrapper fixture;

    private Throwable e;

    public FixtureWrapper getFixture() {
        return fixture;
    }

    public FixtureNotificationResponseEvent(FixtureWrapper fixture) {
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
