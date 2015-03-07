package co.mitoo.sashimi.managers;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.LocationRequest;

import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.GpsResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

/**
 * Created by david on 14-12-08.
 */
public class MitooLocationManager {
    
    private MitooActivity mitooActivity;

    public MitooLocationManager(MitooActivity activity) {
        this.mitooActivity = activity;
    }

    public Boolean LocationServicesIsOn() {

        LocationManager locationManager = (LocationManager) this.mitooActivity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public MitooActivity getMitooActivity() {
        return mitooActivity;
    }

}