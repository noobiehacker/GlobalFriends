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
import com.github.androidprogresslayout.ProgressLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;
import java.util.Locale;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.models.jsonPojo.location;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationRecieve;
import co.mitoo.sashimi.utils.FixtureWrapper;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CompetitionModelResponseEvent;
import co.mitoo.sashimi.utils.events.FixtureModelIndividualResponse;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.NotificationEvent;
import co.mitoo.sashimi.utils.events.NotificationUpdateEvent;
import co.mitoo.sashimi.utils.events.TeamModelResponseEvent;

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
    private NotificationRecieve notificationRecieve;
    private boolean fixturePageLoaded;
    private boolean competitionResponseRecieved;
    private boolean teamResponseRecieved;
    private boolean fixtureListResponseRecieved;

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable(v.getId())){
            switch (v.getId()) {
                //Implement later
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_fixture,
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
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        initializeAllTextViews(view);
    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        initalizeNotificationRecieved();
        initializeFixture();
        setFragmentTitle(getFixtureTitle());
        setFixturePageLoaded(false);
    }

    private void initializeFixture(){
        if(getNotificationRecieve()!=null)
            setFixtureWrapper(null);
        else
            setFixtureWrapper(getFixtureModel().getSelectedFixture());

    }

    @Override
    public void onResume(){

        super.onResume();
        handleAndDisplayFixtureData();

    }

    private void handleAndDisplayFixtureData(){

        if(fixtureDataIsReady()){
            updateViews();
        }
        else{
            requestData();
            setPreDataLoading(true);
        }
    }

    @Override
    protected void handleNetworkError() {

        if (getProgressLayout() != null) {

            centerProgressLayout();
            getProgressLayout().removeAllViews();
            getProgressLayout().addView(createNetworkFailureView());
        }

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    @Override
    protected void handleHttpErrors(int statusCode) {

        if(statusCode == 404)
            routeToHome();
        else
            super.handleHttpErrors(statusCode);
    }

    @Subscribe
    public void onFixtureResponse(FixtureModelIndividualResponse event) {

        handleFixtureResponse();

    }

    private void handleFixtureResponse(){
        setPreDataLoading(false);
        if(getNotificationRecieve()!=null){

            int fixtureID = Integer.parseInt(getNotificationRecieve().getObj_id());
            setFixtureWrapper(getFixtureModel().getFixtureFromModel(fixtureID));

        }else{
            setFixtureWrapper(getFixtureModel().getSelectedFixture());

        }
        updateViews();
    }

    @Subscribe
    public void onTeamResponse(TeamModelResponseEvent event) {

        updateToolbarTitle();

    }

    @Subscribe
    public void onCompetitionResponse(CompetitionModelResponseEvent event) {

        updateSelectedCompetition();

    }

    private void updateViews(){

        setUpAllTextViews();
        showAndHideLogic(getRootView());
        setUpMap();
        if(notificationRecieved())
            requestAdditionalData();
        updateSelectedCompetition();
        updateToolbarTitle();

    }

    private void requestAdditionalData(){

        int competitionSeasonID = getFixtureWrapper().getFixture().getCompetition_season_id();
        int userID = getSessionModel().getSession().id;
        if(!isTeamResponseRecieved())
            getTeamModel().requestTeamByCompetition(competitionSeasonID, true);
        if(!isCompetitionResponseRecieved())
            getCompetitionModel().requestCompetition(userID);
        getFixtureModel().requestFixtureByCompetition(competitionSeasonID, true);

    }

    private void updateToolbarTitle(){

        setFragmentTitle(getFixtureTitle());
        getToolbar().setTitle(getFragmentTitle());

    }

    private void updateSelectedCompetition(){

        if(fixtureDataIsReady()){
            int competitionSeasonID = getFixtureWrapper().getFixture().getCompetition_season_id();
            Competition competition = getCompetitionModel().getCompetitionFromID(competitionSeasonID);
            if(competition!=null){
                getCompetitionModel().setSelectedCompetition(competitionSeasonID);
                getToolbar().setBackgroundColor(getTeamColor());
            }
        }

    }

    private void initializeAllTextViews(View view){

        setResultTextView((TextView) view.findViewById(R.id.fixtureResultText));
        setDateTextView((TextView) view.findViewById(R.id.fixtureDateText));
        setTimeTextView((TextView) view.findViewById(R.id.fixtureTimeText));
        setLocationTextView((TextView) view.findViewById(R.id.fixtureLocationText));
        setStatusTextView((TextView) view.findViewById(R.id.fixtureStatusText));
        setAddressTextView((TextView) view.findViewById(R.id.fixtureAddressText));

    }

    private void showAndHideLogic(View view) {

        RelativeLayout locationLayout = (RelativeLayout) view.findViewById(R.id.fixture_bottom_details);
        RelativeLayout resultLayout = (RelativeLayout) view.findViewById(R.id.fixture_top_details);
        RelativeLayout mapFragmentContainer = (RelativeLayout) view.findViewById(R.id.googleMapFragmentContainer);

        location location = getFixtureWrapper().getFixture().getLocation();
        String address = getFixtureWrapper().getDisplayableAddress();
        String title = getFixtureWrapper().getDisplayablePlace();

        boolean showLocation = (location != null) ;
        boolean showMap =  validLatLng(location);
        boolean showAddress =(address != null && !address.equalsIgnoreCase(""));
        boolean showTitle = (title != null && !title.equalsIgnoreCase(""));
        boolean showResult = (getFixtureWrapper().getFixture().getResult() != null) ;

        handleViewVisibility(locationLayout,showLocation);
        handleViewVisibility(mapFragmentContainer,showMap);
        handleViewVisibility(getAddressTextView(),showAddress);
        handleViewVisibility(getLocationTextView(),showTitle);
        handleViewVisibility(resultLayout,showResult);

        if(showTitle == false && showAddress == false){
            RelativeLayout locationAndAddressContainer = (RelativeLayout) view.findViewById(R.id.locationAndAddressContainer);
            handleViewVisibility(locationAndAddressContainer,showTitle);
        }

    }

    private boolean validLatLng(location location){

        boolean result = false;
        if(location!=null){
            if(location.getLat()!= 0.0 || location.getLng()!=0.0)
                result = true;
        }
        return result;

    }

    public int getTeamColor() {
        Competition competition =getCompetitionModel().getSelectedCompetition();
        if(competition!=null){
                String teamColorString = competition.getLeague().getColor_1();
                teamColor = getViewHelper().getColor(teamColorString);
            }
            else{
                teamColor = getMitooActivity().getResources().getColor(R.color.gray_dark_five);
            }
        return teamColor;
    }

    public String getFixtureTitle() {

        if(fixtureDataIsReady()){
            Team homeTeam = getDataHelper().getTeam(getFixtureWrapper().getFixture().getHome_team_id());
            Team awayTeam = getDataHelper().getTeam(getFixtureWrapper().getFixture().getAway_team_id());
            return getTeamName(homeTeam) +getString(R.string.fixture_page_vs) + getTeamName(awayTeam);
        }else{
            return getString(R.string.fixture_page_loading);
        }

    }

    private boolean areTeamNamesReady() {

        boolean result = false;
        if (getFixtureWrapper() != null) {
            Team homeTeam = getDataHelper().getTeam(getFixtureWrapper().getFixture().getHome_team_id());
            Team awayTeam = getDataHelper().getTeam(getFixtureWrapper().getFixture().getAway_team_id());
            if (homeTeam != null && awayTeam != null)
                result = true;
        }
        return result;
    }

    private boolean isCompetitionSeasonReady(){

        boolean result = false;
        int competitionSeasonID = getFixtureIDFromNotifcation();
        if (getFixtureWrapper() != null) {
            Team homeTeam = getDataHelper().getTeam(getFixtureWrapper().getFixture().getHome_team_id());
            Team awayTeam = getDataHelper().getTeam(getFixtureWrapper().getFixture().getAway_team_id());
            if (homeTeam != null && awayTeam != null)
                result = true;
        }
        return result;
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
        getLocationTextView().setText(getFixtureWrapper().getDisplayablePlace());
        getAddressTextView().setText((getFixtureWrapper().getDisplayableAddress()));
        setUpStatusText();
        setUpTimeText();

    }


    private void setUpTimeText(){
        String time = getFixtureWrapper().getDisplayableTime();
        if(time.equalsIgnoreCase(getString(R.string.fixture_page_tbd)))
            time =getString(R.string.fixture_page_time) + time;
        getTimeTextView().setText(time);
    }

    private void setUpStatusText(){
        MitooEnum.FixtureStatus fixtureStatus = getFixtureWrapper().getFixtureType();
        switch (fixtureStatus){
            case ABANDONED:
            case VOID:
            case DELETED:
            case CANCELED:
                setUpRedStatus();
                break;
            case POSTPONED:
            case RESCHEDULED:
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

    private int getFixtureIDFromNotifcation(){

        int result = MitooConstants.invalidConstant;
        if(getNotificationRecieve()!=null){
            String fixtureID = getNotificationRecieve().getObj_id();
            if(fixtureID!=null)
                result= Integer.parseInt(getNotificationRecieve().getObj_id());
        }
        return result;

    }

    private void setUpRedStatus(){
        setUpStatusText(getResources().getColor(R.color.red_light));
    }

    private void setUpStatusText(int color){
        getStatusTextView().setVisibility(View.VISIBLE);
        getStatusTextView().setTextColor(color);
        getStatusTextView().setText(getFixtureWrapper().getFixtureType().name());
    }

    @Subscribe
    public void onNotificationRecieve(NotificationUpdateEvent event){
        setNotificationRecieve(event.getNotificationRecieve());
        setFixtureWrapper(null);
        handleAndDisplayFixtureData();
    }

    private boolean fixtureDataIsReady(){
        return getFixtureWrapper()!=null;
    }

    @Override
    protected void requestData(){
        int fixtureID =getFixtureIDFromNotifcation();
        getFixtureModel().requestFixtureByFixtureID(fixtureID, true);
    }

    private boolean notificationRecieved(){
        return getNotificationRecieve()!=null;
    }

    public NotificationRecieve getNotificationRecieve() {
        return notificationRecieve;
    }

    public void setNotificationRecieve(NotificationRecieve notificationRecieve) {
        this.notificationRecieve = notificationRecieve;
    }

    private void initalizeNotificationRecieved(){

        try {
            Bundle bundle =getArguments();
            String bundleValue = (String) bundle.get(getString(R.string.bundle_key_notification));
            if(bundleValue!=null && bundleValue!="") {

                setNotificationRecieve(getDataHelper().deserializeObject(bundleValue, NotificationRecieve.class));
            }
        }catch(Exception e) {
        }

    }

    private void googleMapAction(){

        LatLng latLng = getFixtureWrapper().getLatLng();
        Double lat = latLng.latitude;
        Double lng =  latLng.longitude;
        String labelLocation = getFixtureWrapper().getFixture().getLocation().getTitle();
        Uri uri = Uri.parse("geo:<" + lat  + ">,<" + lng+ ">?q=<" + lat + ">,<" + lng + ">(" + labelLocation + ")");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getActivity().startActivity(intent);

    }

    private Competition getCompetition(int id){
        return getCompetitionModel().getCompetitionFromID(id);
    }

    public boolean isFixturePageLoaded() {
        return fixturePageLoaded;
    }

    public void setFixturePageLoaded(boolean fixturePageLoaded) {
        this.fixturePageLoaded = fixturePageLoaded;
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

    public boolean isCompetitionResponseRecieved() {
        return competitionResponseRecieved;
    }

    public void setCompetitionResponseRecieved(boolean competitionResponseRecieved) {
        this.competitionResponseRecieved = competitionResponseRecieved;
    }

    public boolean isTeamResponseRecieved() {
        return teamResponseRecieved;
    }

    public void setTeamResponseRecieved(boolean teamResponseRecieved) {
        this.teamResponseRecieved = teamResponseRecieved;
    }

    public boolean isFixtureListResponseRecieved() {
        return fixtureListResponseRecieved;
    }

    public void setFixtureListResponseRecieved(boolean fixtureListResponseRecieved) {
        this.fixtureListResponseRecieved = fixtureListResponseRecieved;
    }
}
