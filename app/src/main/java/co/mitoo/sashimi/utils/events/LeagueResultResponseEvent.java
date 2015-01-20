package co.mitoo.sashimi.utils.events;

import java.util.List;

import co.mitoo.sashimi.models.jsonPojo.League;

/**
 * Created by david on 15-01-19.
 */
public class LeagueResultResponseEvent {
    
    private List<League> result;

    public LeagueResultResponseEvent(List<League> result) {
        this.result = result;
    }

    public List<League> getResult() {
        return result;
    }

    public void setResult(List<League> result) {
        this.result = result;
    }
}
