package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.Competition;

/**
 * Created by david on 15-05-21.
 */
public class CompetitionNotificationResponseEvent {

    private Competition competition;

    private Throwable e;

    public CompetitionNotificationResponseEvent(Competition competition) {
        this.competition = competition;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public CompetitionNotificationResponseEvent(Throwable e) {
        this.e = e;
    }

    public boolean hasError(){
        return this.e !=null;
    }

}
