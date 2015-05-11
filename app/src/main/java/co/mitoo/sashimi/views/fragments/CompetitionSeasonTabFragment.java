package co.mitoo.sashimi.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.androidprogresslayout.ProgressLayout;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.models.FixtureModel;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FixtureListRequestEvent;
import co.mitoo.sashimi.utils.events.FixtureListResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.TeamListRequestEvent;
import co.mitoo.sashimi.utils.events.TeamListResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.adapters.FixtureListAdapter;

/**
 * Created by david on 15-03-08.
 */

public class CompetitionSeasonTabFragment extends MitooFragment {

    private MitooEnum.FixtureTabType tabType;
    private TextView noResultsTextView;
    private MitooActivity mitooActivity;
    private ListView fixtureListView;
    private FixtureListAdapter fixtureListAdapter;
    private List<FixtureModel> fixtureList;
    private boolean viewLoaded = false;
    private int competitionSeasonID;
    private List<Team> teams;
    private int tabIndex = 0;
    private boolean dataLoaded = false;
    private boolean fragmentStarted = false;

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
        BusProvider.post(new FixtureListRequestEvent(getTabType(), this.competitionSeasonID));
        BusProvider.post(new TeamListRequestEvent(this.competitionSeasonID));

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
        setFixtureListView((ListView) view.findViewById(R.id.fixture_list_view));
        if(allDataLoaded()==false)
            setPreDataLoading(true);
        this.viewLoaded = true;
        updateView();
    }

    private void updateView() {

        if (allDataLoaded()) {
            getFixtureListView().setAdapter(getFixtureListAdapter());
            setUpNoResultsView();
            setPreDataLoading(false);
        }

    }

    private boolean allDataLoaded(){
        return fixtureListRecieved() && this.viewLoaded && teamDataLoaded();
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

    public ListView getFixtureListView() {

        return fixtureListView;
    }

    public void setFixtureListView(ListView fixtureListView) {
        this.fixtureListView = fixtureListView;
    }

    public FixtureListAdapter getFixtureListAdapter() {
        if (fixtureListAdapter == null)
            fixtureListAdapter = new FixtureListAdapter(getActivity(), R.id.fixture_list_view,
                    getFixtureList(), this);
        return fixtureListAdapter;
    }

    public List<FixtureModel> getFixtureList() {
        if (fixtureList == null)
            fixtureList = new ArrayList<FixtureModel>();
        return fixtureList;
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

    private boolean fixtureListRecieved() {
        return getFixtureList() != null && !getFixtureList().isEmpty();
    }

    private String getTabIndexKey() {
        return getString(R.string.bundle_key_tab_index_key);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(getCompetitionSeasonIdKey(), this.competitionSeasonID);
        bundle.putInt(getTabIndexKey(), this.tabIndex);
        super.onSaveInstanceState(bundle);

    }

    private boolean teamDataLoaded() {
        return this.teams != null;
    }


}
