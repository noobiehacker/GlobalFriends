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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Sport;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LeagueQueryRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.LocationResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.adapters.SportAdapter;

/**
 * Created by david on 14-11-05.
 */
public class SearchFragment extends MitooLocationFragment implements AdapterView.OnItemClickListener  {

    private ListView sportsList;
    private SportAdapter sportsDataAdapter;
    private List<Sport> sportData ;
    private RelativeLayout searchPlaceHolder;
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
        sportData= new ArrayList<Sport>();
        sportData.addAll(getSports());

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
        setUpSportsList(view);
        setUpSearchPlaceHolder(view);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        if(parent.getId() == sportsList.getId()) {
            if (position != 0) {
                Sport item = (Sport) sportsList.getItemAtPosition(position);
                searchFieldAction(item.getName());

            }
        }
    }

    private void initializeOnClickListeners(View view){

        view.findViewById(R.id.search_bar).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        
        switch(v.getId()){
            case R.id.search_bar:
                SearchView searchView = (SearchView) v.findViewById(R.id.search_view);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
            case R.id.search_view:
                hideSearchPlaceHolder();
                break;
        }
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        handleAndDisplayError(error);
    }
    
    private List<Sport> getSports(){
        
        ArrayList<Sport> returnList = new ArrayList<Sport>();
        String[] sportsArray = getResources().getStringArray(R.array.sports_array);
        for(String item : sportsArray){
            returnList.add(new Sport(item));
        }
        return returnList;

    }

    private void searchFieldAction(String query){

        this.queryText=query;
        BusProvider.post(new LeagueQueryRequestEvent(query));
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


    @Subscribe
    public void recieveLocation(LocationResponseEvent event){
        setLocation(event.getLocation());
    }


    @Override
    protected void setUpToolBar(View view) {

        toolbar = (Toolbar)view.findViewById(R.id.app_bar);
        if(toolbar!=null) {

            toolbar.setNavigationIcon(R.drawable.header_back_icon);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            toolbar.addView(createClickableTitleView());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMitooActivity().onBackPressed();
                }
            });
        }
     }
    
    private void setUpSearchView(View view){
        
        final SearchView searchView = (SearchView)view.findViewById(R.id.search_view);
        searchView.setOnSearchClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchFieldAction(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
    
    private void setUpSportsList(View view){
       
        sportsDataAdapter = new SportAdapter(getActivity(),R.id.sportsListView, sportData);
        sportsList = (ListView) view.findViewById(R.id.sportsListView);
        sportsList.addHeaderView(getSportsListHeader());
        sportsList.setOnItemClickListener(this);
        sportsList.setAdapter(sportsDataAdapter);
        sportsDataAdapter.notifyDataSetChanged();
        
    }
    
    private View getSportsListHeader(){

        View header = getActivity().getLayoutInflater().inflate(R.layout.list_view_header , null);
        TextView suggestionTextView = (TextView) header.findViewById(R.id.itemText);
        suggestionTextView.setText(getString(R.string.search_page_text_2));
        return header;
    }


    public RelativeLayout getSearchPlaceHolder() {
        return searchPlaceHolder;
    }

    public void setUpSearchPlaceHolder(View view) {
        this.searchPlaceHolder =(RelativeLayout) view.findViewById(R.id.search_view_placeholder_container);
        TextView searchTextPlaceHolder = (TextView) view.findViewById(R.id.search_view_text_view_placeholder);
        searchTextPlaceHolder.setText(getString(R.string.search_page_text_3));
    }

    private void hideSearchPlaceHolder(){
        getSearchPlaceHolder().setVisibility(View.GONE);

    }
    
    private TextView createClickableTitleView(){

        TextView textViewToAdd = new TextView(getActivity());
        textViewToAdd.setText(getFragmentTitle());
        textViewToAdd.setTextAppearance(getActivity(), R.style.whiteLargeText);
        textViewToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireFragmentChangeAction(R.id.fragment_location_search);
            }
        });
        return textViewToAdd;
    }
}
