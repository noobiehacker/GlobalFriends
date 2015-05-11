package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidprogresslayout.ProgressLayout;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.events.AlgoliaLeagueSearchEvent;
import co.mitoo.sashimi.utils.events.BackGroundTaskCompleteEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.LocationRequestEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-01-19.
 */
public class SearchResultsFragment extends MitooFragment {

    private LinearLayout leagueListHolder;
    private List<LeagueModel> leagueData;
    private String searchText;
    private TextView noResultsView ;
    private boolean searchFlowComplete;
    private boolean viewLoaded;

    public static SearchResultsFragment newInstance() {

        return new SearchResultsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            this.searchText = savedInstanceState.getString(getToolBarTitle());
        }else{
            this.searchText  = getArguments().getString(getToolBarTitle());
        }
        setSearchFlowComplete(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStaste) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_search_results,
                container, false);
        initializeOnClickListeners(view);
        initializeViews(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(getToolBarTitle(), this.searchText);
        super.onSaveInstanceState(bundle);

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {
        super.onError(error);
    }


    @Override
    public void onResume() {

        super.onResume();
        if (searchFlowHasCopmleted() == false) {
            setLoading(true);
            BusProvider.post(new LocationRequestEvent());
        } else {
            updateViews();
        }

    }

    @Subscribe
    public void onLatLngRecieved(LatLng latLng) {

        if (getDataHelper().IsValidLatLng(latLng)) {
            BusProvider.post(new AlgoliaLeagueSearchEvent(this.searchText, latLng));
        } else {
            BusProvider.post(new AlgoliaLeagueSearchEvent(this.searchText));
        }
    }

    @Subscribe
    public void recieveLeagueResult(LeagueQueryResponseEvent event) {

        this.leagueData = event.getLeagueModels();
        updateViews();
        setSearchFlowComplete(true);
    }
    
    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        setUpListView(view);
        setUpNoResultsTextView(view);
        setFragmentTitle(this.searchText);
        if(getToolbar()!=null)
            getToolbar().setTitle(getFragmentTitle());
        this.viewLoaded = true;

    }

    private void updateViews(){

        if(this.leagueData !=null && this.viewLoaded){
            int leagueLayout = R.layout.view_league_dynamic_header;
            if (this.leagueData.size() > 0)
                getViewHelper().getLeagueViewHelper().addLeagueDataToList(this, leagueLayout,
                        getLeagueListHolder(), this.leagueData);
            else{
                getNoResultsView().setText(createNoResultsString());
                getNoResultsView().setVisibility(View.VISIBLE);
                setLoading(false);
            }
        }

    }

    @Override
    protected void initializeOnClickListeners(View view) {
        super.initializeOnClickListeners(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private String createNoResultsString() {

        return getString(R.string.results_page_text_1) + " " +
                getFragmentTitle() + " " +
                getString(R.string.results_page_text_2);
    }

    private void setUpListView(View view) {

        setLeagueListHolder((LinearLayout) view.findViewById(R.id.league_image_holder));

    }

    @Override
    public void tearDownReferences() {

        super.tearDownReferences();
    }

    public LinearLayout getLeagueListHolder() {
        return leagueListHolder;
    }

    public void setLeagueListHolder(LinearLayout leagueListHolder) {
        this.leagueListHolder = leagueListHolder;
    }

    @Override
    protected void removeDynamicViews() {
        if (getLeagueListHolder() != null)
            getLeagueListHolder().removeAllViews();
        super.removeDynamicViews();
    }

    public TextView getNoResultsView() {
        return noResultsView;
    }

    public void setNoResultsView(TextView noResultsView) {
        this.noResultsView = noResultsView;
    }

    private void setUpNoResultsTextView(View view){

        setNoResultsView((TextView)view.findViewById(R.id.noResultsTextView));

    }

    public boolean searchFlowHasCopmleted() {
        return searchFlowComplete;
    }

    public void setSearchFlowComplete(boolean searchFlowComplete) {
        this.searchFlowComplete = searchFlowComplete;
    }

    @Subscribe
    public void iconBackgroundTasksComplete(BackGroundTaskCompleteEvent event) {
        setLoading(false);
    }
}
