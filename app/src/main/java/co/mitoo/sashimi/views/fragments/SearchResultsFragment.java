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
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.Sport;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LeagueResultRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueResultResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.adapters.LeagueAdapter;
import co.mitoo.sashimi.views.adapters.SearchableAdapter;

/**
 * Created by david on 15-01-19.
 */
public class SearchResultsFragment extends MitooFragment implements AdapterView.OnItemClickListener {

    private ListView leagueList;
    private ListView sportsList;
    private LeagueAdapter leagueDataAdapter;
    private SearchableAdapter sportsDataAdapter;
    private List<League> leagueData ;
    private List<Sport> sportData ;
    private boolean filterOn = false;
    private boolean sportSelectionOn = false;

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

        if(getLeagueModel().getLeagueResults()!=null){
            clearList(leagueData);
            addToListList(this.leagueData, getLeagueModel().getLeagueResults());
            this.leagueDataAdapter.notifyDataSetChanged();
        }
    }

    private void setUpLeagueData(){

        if(leagueData==null){
            leagueData = new ArrayList<League>();
            leagueDataAdapter = new LeagueAdapter(getActivity(),R.id.leagueListView,leagueData);
        }

    }
    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        leagueList = (ListView) view.findViewById(R.id.leagueListView);
        leagueList.setAdapter(leagueDataAdapter);
        leagueList.setOnItemClickListener(this);

    }

    private void initializeOnClickListeners(View view){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        if(parent.getId() == leagueList.getId())
        {
            leagueListItemAction((League)leagueList.getItemAtPosition(position));
        }
    }

    private void leagueListItemAction(League league){

        getRetriever().getLeagueModel().setSelectedLeague(league);
        fireFragmentChangeAction(R.id.fragment_league);

    }

    private synchronized <T> void addToListList(List<T>container ,List<T> additionList){
        for(T item : additionList){
            container.add(item);
        }
    }

    private synchronized <T> void clearList(List<T> result){
        if(result!=null && result.size() >0){
            
            Iterator<T> iterator = result.iterator();
            while(iterator.hasNext()){
                iterator.next();
                iterator.remove();
            }

        }
    }

    private LeagueModel getLeagueModel(){

        return (LeagueModel) getMitooModel(LeagueModel.class);
    }


}
