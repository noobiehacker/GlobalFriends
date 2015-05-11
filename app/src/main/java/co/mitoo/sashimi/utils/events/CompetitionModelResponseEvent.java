package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.Competition;

/**
 * Created by david on 15-03-06.
 */
public class CompetitionModelResponseEvent {

    private Competition competition;

    public Competition getCompetition() {
        return competition;
    }

    public CompetitionModelResponseEvent(Competition competition) {
        this.competition = competition;
    }
}
