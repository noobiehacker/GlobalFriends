package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.utils.FixtureWrapper;

/**
 * Created by david on 15-04-07.
 */
public class FixtureModelIndividualResponse {

    private FixtureWrapper fixture;

    public FixtureWrapper getFixture() {
        return fixture;
    }

    public FixtureModelIndividualResponse(FixtureWrapper fixture) {
        this.fixture = fixture;
    }
}
