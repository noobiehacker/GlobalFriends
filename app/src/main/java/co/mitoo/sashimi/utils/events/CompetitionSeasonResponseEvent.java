package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.Competition;

/**
 * Created by david on 15-05-06.
 */
public class CompetitionSeasonResponseEvent {

    private Competition competition;

    public Competition getCompetition() {
        return competition;
    }

    public CompetitionSeasonResponseEvent(Competition competition) {
        this.competition = competition;
    }
}
