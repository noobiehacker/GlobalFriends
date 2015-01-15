package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.events.LocationResponseEvent;

/**
 * Created by david on 14-12-19.
 */

public class LeagueFragment extends MitooLocationFragment {

    public static LeagueFragment newInstance() {
        LeagueFragment fragment = new LeagueFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_league,
                        container, false);
        initializeViewElements(view);
        initializeFields();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.googleMapFragment);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    private void joinAction(){
        fireFragmentChangeAction(R.id.fragment_join);
    }

    private void initializeFields(){
        setUpMap(null);
    }

    private void initializeViewElements(View view){
        //Work around for the animation to display a gray background during load
        initializeOnClickListeners(view);
    }

    private void initializeOnClickListeners(View view){
        view.findViewById(R.id.interestedButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.interestedButton:
                 joinAction();
                break;
        }
    }
    
    private void setUpMap(LatLng latLng){
        latLng = new LatLng( 37.796648, -122.402406);
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.googleMapFragment)).getMap();
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.addMarker(new MarkerOptions().position(latLng)).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

    }

    @Subscribe
    public void recieveLocation(LocationResponseEvent event){
        setLocation(event.getLocation());
    }

}
