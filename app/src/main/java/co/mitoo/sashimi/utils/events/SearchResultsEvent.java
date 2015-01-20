package co.mitoo.sashimi.utils.events;

import java.util.List;

import co.mitoo.sashimi.models.jsonPojo.League;

/**
 * Created by david on 15-01-19.
 */
public class SearchResultsEvent {
    
    private List<League> results;
    private String queryString;

    public SearchResultsEvent(List<League> results, String queryString) {
        this.results = results;
        this.queryString = queryString;
    }

    public List<League> getResults() {
        return results;
    }

    public void setResults(List<League> results) {
        this.results = results;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
