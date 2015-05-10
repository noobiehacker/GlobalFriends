package co.mitoo.sashimi.models;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.IsSearchable;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.PredictionWrapper;
import co.mitoo.sashimi.utils.events.GpsResponseEvent;
import co.mitoo.sashimi.utils.events.LocationModelLocationsSelectedEvent;
import co.mitoo.sashimi.utils.events.LocationModelQueryResultEvent;
import co.mitoo.sashimi.utils.events.LocationRequestEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscriber;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.Prediction;

/**
 * Created by david on 15-01-09.
 */
public class LocationService extends MitooService {

    private Location currentLocation;
    private List<Prediction> queryResult;
    private GooglePlaces client;
    private Place selectedPlace;
    private LatLng selectedLocationLatLng;
    private boolean currentLocationClicked = false;
    private ReactiveLocationProvider reactiveLocationProvider;
    private PredictionWrapper predictionWrapper;

    public LocationService(MitooActivity activity) {
        super(activity);
        client = new GooglePlaces(getActivity().getString(R.string.API_key_google_places));
    }

    public ReactiveLocationProvider getReactiveLocationProvider() {
        if(this.reactiveLocationProvider==null)
            this.reactiveLocationProvider = new ReactiveLocationProvider(getActivity());
        return this.reactiveLocationProvider;
    }

    public void setLocation(Location location) {
        this.currentLocation = location;
    }

    public void GpsCurrentLocationRequest() {

        ReactiveLocationProvider locationProvider = getReactiveLocationProvider();
        locationProvider.getLastKnownLocation()
                .subscribe(new Subscriber<Location>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleGpsResponse(new GpsResponseEvent(null));
                    }

                    @Override
                    public void onNext(Location objectRecieve) {
                        handleGpsResponse(new GpsResponseEvent(objectRecieve));

                    }
                });

    }

    public void handleGpsResponse(GpsResponseEvent event) {

        if (event.getLocation() != null) {

            Location result = event.getLocation();
            setLocation(result);
            setSelectedLocationLatLng(new LatLng(result.getLatitude(), result.getLongitude()));
            BusProvider.post(new LocationModelLocationsSelectedEvent());

        } else {

            setToUseCurrentLocation(false);
            String errorMesssage = getActivity().getString(R.string.error_location_serivces);
            MitooActivitiesErrorEvent errorEvent = new MitooActivitiesErrorEvent(errorMesssage);
            BusProvider.post(errorEvent);

        }

    }
    
    public void searchForPrediction(final String query){

        this.backgroundRunnable =new Runnable() {
            @Override
            public void run() {

                try {
                    List<Prediction> queryPrediction = getClient().getQueryPredictions(query);
                    List<Place> queryPlace =  getClient().getPlacesByQuery(query );
                    DataHelper dataHelper = new DataHelper(getActivity());
                    dataHelper.removeNonCity(queryPrediction);
                    queryResult = queryPrediction;
                    BusProvider.post(new LocationModelQueryResultEvent(transFormList(queryResult)));
                    setQueryResult(null);

                }
                catch(Exception e){
                    String temp = e.toString();

                }
            }
        };

        Thread t = new Thread(this.backgroundRunnable);
        t.start();

    }

    public void selectPlace(final PredictionWrapper selectedPrediction) {
        setPredictionWrapper(selectedPrediction);
        setToUseCurrentLocation(false);
        this.backgroundRunnable =new Runnable() {
            @Override
            public void run() {

                try {

                    Prediction prediction = selectedPrediction.getPrediciton();
                    setSelectedPlace(prediction);
                    setSelectedLocationLatLng(getSelectedPlace());
                    setToUseCurrentLocation(false);

                }
                catch(Exception e){
                    BusProvider.post(new MitooActivitiesErrorEvent());
                }
            }
        };

        Thread t = new Thread(this.backgroundRunnable);
        t.start();

    }

    public GooglePlaces getClient() {
        return client;
    }

    public void setClient(GooglePlaces client) {
        this.client = client;
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

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean isUsingCurrentLocation() {
        return currentLocationClicked;
    }

    public void setToUseCurrentLocation(boolean enable) {

        this.currentLocationClicked = enable;
        if (enable) {
            if (getCurrentLocation() == null)
                GpsCurrentLocationRequest();
            else {
                setSelectedLocationLatLng(getCurrentLocation());
                BusProvider.post(new LocationModelLocationsSelectedEvent());
            }
        }
    }


    public Place getSelectedPlace() {
        return selectedPlace;
    }

    public void setSelectedPlace(Prediction prediction) {

        String reference = prediction.getPlaceReference();
        if(reference!=null)
            this.selectedPlace = getClient().getPlace(prediction.getPlaceReference());
    }

    @Subscribe
    public void requestSelectedLocationLatLng(LocationRequestEvent event){

        if(selectedLocationLatLng!=null)
            BusProvider.post(selectedLocationLatLng);
        else
            BusProvider.post(new LatLng(MitooConstants.invalidConstant, MitooConstants.invalidConstant));
    }
    
    public void setSelectedLocationLatLng(LatLng selectedLocationLatLng) {
        
        this.selectedLocationLatLng = selectedLocationLatLng;
    
    }

    public void setSelectedLocationLatLng(Place place) {
        if(place!=null){
            LatLng latLng =new LatLng(place.getLatitude(),place.getLongitude());
            this.selectedLocationLatLng = latLng;
        }
    }

    public void setSelectedLocationLatLng(Location location) {
        if(location!=null){
            LatLng latLng =new LatLng(location.getLatitude(),location.getLongitude());
            this.selectedLocationLatLng = latLng;
        }
    }

    public PredictionWrapper getPredictionWrapper() {
        return predictionWrapper;
    }

    public void setPredictionWrapper(PredictionWrapper predictionWrapper) {
        this.predictionWrapper = predictionWrapper;
    }
}