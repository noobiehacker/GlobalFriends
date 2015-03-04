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
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.events.AlgoliaLeagueSearchEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-01-19.
 */
public class SearchResultsFragment extends MitooFragment {

    private LinearLayout leagueListHolder;
    private List<League> leagueData;
    private String searchText;
    private TextView noResultsView ;
    private boolean searchFlowComplete;

    public static SearchResultsFragment newInstance() {

        return new SearchResultsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setSearchFlowComplete(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStaste) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_search_results,
                container, false);
        initializeOnClickListeners(view);
        initializeFields(savedInstanceStaste);
        initializeViews(view);
        return view;
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {
        super.onError(error);
    }

    private void initializeFields(Bundle savedInstanceState) {

        super.initializeFields();
        Bundle arguments = getArguments();
        setSearchText(arguments.get(getString(R.string.bundle_key_tool_bar_title)).toString());
        setFragmentTitle(getSearchText());
        setLeagueData(new ArrayList<League>());
        updateLeagueDataResult();
    }

    @Override
    public void onResume() {

        super.onResume();
        if(searchFlowHasCopmleted()==false){
            setLoading(true);
            getLocationModel().requestSelectedLocationLatLng();
        }else{
            updateViews();
        }

    }

    @Subscribe
    public void onLatLngRecieved(LatLng latLng) {

        if (getDataHelper().IsValidLatLng(latLng)) {
            getLeagueModel().requestAlgoLiaSearch(new AlgoliaLeagueSearchEvent(getSearchText(), latLng));
        } else {
            getLeagueModel().requestAlgoLiaSearch(new AlgoliaLeagueSearchEvent(getSearchText()));
        }
    }

    @Subscribe
    public void recieveLeagueResult(LeagueQueryResponseEvent event) {

        updateLeagueDataResult();
        updateViews();
        setSearchFlowComplete(true);
    }
    
    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        setUpListView(view);
        setUpNoResultsTextView(view);

    }

    public void updateLeagueDataResult() {

        if (getLeagueModel().getLeagueSearchResults() != null) {
            DataHelper dataHelper = new DataHelper(getMitooActivity());
            dataHelper.clearList(getLeagueData());
            dataHelper.addToListList(getLeagueData(), getLeagueModel().getLeagueSearchResults());
        }
    }

    private void updateViews(){

        int leagueLayout = R.layout.view_league_dynamic_header;
        if (getLeagueData().size() > 0)
            getViewHelper().addLeagueDataToList(this, leagueLayout, 
                    getLeagueListHolder(), getLeagueData());
        else{
            getNoResultsView().setText(createNoResultsString());
            getNoResultsView().setVisibility(View.VISIBLE);
        }
       
        setLoading(false);

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

    public List<League> getLeagueData() {
        if (leagueData == null) {
            setLeagueData(new ArrayList<League>());
        }
        return leagueData;
    }

    public void setLeagueData(List<League> leagueData) {
        this.leagueData = leagueData;
    }

    private void setUpListView(View view) {

        setLeagueListHolder((LinearLayout) view.findViewById(R.id.league_image_holder));

    }

    @Override
    public void tearDownReferences() {

   //     removeDynamicViews();
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
 

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public void setLoading(boolean loading) {
        this.loading = loading;
        if (this.loading) {
            getProgressLayout().showProgress();
        } else {
            getProgressLayout().showContent();
        }
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
}
