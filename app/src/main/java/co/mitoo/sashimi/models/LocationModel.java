package co.mitoo.sashimi.models;
import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.IsSearchable;
import co.mitoo.sashimi.utils.PredictionWrapper;
import co.mitoo.sashimi.utils.events.GpsRequestEvent;
import co.mitoo.sashimi.utils.events.GpsResponseEvent;
import co.mitoo.sashimi.utils.events.LocationModelQueryResultEvent;
import co.mitoo.sashimi.utils.events.LocationRequestEvent;
import co.mitoo.sashimi.utils.events.LocationResponseEvent;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Prediction;

/**
 * Created by david on 15-01-09.
 */
public class LocationModel extends MitooModel {

    private Location currentLocation;
    private List<Prediction> queryResult;
    private GooglePlaces client;
    private PredictionWrapper selectedPrediction;
    private boolean usingCurrentLocation;
    public LocationModel(Activity activity) {
        super(activity);
        setUsingCurrentLocation(true);
        client = new GooglePlaces(getActivity().getString(R.string.API_key_google_places));
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

                    List<Prediction> queryPrediction = getClient().getQueryPredictions(query , GooglePlaces.Param.name("types").value("geocode"));
                    queryResult = queryPrediction;
                }
                catch(Exception e){

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
            
            BusProvider.post(new LocationModelQueryResultEvent(transFormList(queryResult)));
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

    public List<Prediction> getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(List<Prediction> queryResult) {
        this.queryResult = queryResult;
    }

    private List<IsSearchable> transFormList(List<Prediction> inputList){
        List<IsSearchable> returnList =  new ArrayList<IsSearchable>();
        for(Prediction item :inputList){
            returnList.add(new PredictionWrapper(item));
        }
        return returnList;

    }

    public PredictionWrapper getSelectedPredictionWrapper() {
        return selectedPrediction;
    }

    public void setSelectedPredictionWrapper(PredictionWrapper selectedPrediction) {
        this.selectedPrediction = selectedPrediction;
        setUsingCurrentLocation(false);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isUsingCurrentLocation() {
        return usingCurrentLocation;
    }

    public void setUsingCurrentLocation(boolean usingCurrentLocation) {
        this.usingCurrentLocation = usingCurrentLocation;
    }
}