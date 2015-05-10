package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-09.
 */
public class FixtureNotificaitonRequestEvent {

    private int fixtureID;

    public FixtureNotificaitonRequestEvent(int fixtureID) {
        this.fixtureID = fixtureID;
    }

    public int getFixtureID() {
        return fixtureID;
    }
}
