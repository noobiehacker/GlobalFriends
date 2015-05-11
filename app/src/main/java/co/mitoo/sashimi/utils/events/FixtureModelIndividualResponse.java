package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.FixtureModel;

/**
 * Created by david on 15-04-07.
 */
public class FixtureModelIndividualResponse {

    private FixtureModel fixture;

    public FixtureModel getFixture() {
        return fixture;
    }

    public FixtureModelIndividualResponse(FixtureModel fixture) {
        this.fixture = fixture;
    }
}
