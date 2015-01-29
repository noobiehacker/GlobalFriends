package co.mitoo.sashimi.utils.events;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by david on 15-01-15.
 */
public class AlgoliaLeagueSearchEvent {
    
    private String query;
    private LatLng latLng;

    public AlgoliaLeagueSearchEvent(String query, LatLng latLng) {
        this.query = query;
        this.latLng = latLng;
    }

    public AlgoliaLeagueSearchEvent(String query) {
        this.query = query;
    }
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
