package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.MitooModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo._geoLoc;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.utils.events.LocationResponseEvent;

/**
 * Created by david on 14-12-19.
 */

public class LeagueFragment extends MitooLocationFragment {

    private String leagueTitle;
    private LeagueModel leagueModel;
    private League selectedLeague;
    
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
        initializeViews(view);
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

    @Override
    protected void initializeFields(){
        
        setUpMap();
        setFragmentTitle(getSelectedLeague().getName());
    }

    private void initializeViewElements(View view){
        //Work around for the animation to display a gray background during load
        initializeOnClickListeners(view);
        ViewHelper viewHelper = new ViewHelper(getActivity());
        viewHelper.setUpLeagueImage(view, getSelectedLeague());
        viewHelper.setUpLeageText(view , getSelectedLeague());
        setUpText(view , getSelectedLeague());
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
    
    private void setUpMap(){
        _geoLoc getLoc = getSelectedLeague().get_geoloc();
        LatLng latLng =new LatLng(getLoc.getLat(), getLoc.getLng());
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.googleMapFragment)).getMap();
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.addMarker(new MarkerOptions().position(latLng)).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));

    }

    @Subscribe
    public void recieveLocation(LocationResponseEvent event){
        setLocation(event.getLocation());
    }


    public String getLeagueTitle() {
        return leagueTitle;
    }

    public void setLeagueTitle(String leagueTitle) {
        this.leagueTitle = leagueTitle;
    }

    private void setUpText(View view , League league){

        TextView leagueInfoTextView =  (TextView) view.findViewById(R.id.league_join_details);
        leagueInfoTextView.setText(league.getAbout());

    }

    public LeagueModel getLeagueModel() {
        if(leagueModel==null){
            MitooModel model = getMitooActivity().getModel(LeagueModel.class);
            if(model!=null){
                leagueModel = (LeagueModel) model;
            }
        }
        return leagueModel;
    }

    public void setLeagueModel(LeagueModel leagueModel) {
        this.leagueModel = leagueModel;
    }

    public League getSelectedLeague() {
        if(selectedLeague==null){
            Bundle arguments = getArguments();
            String value = arguments.get(getString(R.string.bundle_key_league_object_id)).toString();
            int objectID = Integer.parseInt(value);
            setSelectedLeague(getLeagueModel().getLeagueByObjectID(objectID));
        }
        return selectedLeague;
    }

    public void setSelectedLeague(League selectedLeague) {
        this.selectedLeague = selectedLeague;
    }
}
