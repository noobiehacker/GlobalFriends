package co.mitoo.sashimi.utils.events;

import android.location.Location;

/**
 * Created by david on 15-01-09.
 */
public class LocationResponseEvent {

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocationResponseEvent(Location location){
        this.location=location;
    }
}
