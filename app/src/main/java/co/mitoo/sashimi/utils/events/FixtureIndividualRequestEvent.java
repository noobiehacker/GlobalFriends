package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-07.
 */
public class FixtureIndividualRequestEvent {

    private int fixtureID;

    public int getFixtureID() {
        return fixtureID;
    }

    public FixtureIndividualRequestEvent(int fixtureID) {
        this.fixtureID = fixtureID;
    }
}
