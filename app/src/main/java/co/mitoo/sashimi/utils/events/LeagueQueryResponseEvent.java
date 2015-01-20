package co.mitoo.sashimi.utils.events;
import java.util.List;
import co.mitoo.sashimi.models.jsonPojo.League;

/**
 * Created by david on 15-01-15.
 */
public class LeagueQueryResponseEvent {
    
    private List<League> results;

    public LeagueQueryResponseEvent(List<League> results) {
        this.results = results;
    }

    public List<League> getResults() {
        return results;
    }

    public void setResults(List<League> results) {
        this.results = results;
    }
}
