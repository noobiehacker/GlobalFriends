package co.mitoo.sashimi.models;

import android.content.res.Resources;
import android.location.Location;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.GpsRequestEvent;
import co.mitoo.sashimi.utils.events.GpsResponseEvent;
import co.mitoo.sashimi.utils.events.LocationRequestEvent;
import co.mitoo.sashimi.utils.events.LocationResponseEvent;

/**
 * Created by david on 15-01-09.
 */
public class LocationModel extends MitooModel {

    private Location location;

    public LocationModel(Resources resources) {
        super(resources);
    }

    @Subscribe
    public void requestLocation(LocationRequestEvent event) {
        if(location==null)
            GpsRequest();
        else{
            BusProvider.post(new LocationResponseEvent(location));
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private void GpsRequest(){
        BusProvider.post(new GpsRequestEvent());
    }

    @Subscribe
    public void GpsResponse(GpsResponseEvent event){
        
        setLocation(event.getLocation());
        BusProvider.post(new LocationResponseEvent(location));
    }
}