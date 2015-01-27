package co.mitoo.sashimi.models;

import android.app.Activity;
import android.location.Location;
import android.os.Handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;

import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.GpsRequestEvent;
import co.mitoo.sashimi.utils.events.GpsResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.LocationModelQueryResultEvent;
import co.mitoo.sashimi.utils.events.LocationRequestEvent;
import co.mitoo.sashimi.utils.events.LocationResponseEvent;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

/**
 * Created by david on 15-01-09.
 */
public class LocationModel extends MitooModel {

    private Location currentLocation;
    protected List<Place> queryResult;
    private GooglePlaces client;
    public LocationModel(Activity activity) {
        super(activity);
        client = new GooglePlaces(getActivity().getString(R.string.API_key_google_map));
    }

    @Subscribe
    public void requestCurrentLocation(LocationRequestEvent event) {
        if(currentLocation==null)
            GpsRequest();
        else{
            BusProvider.post(new LocationResponseEvent(currentLocation));
        }
    }

    public void setLocation(Location location) {
        this.currentLocation = location;
    }

    private void GpsRequest(){
        BusProvider.post(new GpsRequestEvent());
    }

    @Subscribe
    public void GpsResponse(GpsResponseEvent event){
        
        setLocation(event.getLocation());
        BusProvider.post(new LocationResponseEvent(currentLocation));
    }
    
    public void searchForPlace(final String query){

        this.handler= new Handler();
        this.backgroundRunnable =new Runnable() {
            @Override
            public void run() {

                try {
               
                    List<Place> result = getClient().getPlacesByQuery(query,GooglePlaces.MAXIMUM_PAGE_RESULTS);
                    queryResult =result;
                }
                catch(Exception e){
                    String temp = e.toString();
                    String temp2= temp + "HAH";
                }
            }
        };

        Thread t = new Thread(this.backgroundRunnable);
        t.start();

        this.getResultsRunnable = createGetResultsRunnable();
        this.handler.postDelayed(this.getResultsRunnable, 1000);

    }


    @Override
    protected void obtainResults(){

        if(this.queryResult !=null){
            BusProvider.post(new LocationModelQueryResultEvent(queryResult));
            setQueryResult(null);
            
        }else
        {
            this.handler.postDelayed(this.getResultsRunnable,1000);
        }

    }

    public GooglePlaces getClient() {
        return client;
    }

    public void setClient(GooglePlaces client) {
        this.client = client;
    }

    public List<Place> getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(List<Place> queryResult) {
        this.queryResult = queryResult;
    }
}