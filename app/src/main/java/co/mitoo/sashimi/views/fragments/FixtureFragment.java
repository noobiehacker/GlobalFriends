package co.mitoo.sashimi.views.fragments;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;

import java.util.Locale;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.FixtureWrapper;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-04-01.
 */
public class FixtureFragment extends MitooFragment {

    private int teamColor = MitooConstants.invalidConstant;
    private FixtureWrapper fixtureWrapper;
    private TextView resultTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView locationTextView;
    private TextView statusTextView;
    private TextView addressTextView;

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable()){
            switch (v.getId()) {
            }
        }
    }

    public static FixtureFragment newInstance() {
        FixtureFragment fragment = new FixtureFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_individual_fixture,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.googleFixtureMapFragment);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        initializeAllTextView(view);
        setUpAllTextViews();
        showAndHideLogic(view);
        setUpMap();

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setFixtureWrapper(getFixtureModel().getSelectedFixture());
        setFragmentTitle(getFixtureTitle());
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    @Override
    protected void initializeOnClickListeners(View view) {
    }

    private void initializeAllTextView(View view){
        setResultTextView((TextView) view.findViewById(R.id.fixtureResultText));
        setDateTextView((TextView) view.findViewById(R.id.fixtureDateText));
        setTimeTextView((TextView) view.findViewById(R.id.fixtureTimeText));
        setLocationTextView((TextView) view.findViewById(R.id.fixtureLocationText));
        setStatusTextView((TextView) view.findViewById(R.id.fixtureStatusText));
        setAddressTextView((TextView) view.findViewById(R.id.fixtureAddressText));
    }

    private void showAndHideLogic(View view){

        if(fixtureWrapper.getFixture().getResult()==null){
            RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.fixture_top_details);
            layout.setVisibility(View.GONE);
        }

    }

    public int getTeamColor() {
        if (teamColor == MitooConstants.invalidConstant) {
            Competition competition =getCompetitionModel().getCompetitionFromID(getFixtureWrapper().getFixture().getCompetition_season_id());
            String teamColorString = competition.getLeague().getColor_1();
            teamColor = getViewHelper().getColor(teamColorString);
        }
        return teamColor;
    }

    public String getFixtureTitle() {

        Team homeTeam = getDataHelper().getTeam(getFixtureWrapper().getFixture().getHome_team_id());
        Team awayTeam = getDataHelper().getTeam(getFixtureWrapper().getFixture().getAway_team_id());
        return getTeamName(homeTeam) +getString(R.string.fixture_page_vs) + getTeamName(awayTeam);

    }

    private String getTeamName(Team team){
        if(team==null)
            return getResources().getString(R.string.fixture_page_tbd);
        else
            return team.getName();
    }

    public FixtureWrapper getFixtureWrapper() {
        return fixtureWrapper;
    }

    public void setFixtureWrapper(FixtureWrapper fixtureWrapper) {
        this.fixtureWrapper = fixtureWrapper;
    }

    @Override
    protected Toolbar setUpToolBar(View view) {

        setToolbar((Toolbar) view.findViewById(R.id.app_bar));
        if (getToolbar() != null) {

            getToolbar().setBackgroundColor(getTeamColor());
            getToolbar().setTitle(getFragmentTitle());
            getToolbar().setNavigationIcon(R.drawable.header_back_icon);
            setUpBackButtonClickListner();

        }
        return getToolbar();
    }

    private void setUpAllTextViews(){

        getResultTextView().setText(getFixtureWrapper().getDisplayableScore());
        getDateTextView().setText(getFixtureWrapper().getLongDisplayableDate());
        getTimeTextView().setText(getFixtureWrapper().getDisplayableTime());
        getLocationTextView().setText(getFixtureWrapper().getDisplayablePlace());
        getAddressTextView().setText((getFixtureWrapper().getDisplayableAddress()));
        setUpStatusText();

    }

    private void setUpStatusText(){
        MitooEnum.FixtureRowType fixtureRowType = getFixtureWrapper().getFixtureType();
        switch (fixtureRowType){
            case ABANDONED:
            case VOID:
            case DELETED:
                setUpRedStatus();
                break;
            case POSTPONED:
            case RESCHEDULE:
                setUpYellowStatus();
                break;
            case SCORE:
                break;
        }

    }

    private void setUpMap(){

        try{
            GoogleMap map = ((MapFragment) getFragmentManager()
                    .findFragmentById(R.id.googleFixtureMapFragment)).getMap();
            getViewHelper().setUpMap(getFixtureWrapper().getLatLng(), map );
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    googleMapAction();
                }
            });
        }
        catch(Exception e){
        }

    }

    private void setUpYellowStatus(){
        setUpStatusText(getResources().getColor(R.color.orange_light));
    }

    private void setUpRedStatus(){
        setUpStatusText(getResources().getColor(R.color.red_light));
    }

    private void setUpStatusText(int color){
        getStatusTextView().setVisibility(View.VISIBLE);
        getStatusTextView().setTextColor(color);
        getStatusTextView().setText(getFixtureWrapper().getFixtureType().name());
    }

    public TextView getResultTextView() {
        return resultTextView;
    }

    public void setResultTextView(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    public TextView getDateTextView() {
        return dateTextView;
    }

    public void setDateTextView(TextView dateTextView) {
        this.dateTextView = dateTextView;
    }

    public TextView getTimeTextView() {
        return timeTextView;
    }

    public void setTimeTextView(TextView timeTextView) {
        this.timeTextView = timeTextView;
    }

    public TextView getLocationTextView() {
        return locationTextView;
    }

    public void setLocationTextView(TextView locationTextView) {
        this.locationTextView = locationTextView;
    }

    public TextView getStatusTextView() {
        return statusTextView;
    }

    public void setStatusTextView(TextView statusTextView) {
        this.statusTextView = statusTextView;
    }

    public TextView getAddressTextView() {
        return addressTextView;
    }

    public void setAddressTextView(TextView addressTextView) {
        this.addressTextView = addressTextView;
    }

    private void googleMapAction(){
        LatLng latLng = getFixtureWrapper().getLatLng();
        String uri = String.format(Locale.ENGLISH, "geo:%1$,.2f,%2$,.2f", latLng.latitude, latLng.longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        getActivity().startActivity(intent);
    }
}
