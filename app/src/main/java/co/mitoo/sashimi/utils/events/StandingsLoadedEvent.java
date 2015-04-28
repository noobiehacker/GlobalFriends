package co.mitoo.sashimi.utils.events;

import java.util.List;

import co.mitoo.sashimi.models.appObject.MitooStandings;

/**
 * Created by david on 15-04-20.
 */
public class StandingsLoadedEvent {

    private List<MitooStandings> mitooStandings;

    public StandingsLoadedEvent(List<MitooStandings> mitooStandings) {
        this.mitooStandings = mitooStandings;
    }

    public List<MitooStandings> getMitooStandings() {
        return mitooStandings;
    }
}
