package co.mitoo.sashimi.utils.events;

import android.location.Location;

/**
 * Created by david on 14-12-08.
 */
public class GpsResponseEvent {

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public GpsResponseEvent(Location location){
        this.location=location;
    }
}
