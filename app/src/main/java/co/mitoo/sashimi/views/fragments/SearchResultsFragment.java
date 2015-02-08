package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-01-19.
 */
public class SearchResultsFragment extends MitooFragment  {

    private LinearLayout leagueListHolder;
    private List<League> leagueData ;

    public static SearchResultsFragment newInstance() {

        return new SearchResultsFragment();
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
    public void onError(MitooActivitiesErrorEvent error){
        super.onError(error);
    }

    private void initializeFields(Bundle savedInstanceState){

        super.initializeFields();
        Bundle arguments = getArguments();
        setFragmentTitle(arguments.get(getString(R.string.bundle_key_tool_bar_title)).toString());
        setLeagueData(new ArrayList<League>());
        updateLeagueDataResult();
    }

    public void updateLeagueDataResult(){

        if(getLeagueModel().getLeagueSearchResults()!=null){
            DataHelper dataHelper = new DataHelper(getMitooActivity());
            dataHelper.clearList(leagueData);
            dataHelper.addToListList(this.leagueData, getLeagueModel().getLeagueSearchResults());
        }
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        if(leagueData.size() >0)
            setUpListView(view);
        else
            setUpNoResultsTextView(view);
    }

    @Override
    protected void initializeOnClickListeners(View view){
        super.initializeOnClickListeners(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
    
    private void setUpNoResultsTextView(View view){
        
        TextView noResultsView = (TextView) view.findViewById(R.id.noResultsTextView);
        String noReultsText = createNoResultsString();
        noResultsView.setText(noReultsText);
        noResultsView.setVisibility(View.VISIBLE);
    }

    private String createNoResultsString(){
        
        return getString(R.string.results_page_text_1) + " " +
               getFragmentTitle() + " " +
               getString(R.string.results_page_text_2);
    }

    public List<League> getLeagueData() {
        if (leagueData == null) {
            setLeagueData(new ArrayList<League>());
            updateLeagueDataResult();
        }
        return leagueData;
    }

    public void setLeagueData(List<League> leagueData) {
        this.leagueData = leagueData;
    }

    private void setUpListView(View view){

        int leagueLayout = R.layout.list_view_item_league;
        setLeagueListHolder((LinearLayout) view.findViewById(R.id.league_image_holder));
        getViewHelper().addLeagueDataToList(this, leagueLayout, getLeagueListHolder(), getLeagueData());

    }
    

    
    @Override
    public void tearDownReferences(){

        getRootView().removeAllViews();
        super.tearDownReferences();
    }

    public LinearLayout getLeagueListHolder() {
        return leagueListHolder;
    }

    public void setLeagueListHolder(LinearLayout leagueListHolder) {
        this.leagueListHolder = leagueListHolder;
    }
}
