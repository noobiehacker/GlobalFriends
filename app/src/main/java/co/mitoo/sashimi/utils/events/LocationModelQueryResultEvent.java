package co.mitoo.sashimi.utils.events;

import java.util.List;

import se.walkercrou.places.Place;

/**
 * Created by david on 15-01-26.
 */
public class LocationModelQueryResultEvent {
    
    private List<Place> places;

    public LocationModelQueryResultEvent(List<Place> places) {
        this.places = places;
    }
}
