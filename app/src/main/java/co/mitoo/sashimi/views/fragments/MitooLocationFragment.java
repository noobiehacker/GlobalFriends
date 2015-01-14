package co.mitoo.sashimi.views.fragments;

import android.location.Location;
import android.view.View;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LocationRequestEvent;

/**
 * Created by david on 15-01-09.
 */
public abstract class MitooLocationFragment extends MitooFragment {

    private Location location;
    
    @Override
    public void onClick(View v) {
    }

    @Override
    public void onResume(){
        super.onResume();
        handleLocationServices();
        requestLocation();
    }
    
    protected void requestLocation(){
        BusProvider.post(new LocationRequestEvent());
    }

    protected Location getLocation() {
        return location;
    }

    protected void setLocation(Location location) {
        this.location = location;
    }
}
