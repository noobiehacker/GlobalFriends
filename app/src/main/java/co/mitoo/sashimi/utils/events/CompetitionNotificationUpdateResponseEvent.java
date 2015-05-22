package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.Competition;

/**
 * Created by david on 15-05-21.
 */
public class CompetitionNotificationUpdateResponseEvent {

    private Competition competition;

    public CompetitionNotificationUpdateResponseEvent(Competition competition) {
        this.competition = competition;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }
}
