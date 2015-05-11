package co.mitoo.sashimi.utils.events;

import java.util.List;

/**
 * Created by david on 15-05-09.
 */
public class CompetitionListResponseEvent {

    private List cometitions;

    public CompetitionListResponseEvent(List cometitions) {
        this.cometitions = cometitions;
    }

    public List getCometitions() {
        return cometitions;
    }
}
