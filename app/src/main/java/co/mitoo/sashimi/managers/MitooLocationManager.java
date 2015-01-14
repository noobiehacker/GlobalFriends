package co.mitoo.sashimi.managers;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.GpsResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

/**
 * Created by david on 14-12-08.
 */
public class MitooLocationManager implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener , LocationListener {

    private MitooActivity activity;
    private LocationClient locationClient;
    private LocationRequest locationRequest;
    private Location location;
    private Boolean connected;

    public MitooLocationManager(MitooActivity activity) {
        this.activity= activity;
        locationClient = new LocationClient(this.activity,this, this);
        locationRequest = LocationRequest.create();
        this.connect();

    }

    public void connect(){
        locationClient.connect();

    }

    public void disconnect(){
        if (locationClient.isConnected()) {
            locationClient.removeLocationUpdates(this);
        }
        locationClient.disconnect();
    }

    public void locationRequest(){
        getReactiveLastKnownLocation();
    }

    private Boolean clientIsConnected() {
        return (locationClient != null && locationClient.isConnected());
    }

    private void getSequentialCurrentLocation(){
        if(locationClient.getLastLocation() != null)
            postLocationResponse(locationClient.getLastLocation());
    }

    private void getReactiveLastKnownLocation(){

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this.activity);
        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        postLocationResponse(location);
                    }
                });
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationClient.requestLocationUpdates(locationRequest,this);
    }

    @Override
    public void onLocationChanged(Location location) {

        this.location = location;
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public Boolean LocationServicesIsOn() {

        LocationManager locationManager = (LocationManager) this.activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void postLocationResponse(Location location){
        BusProvider.post(new GpsResponseEvent(location));
    }
}