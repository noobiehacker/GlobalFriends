package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo._geoLoc;
import co.mitoo.sashimi.utils.ViewHelper;

/**
 * Created by david on 14-12-19.
 */

public class LeagueFragment extends MitooFragment {

    private String leagueTitle;
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

    private void joinButtonAction(){

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_key_league_object_id),String.valueOf(getSelectedLeague().getId()));
        fireFragmentChangeAction(R.id.fragment_sign_up, bundle);
    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setFragmentTitle(getSelectedLeague().getName());
    }

    @Override
    protected void initializeViews(View view){
        
        super.initializeViews(view);
        initializeOnClickListeners(view);
        setUpMap();
        ViewHelper viewHelper = new ViewHelper(getActivity());
        viewHelper.setUpLeagueImage(view, getSelectedLeague());
        viewHelper.setUpLeageText(view, getSelectedLeague());
        viewHelper.setLineColor(view, getSelectedLeague());
        viewHelper.setJoinBottonColor(view, getSelectedLeague().getColor_1());
        setUpText(view, getSelectedLeague());
    }

    private void initializeOnClickListeners(View view){
        view.findViewById(R.id.interestedButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.interestedButton:
                 joinButtonAction();
                break;
        }
    }
    
    private void setUpMap(){
        _geoLoc getLoc = getSelectedLeague().get_geoloc();
        LatLng latLng =new LatLng(getLoc.getLat(), getLoc.getLng());
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.googleMapFragment)).getMap();
        MarkerOptions option = new MarkerOptions().position(latLng)
                                                  .snippet(getSelectedLeague().getName())
                                                  .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                                  .flat(true);
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.addMarker(option).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

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
    
    public League getSelectedLeague() {
        if(selectedLeague==null){
            setSelectedLeague(getRetriever().getLeagueModel().getSelectedLeague());
        }
        return selectedLeague;
    }

    public void setSelectedLeague(League selectedLeague) {
        this.selectedLeague = selectedLeague;
    }
}
