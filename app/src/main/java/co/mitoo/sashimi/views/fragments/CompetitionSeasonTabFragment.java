package co.mitoo.sashimi.views.fragments;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.androidprogresslayout.ProgressLayout;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.BabushkaText;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.models.FixtureModel;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CompetitionSeasonReqByCompAndUserID;
import co.mitoo.sashimi.utils.events.CompetitionSeasonResponseEvent;
import co.mitoo.sashimi.utils.events.FixtureListRequestEvent;
import co.mitoo.sashimi.utils.events.FixtureListResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.RainOut;
import co.mitoo.sashimi.utils.events.TeamListRequestEvent;
import co.mitoo.sashimi.utils.events.TeamListResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.adapters.FixtureListAdapter;
import co.mitoo.sashimi.views.widgets.HeaderListView;

/**
 * Created by david on 15-03-08.
 */

public class CompetitionSeasonTabFragment extends MitooFragment {

    private MitooEnum.FixtureTabType tabType;
    private TextView noResultsTextView;
    private MitooActivity mitooActivity;
    private HeaderListView fixtureListView;
    private FixtureListAdapter fixtureListAdapter;
    private List<FixtureModel> fixtureList;
    private boolean viewLoaded = false;
    private int competitionSeasonID;
    private List<Team> teams;
    private int tabIndex = 0;
    private boolean dataLoaded = false;
    private boolean fragmentStarted = false;
    private String rainOutMessage;
    private String firstRainOutColor;
    private String secondRainOutColor;
    private RainOut rainOut;

    @Override
    public void onClick(View v) {
    }

    public static CompetitionSeasonTabFragment newInstance() {
        CompetitionSeasonTabFragment fragment = new CompetitionSeasonTabFragment();
        return fragment;
    }

    public static CompetitionSeasonTabFragment newInstance(MitooEnum.FixtureTabType tabType) {
        CompetitionSeasonTabFragment fragment = new CompetitionSeasonTabFragment();
        fragment.setTabType(tabType);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MitooActivity)
            setMitooActivity((MitooActivity) activity);
    }

    @Override
    protected void handleHttpErrors(int statusCode) {

        if (statusCode == 404){
            //DO NOTHING
        }
        else
            super.handleHttpErrors(statusCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.competitionSeasonID = (int) savedInstanceState.get(getCompetitionSeasonIdKey());
            this.tabIndex = savedInstanceState.getInt(getTabIndexKey());
            this.rainOutMessage = savedInstanceState.getString(getRainOutKey());
            this.firstRainOutColor = savedInstanceState.getString(getFirstRainOutColorKey());
            this.secondRainOutColor = savedInstanceState.getString(getSecondRainOutColorKey());
            this.fragmentStarted = true;
            setTabType(this.tabIndex);
        } else {
            this.competitionSeasonID = getArguments().getInt(getCompetitionSeasonIdKey());

        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (allDataLoaded()==false){
            requestData();
        }
        updateView();
    }

    @Override
    public void requestData() {
        getFixtureModel();
        getTeamModel();
        getCompetitionModel();
        BusProvider.post(new FixtureListRequestEvent(getTabType(), this.competitionSeasonID));
        BusProvider.post(new TeamListRequestEvent(this.competitionSeasonID));
        BusProvider.post(new CompetitionSeasonReqByCompAndUserID(this.competitionSeasonID, getUserID()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_competition_tab,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        setNoResultsTextView((TextView) view.findViewById(R.id.noFixturesTextView));
        setFixtureListView((HeaderListView) view.findViewById(R.id.fixture_list_view));
        if(this.getTabType()== MitooEnum.FixtureTabType.FIXTURE_SCHEDULE)
            setUpRainOutHeader(getFixtureListView());
        if(allDataLoaded()==false)
            setPreDataLoading(true);
        this.viewLoaded = true;
        updateView();
    }

    private void updateView() {

        if (allDataLoaded()) {
            getFixtureListView().setAdapter(getFixtureListAdapter());
            setUpNoResultsView();
            updateRainOut(this.rainOut);
            setPreDataLoading(false);
        }
    }

    private boolean allDataLoaded(){

        boolean rainOutDataReady = true;
        switch (getTabType()){
            case FIXTURE_RESULT:
                rainOutDataReady = true;
                break;
            case FIXTURE_SCHEDULE:
                rainOutDataReady = this.rainOut !=null;
                break;
        }
        return fixtureListRecieved() && this.viewLoaded && teamDataLoaded() && (rainOutDataReady);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
    }

    @Subscribe
    public void onTeamDataLoaded(TeamListResponseEvent event) {
        if (!teamDataLoaded()) {
            this.teams = event.getLsitOfTeams();
            updateView();
        }
    }

    public TextView getNoResultsTextView() {
        return noResultsTextView;
    }

    public void setNoResultsTextView(TextView noResultsTextView) {
        this.noResultsTextView = noResultsTextView;
    }

    private void setUpNoResultsView() {

        if (this.fixtureList != null && this.fixtureList.isEmpty()) {
            getNoResultsTextView().setVisibility(View.VISIBLE);
            if (getTabType() == MitooEnum.FixtureTabType.FIXTURE_RESULT)
                getNoResultsTextView().setText(getString(R.string.fixture_page_no_results));
            else
                getNoResultsTextView().setText(getString(R.string.fixture_page_no_up_coming_games));
        }

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);
    }

    public MitooEnum.FixtureTabType getTabType() {
        if (tabType == null)
            setTabType(MitooEnum.FixtureTabType.FIXTURE_SCHEDULE);
        return tabType;
    }

    public void setTabType(MitooEnum.FixtureTabType tabType) {
        this.tabType = tabType;
        if (tabType == MitooEnum.FixtureTabType.FIXTURE_RESULT)
            this.tabIndex = 1;
        if (tabType == MitooEnum.FixtureTabType.FIXTURE_SCHEDULE)
            this.tabIndex = 0;

    }

    public void setTabType(int tabIndex) {

        if (tabIndex == 0)
            this.tabType = MitooEnum.FixtureTabType.FIXTURE_SCHEDULE;
        if (tabIndex == 1)
            this.tabType = MitooEnum.FixtureTabType.FIXTURE_RESULT;

    }

    @Override
    public MitooActivity getMitooActivity() {
        if (mitooActivity != null)
            return mitooActivity;
        else
            return super.getMitooActivity();
    }

    public void setMitooActivity(MitooActivity mitooActivity) {
        this.mitooActivity = mitooActivity;
    }

    public HeaderListView getFixtureListView() {

        return fixtureListView;
    }

    public void setFixtureListView(HeaderListView fixtureListView) {
        this.fixtureListView = fixtureListView;
    }

    public FixtureListAdapter getFixtureListAdapter() {
        if (fixtureListAdapter == null)
            fixtureListAdapter = new FixtureListAdapter(getActivity(), R.id.fixture_list_view,
                    new ArrayList<FixtureModel>(), this);
        return fixtureListAdapter;
    }

    @Subscribe
    public void onFixtureResponse(FixtureListResponseEvent event) {

        if (getTabType() == event.getTabType() && !fixtureListRecieved()) {
            this.fixtureList = event.getFixtureList();
            this.fixtureListAdapter = new FixtureListAdapter(getActivity(), R.id.fixture_list_view,
                    this.fixtureList, this);
            this.dataLoaded = true;
            updateView();
        }

    }

    public void updateRainOut(RainOut event) {

        if(this.getTabType()== MitooEnum.FixtureTabType.FIXTURE_SCHEDULE &&
                getFixtureListView()!= null &&
                getFixtureListView().getHeaderView()!=null){

            View rainOutView = getFixtureListView().getHeaderView();
            this.rainOutMessage = event.getRainOutMessage();
            this.firstRainOutColor = event.getFirstColor();
            this.secondRainOutColor = event.getSecondColor();

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
        }

    }

    private boolean fixtureListRecieved() {
        return this.fixtureList != null;
    }

    private String getTabIndexKey() {
        return getString(R.string.bundle_key_tab_index_key);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(getCompetitionSeasonIdKey(), this.competitionSeasonID);
        bundle.putInt(getTabIndexKey(), this.tabIndex);
        bundle.putString(getRainOutKey(), this.rainOutMessage);
        bundle.putString(getFirstRainOutColorKey(), this.firstRainOutColor);
        bundle.putString(getSecondRainOutColorKey(), this.secondRainOutColor);
        super.onSaveInstanceState(bundle);

    }

    private boolean teamDataLoaded() {
        return this.teams != null;
    }

    private void setUpRainOutHeader(HeaderListView listView){

        View headerView = getViewHelper().createViewFromInflator(R.layout.view_rainout_header);
        listView.setHeaderView(headerView);
        listView.addHeaderView(headerView);

    }

    @Subscribe
    public void onCompetitionLoaded(CompetitionSeasonResponseEvent event) {
        //TODO: REFACTOR
        /**
         *
         *Hard Coding, change later
         *
         */
        if(this.getTabType()== MitooEnum.FixtureTabType.FIXTURE_SCHEDULE){

            Competition competition = event.getCompetition();

            String rainOutMessage = competition.getRain_out_message();
            rainOutMessage = (rainOutMessage!=null)  ? rainOutMessage : getString(R.string.place_holder_rain_out);

            League league = competition.getLeague();

            String firstColorMessage =  league.getColor_1();
            firstColorMessage = (firstColorMessage!= null) ?"#" + firstColorMessage : "#" +getString(R.string.place_holder_color_one);

            String secondColorMessage = league.getColor_2();
            secondColorMessage = (secondColorMessage!= null) ?"#" + secondColorMessage :"#" +getString(R.string.place_holder_color_two);

            this.rainOut = new RainOut(rainOutMessage, firstColorMessage, secondColorMessage);
        }
        updateView();

    }

}
