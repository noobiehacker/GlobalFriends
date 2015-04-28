package co.mitoo.sashimi.views.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.models.jsonPojo.location;
import co.mitoo.sashimi.services.EventTrackingService;
import co.mitoo.sashimi.utils.BabushkaText;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.models.FixtureModel;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CompetitionSeasonReqByCompAndUserID;
import co.mitoo.sashimi.utils.events.CompetitionSeasonResponseEvent;
import co.mitoo.sashimi.utils.events.FixtureIndividualRequestEvent;
import co.mitoo.sashimi.utils.events.FixtureModelIndividualResponse;
import co.mitoo.sashimi.utils.events.FixtureNotificationUpdateResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.RainOutModel;
import co.mitoo.sashimi.utils.events.TeamIndividualRequestEvent;
import co.mitoo.sashimi.utils.events.TeamIndividualResponseEvent;
import co.mitoo.sashimi.utils.events.NotificationUpdateEvent;

/**
 * Created by david on 15-04-01.
 */
public class FixtureFragment extends MitooFragment {

    private int teamColor = MitooConstants.invalidConstant;
    private FixtureModel fixtureModel;
    private TextView resultTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView locationTextView;
    private TextView statusTextView;
    private TextView addressTextView;
    private View rainOutView;
    private int fixtureID = MitooConstants.invalidConstant;
    private int competitionSeasonID = MitooConstants.invalidConstant;
    private int homeTeamID = MitooConstants.invalidConstant;
    private int awayTeamID = MitooConstants.invalidConstant;
    private Team homeTeam;
    private Team awayTeam;
    private boolean viewLoaded = false;
    private String rainOutMessage;
    private String firstRainOutColor;
    private String secondRainOutColor;
    private RainOutModel rainOutModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initializeFixtureID(savedInstanceState);
        requestData();
    }

    private void initializeFixtureID(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            this.fixtureID = savedInstanceState.getInt(getFixtureIdKey());
        } else {
            this.fixtureID = getArguments().getInt(getFixtureIdKey());
        }

    }

    @Subscribe
    public void onNotificationRecieve(FixtureNotificationUpdateResponseEvent event) {
        this.fixtureModel = event.getFixtureModel();
        this.fixtureID = this.fixtureModel.getFixture().getId();
        this.competitionSeasonID = this.fixtureModel.getFixture().getCompetition_season_id();
        this.homeTeamID = this.fixtureModel.getFixture().getHome_team_id();
        this.awayTeamID = this.fixtureModel.getFixture().getAway_team_id();
        this.homeTeam=null;
        this.awayTeam=null;
        requestAddtionalData();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(getFixtureIdKey(), this.fixtureID);
        super.onSaveInstanceState(bundle);

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
    public void onPause() {

        try {
            MapFragment f = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.googleFixtureMapFragment);
            if (f != null)
                getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();

        } catch (Exception e) {

            Log.i("MitooFragmentException", e.getStackTrace().toString());

        } finally {

            super.onPause();

        }

    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        initializeAllTextViews(view);
        setPreDataLoading(true);
        this.rainOutView = (RelativeLayout) view.findViewById(R.id.rain_out_view);
        this.viewLoaded = true;
        updateViews();

    }

    @Override
    protected void initializeFields() {

        super.initializeFields();
        setFragmentTitle(getFixtureTitle());
    }

    @Override
    public void onResume() {

        // Track this event
        EventTrackingService.userViewedFixtureDetailsScreen(this.getUserID(), this.fixtureID, this.competitionSeasonID, 0);
        super.onResume();

    }

    @Override
    protected void handleNetworkError() {

        if (getProgressLayout() != null && allDataLoaded()) {

            centerProgressLayout();
            getProgressLayout().removeAllViews();
            getProgressLayout().addView(createNetworkFailureView());
        }

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);
    }

    @Override
    protected void handleHttpErrors(int statusCode) {

        if (statusCode == 404){
            //DO NOTHING
        }
        else
            super.handleHttpErrors(statusCode);
    }

    @Subscribe
    public void onFixtureResponse(FixtureModelIndividualResponse event) {

        this.fixtureModel = (event.getFixture());
        this.homeTeamID = event.getFixture().getFixture().getHome_team_id();
        this.awayTeamID = event.getFixture().getFixture().getAway_team_id();
        this.competitionSeasonID = event.getFixture().getFixture().getCompetition_season_id();
        requestAddtionalData();
    }

    @Override
    protected void requestData() {
        getFixtureModel();
        BusProvider.post(new FixtureIndividualRequestEvent(this.fixtureID));
    }

    protected void requestAddtionalData() {
        getTeamModel();
        getCompetitionModel();
        BusProvider.post(new TeamIndividualRequestEvent(this.homeTeamID, this.competitionSeasonID));
        BusProvider.post(new TeamIndividualRequestEvent(this.awayTeamID, this.competitionSeasonID));
        BusProvider.post(new CompetitionSeasonReqByCompAndUserID(this.competitionSeasonID, getUserID()));
    }

    @Subscribe
    public void onTeamResponse(TeamIndividualResponseEvent event) {

        Team newTeamObject = event.getTeam();
        if (newTeamObject == null) {
            newTeamObject = new Team();
            newTeamObject.setName(getString(R.string.fixture_page_tbd));
        }

        if (event.getTeamID() == this.homeTeamID)
            this.homeTeam = newTeamObject;
        if (event.getTeamID() == this.awayTeamID)
            this.awayTeam = newTeamObject;
        //BOTH TEAMS COULD HAVE SAME ID

        updateToolbar();
        updateViews();

    }

    @Subscribe
    public void onCompetitionResponse(CompetitionSeasonResponseEvent event) {

        this.teamColor = getViewHelper().getColor(event.getCompetition().getLeague().getColor_1());
        updateToolbar();
        Competition competition = event.getCompetition();
        RainOutModel model = new RainOutModel(competition.getRainOutMessage());
        this.rainOutModel = model;
        updateViews();

    }

    private void updateViews() {

        if (allDataLoaded()) {
            setUpAllTextViews();
            showAndHideLogic(getRootView());
            setUpMap();
            updateToolbar();
            setPreDataLoading(false);
            updateRainOutView(this.rainOutModel);

        }

    }

    private boolean allDataLoaded(){
        return this.rainOutModel !=null && this.viewLoaded && this.fixtureModel != null && teamNamesAreReady();
    }

    private void updateToolbar() {

        setFragmentTitle(getFixtureTitle());
        if (getToolbar() != null) {
            getToolbar().setTitle(getFragmentTitle());
            getToolbar().setBackgroundColor(this.teamColor);
        }

    }

    private void initializeAllTextViews(View view) {

        this.resultTextView = ((TextView) view.findViewById(R.id.fixtureResultText));
        this.dateTextView = ((TextView) view.findViewById(R.id.fixtureDateText));
        this.timeTextView = ((TextView) view.findViewById(R.id.fixtureTimeText));
        this.locationTextView = ((TextView) view.findViewById(R.id.fixtureLocationText));
        this.statusTextView = ((TextView) view.findViewById(R.id.fixtureStatusText));
        this.addressTextView = ((TextView) view.findViewById(R.id.fixtureAddressText));

    }

    private void showAndHideLogic(View view) {

        RelativeLayout locationLayout = (RelativeLayout) view.findViewById(R.id.fixture_bottom_details);
        RelativeLayout resultLayout = (RelativeLayout) view.findViewById(R.id.fixture_top_details);
        RelativeLayout mapFragmentContainer = (RelativeLayout) view.findViewById(R.id.googleMapFragmentContainer);

        location location = this.fixtureModel.getFixture().getLocation();
        String address = this.fixtureModel.getDisplayableAddress();
        String title = this.fixtureModel.getDisplayablePlace();

        boolean showLocation = (location != null);
        boolean showMap = validLatLng(location);
        boolean showAddress = (address != null && !address.equalsIgnoreCase(""));
        boolean showTitle = (title != null && !title.equalsIgnoreCase(""));
        boolean showResult = (this.fixtureModel.getFixture().getResult() != null);

        handleViewVisibility(locationLayout, showLocation);
        handleViewVisibility(mapFragmentContainer, showMap);
        handleViewVisibility(this.addressTextView, showAddress);
        handleViewVisibility(this.locationTextView, showTitle);
        handleViewVisibility(resultLayout, showResult);

        if (showTitle == false && showAddress == false) {
            RelativeLayout locationAndAddressContainer = (RelativeLayout) view.findViewById(R.id.locationAndAddressContainer);
            handleViewVisibility(locationAndAddressContainer, showTitle);
        }

    }

    private boolean validLatLng(location location) {

        boolean result = false;
        if (location != null) {
            if (location.getLat() != 0.0 || location.getLng() != 0.0)
                result = true;
        }
        return result;

    }

    public String getFixtureTitle() {

        if (this.homeTeam != null && this.awayTeam != null) {
            return getTeamName(this.homeTeam) + getString(R.string.fixture_page_vs) + getTeamName(this.awayTeam);
        } else {
            return getString(R.string.fixture_page_loading);
        }

    }

    private boolean teamNamesAreReady() {

        return this.homeTeam != null && this.awayTeam != null;
    }

    private String getTeamName(Team team) {
        if (team == null)
            return getResources().getString(R.string.fixture_page_tbd);
        else
            return team.getName();
    }


    @Override
    protected Toolbar setUpToolBar(View view) {

        setToolbar((Toolbar) view.findViewById(R.id.app_bar));
        if (getToolbar() != null) {

            getToolbar().setBackgroundColor(getMitooActivity().getResources().getColor(R.color.gray_dark_five));
            getToolbar().setTitle(getFragmentTitle());
            getToolbar().setNavigationIcon(R.drawable.header_back_icon);
            setUpBackButtonClickListner();

        }
        return getToolbar();
    }

    private void setUpAllTextViews() {

        this.resultTextView.setText(this.fixtureModel.getDisplayableScore());
        this.dateTextView.setText(this.fixtureModel.getLongDisplayableDate());
        this.locationTextView.setText(this.fixtureModel.getDisplayablePlace());
        this.addressTextView.setText((this.fixtureModel.getDisplayableAddress()));
        setUpStatusText();
        setUpTimeText();

    }


    private void setUpTimeText() {
        String time = this.fixtureModel.getDisplayableTime();
        if (time.equalsIgnoreCase(getString(R.string.fixture_page_tbd)))
            time = getString(R.string.fixture_page_time) + time;
        this.timeTextView.setText(time);
    }

    private void setUpStatusText() {
        MitooEnum.FixtureStatus fixtureStatus = this.fixtureModel.getFixtureType();
        switch (fixtureStatus) {
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

    private void setUpMap() {

        try {
            GoogleMap map = ((MapFragment) getFragmentManager()
                    .findFragmentById(R.id.googleFixtureMapFragment)).getMap();
            getViewHelper().setUpMap(this.fixtureModel.getLatLng(), map);
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    googleMapAction();
                }
            });
        } catch (Exception e) {
        }

    }

    private void setUpYellowStatus() {
        setUpStatusText(getResources().getColor(R.color.orange_light));
    }

    private void setUpRedStatus() {
        setUpStatusText(getResources().getColor(R.color.red_light));
    }

    private void setUpStatusText(int color) {
        this.statusTextView.setVisibility(View.VISIBLE);
        this.statusTextView.setTextColor(color);
        this.statusTextView.setText(this.fixtureModel.getFixtureType().name());
    }

    private void googleMapAction() {

        LatLng latLng = this.fixtureModel.getLatLng();
        Double lat = latLng.latitude;
        Double lng = latLng.longitude;
        String labelLocation = this.fixtureModel.getFixture().getLocation().getTitle();
        Uri uri = Uri.parse("geo:<" + lat + ">,<" + lng + ">?q=<" + lat + ">,<" + lng + ">(" + labelLocation + ")");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getActivity().startActivity(intent);

    }

    private String getFixtureIdKey() {
        return getString(R.string.bundle_key_fixture_id_key);
    }

    @Override
    public void onClick(View v) {
        if (getDataHelper().isClickable(v.getId())) {
            switch (v.getId()) {
                //Implement later
            }
        }
    }

    public void updateRainOutView(RainOutModel event) {

        if(this.rainOutView!=null &&this.rainOutModel.getRainOutMessage()!=null){

            this.rainOutMessage = this.rainOutModel.getRainOutMessage().getMessage();
            this.firstRainOutColor = parseRainOutColor(this.rainOutModel.getRainOutMessage().getColor());
            this.secondRainOutColor = "#" + getString(R.string.place_holder_color_two);

            //CUSTOMIZE TEXT

            BabushkaText text = (BabushkaText)rainOutView.findViewById(R.id.leagueMessage);
            text.reset();
            text.addPiece(new BabushkaText.Piece.Builder(getString(R.string.competition_page_league_message_prefix))
                    .textColor(Color.parseColor(this.firstRainOutColor))
                    .style(Typeface.BOLD)
                    .build());
            text.addPiece(new BabushkaText.Piece.Builder(this.rainOutMessage)
                    .textColor(Color.parseColor(this.firstRainOutColor))
                    .build());
            text.display();

            //CUSTOMIZE Background Color

            RelativeLayout borderLayout  = (RelativeLayout)rainOutView.findViewById(R.id.rain_out_view);
            RelativeLayout backgroundLayout = (RelativeLayout)borderLayout.findViewById(R.id.background_layout);
            borderLayout.setBackgroundColor(Color.parseColor(this.firstRainOutColor));
            backgroundLayout.setBackgroundColor(Color.parseColor(this.secondRainOutColor));
            this.rainOutView.setVisibility(View.VISIBLE);
        }else{
            this.rainOutView.setVisibility(View.GONE);

        }

    }

    private String parseRainOutColor(String color) {

        String returnColor =color;
        if (color != null) {

            if (color.length() == 7)
                returnColor = color;
            else if (color.length() == 6)
                returnColor =  "#" + color;

        } else {
            returnColor =  "#" + getString(R.string.place_holder_color_one);

        }
        return returnColor;
    }
}
