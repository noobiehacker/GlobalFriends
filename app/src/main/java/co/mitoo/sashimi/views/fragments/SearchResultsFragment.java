package co.mitoo.sashimi.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.Sport;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.adapters.LeagueAdapter;
import co.mitoo.sashimi.views.adapters.SearchableAdapter;

/**
 * Created by david on 15-01-19.
 */
public class SearchResultsFragment extends MitooFragment  {

    private ListView leagueList;
    private LeagueAdapter leagueDataAdapter;
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
        setUpLeagueData();
        updateLeagueDataResult();
    }

    public void updateLeagueDataResult(){

        if(getLeagueModel().getLeagueSearchResults()!=null){
            DataHelper dataHelper = new DataHelper(getMitooActivity());
            dataHelper.clearList(leagueData);
            dataHelper.addToListList(this.leagueData, getLeagueModel().getLeagueSearchResults());
            this.leagueDataAdapter.notifyDataSetChanged();
        }
    }

    private void setUpLeagueData(){

        if(leagueData==null){
            leagueData = new ArrayList<League>();
            leagueDataAdapter = new LeagueAdapter(getActivity(),R.id.leagueListView,leagueData , this);
        }

    }
    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        leagueList = (ListView) view.findViewById(R.id.leagueListView);
        leagueList.setAdapter(leagueDataAdapter);
        leagueList.setOnItemClickListener(leagueDataAdapter);

    }

    private void initializeOnClickListeners(View view){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


}
