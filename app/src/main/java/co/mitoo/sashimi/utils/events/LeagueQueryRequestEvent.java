package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-01-15.
 */
public class LeagueQueryRequestEvent {
    
    private String query;

    public LeagueQueryRequestEvent(String query) {
        this.query = query;
    }
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


}
