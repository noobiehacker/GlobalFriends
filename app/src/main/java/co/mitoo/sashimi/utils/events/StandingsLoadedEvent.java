package co.mitoo.sashimi.utils.events;

import java.util.List;

import co.mitoo.sashimi.models.appObject.StandingsRow;

/**
 * Created by david on 15-04-20.
 */
public class StandingsLoadedEvent {

    private List<StandingsRow> standingRows;

    public StandingsLoadedEvent(List<StandingsRow> standingRows) {
        this.standingRows = standingRows;
    }

    public List<StandingsRow> getStandingRows() {
        return standingRows;
    }
}
