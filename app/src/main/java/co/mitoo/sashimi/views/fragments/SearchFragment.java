package co.mitoo.sashimi.views.fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import co.mitoo.sashimi.models.jsonPojo.MockPojo;
import co.mitoo.sashimi.models.jsonPojo.Sport;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LeagueQueryRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.LocationResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.SearchResultsEvent;
import co.mitoo.sashimi.views.adapters.LeagueAdapter;
import co.mitoo.sashimi.views.adapters.SportAdapter;

/**
 * Created by david on 14-11-05.
 */
public class SearchFragment extends MitooLocationFragment implements AdapterView.OnItemClickListener {

    private ListView leagueList;
    private ListView sportsList;
    private LeagueAdapter leagueDataAdapter;
    private SportAdapter sportsDataAdapter;
    private List<League> leagueData ;
    private List<Sport> sportData ;
    private boolean filterOn = false;
    private boolean sportSelectionOn = false;
    private String queryText;

    public static SearchFragment newInstance() {

        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStaste) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_search,
                container, false);
        initializeOnClickListeners(view);
        initializeFields();
        initializeViews(view);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    
    @Override
    public void onStop(){
        super.onStop();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    @Override
    protected void initializeFields(){

        setFragmentTitle(getString(R.string.tool_bar_near_you));
        leagueData = new ArrayList<League>();
        sportData= new ArrayList<Sport>();
        sportData.addAll(MockPojo.getSportList());
        getMitooActivity().addModel(LeagueModel.class);
        
    }
    
    @Subscribe
    public void recieveLeagueResult(LeagueQueryResponseEvent event){

        if(event.getResults()!=null){

            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.bundle_key_tool_bar_title), queryText);
            fireFragmentChangeAction(R.id.fragment_search_results , bundle);
            
        }
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setUpSearchView(view);
        sportsDataAdapter = new SportAdapter(getActivity(),R.id.sportsListView, sportData);
        sportsList = (ListView) view.findViewById(R.id.sportsListView);
        sportsList.setOnItemClickListener(this);
        sportsList.setAdapter(sportsDataAdapter);
        sportsDataAdapter.notifyDataSetChanged();

    }

    private void initializeOnClickListeners(View view){

        /** Taking out filters for version 1
         * 
         * 
         *  
         view.findViewById(R.id.micButton).setOnClickListener(this);
         view.findViewById(R.id.sportFilterButton).setOnClickListener(this);
         view.findViewById(R.id.filterButtonTop).setOnClickListener(this);
         view.findViewById(R.id.filterButtonBottom).setOnClickListener(this);
         *
         * 
         * 
         **/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

                /** Taking out filters for version 1
                 *
                 *
                 *
                 case R.id.filterButtonTop:
                 case R.id.filterButtonBottom:
                 filterButtonAction();
                 break;
                 *
                 *
                 *
                 *             case R.id.leagueListView:
                 leagueListItemAction();
                 break;
                 case R.id.sportFilterButton:
                 sportSelectionAction();
                 break;*
                 **/


        }
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        handleAndDisplayError(error);
    }

    private void sportSelectionAction(){
        sportData.addAll(MockPojo.getSportList());
        sportsDataAdapter.notifyDataSetChanged();
     
        View generalFilter = getActivity().findViewById(R.id.viewGeneralFilter);
        View sportFilter = getActivity().findViewById(R.id.viewSportFilter);
        
        generalFilter.setVisibility(View.GONE);
        sportFilter.setVisibility(View.VISIBLE);
    }

    /*
    *
    * 
    *
        private void filterButtonAction(){
        if(filterOn){
            slidDownView(R.id.filterSelectionContainer);
            fadeInView(R.id.filterButtonBottom);
        }
        else{
            slideUpView(R.id.filterSelectionContainer);
            fadeOutView(R.id.filterButtonBottom);
        }
        filterOn = !filterOn;
    }
    *
    *
    *
    */

    private void searchAction(String query){

        search(query);
     //   setFragmentTitle(searchInput);
    }
    
    private synchronized <T> void addToListList(List<T>container ,List<T> additionList){
        for(T item : additionList){
            container.add(item);
        }
    }

    private synchronized <T> void clearList(List<T> result){
        if(result!=null){
            Iterator<T> iterator = result.iterator();
            while(iterator.hasNext()){
                iterator.remove();
            }

        }
    }

    private void search(String input){
        this.queryText=input;
        BusProvider.post(new LeagueQueryRequestEvent(input));
    }

    @Subscribe
    public void recieveLocation(LocationResponseEvent event){
        setLocation(event.getLocation());
    }


    @Override
    protected void setUpToolBar(View view) {

        super.setUpToolBar(view);


    }
    
    private void setUpSearchView(View view){
        
        final SearchView searchView = (SearchView)view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchAction(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
