package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.Competition;

/**
 * Created by david on 15-05-21.
 */
public class CompetitionNotificationUpdateResponseEvent {

    private Competition competition;
    private String mitooAction;

    public CompetitionNotificationUpdateResponseEvent(Competition competition, String mitooAction) {
        this.competition = competition;
        this.mitooAction = mitooAction;
    }

    public CompetitionNotificationUpdateResponseEvent(Competition competition) {
        this.competition = competition;
    }

    public Competition getCompetition() {
        return competition;
    }

    public String getMitooAction() {
        return mitooAction;
    }
}
