package co.mitoo.sashimi.views.fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.CompetitionModel;
import co.mitoo.sashimi.models.ICompetitionModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.MockPojo;
import co.mitoo.sashimi.models.jsonPojo.Sport;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LocationResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.ToolBarDisplayEvent;
import co.mitoo.sashimi.views.adapters.LeagueAdapter;
import co.mitoo.sashimi.views.adapters.SportAdapter;

/**
 * Created by david on 14-11-05.
 */
public class SearchFragment extends MitooLocationFragment implements AdapterView.OnItemClickListener {

    private ICompetitionModel model;
    private ListView leagueList;
    private ListView sportsList;
    private LeagueAdapter leagueDataAdapter;
    private SportAdapter sportsDataAdapter;
    private List<League> leagueData ;
    private List<Sport> sportData ;
    private boolean filterOn = false;
    private boolean sportSelectionOn = false;
    private Toolbar toolbar;

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
        searchAction();
        BusProvider.post(new ToolBarDisplayEvent(true));
    }
    
    @Override
    public void onStop(){
        super.onStop();
        BusProvider.post(new ToolBarDisplayEvent(false));
        
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        BusProvider.post(new ToolBarDisplayEvent(false));
    }
    
    private void initializeFields(){

        model = new CompetitionModel(getResources());
        sportData = new ArrayList<Sport>();
        leagueData = new ArrayList<League>();

    }

    private void initializeViews(View view){

        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        leagueDataAdapter = new LeagueAdapter(getActivity(),R.id.leagueListView,leagueData);
        leagueList = (ListView) view.findViewById(R.id.leagueListView);
        leagueList.setOnItemClickListener(this);
        leagueList.setAdapter(leagueDataAdapter);
        setUpToolBar(view);

        /** Taking out filters for version 1
         *
         *
         *
         sportsDataAdapter = new SportAdapter(getActivity(),R.id.sportsListView, sportData);
        sportsList = (ListView) view.findViewById(R.id.sportsListView);
        sportsList.setOnItemClickListener(this);
        sportsList.setAdapter(sportsDataAdapter);
         *
         *
         *
         **/

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
            case R.id.micButton:
                searchAction();
                break;

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
                 **/

            case R.id.leagueListView:
                leagueListItemAction();
                break;
            case R.id.sportFilterButton:
                sportSelectionAction();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        if(parent.getId() == leagueList.getId())
        {
            leagueListItemAction();
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


    private void leagueListItemAction(){
        fireFragmentChangeAction(R.id.fragment_league);
    }

    private void searchAction(){
        search("");
        //search(getSearchInput());
    }

    private void search(String input){
        leagueData.addAll(MockPojo.getLeagueList());
        leagueDataAdapter.notifyDataSetChanged();
    }

    private String getSearchInput(){
        return this.getTextFromTextField(R.id.search_input);
    }

    @Subscribe
    public void recieveLocation(LocationResponseEvent event){
        setLocation(event.getLocation());
    }


    private void hideActionBar(){

        this.toolbar.setVisibility(View.GONE);

    }

    private void showActionBar(){

        this.toolbar.setVisibility(View.VISIBLE);
    }
    private void setUpToolBar(View view) {

        toolbar = (Toolbar)view.findViewById(R.id.app_bar);
        ActionBarActivity activity = (ActionBarActivity)getActivity();
        activity.setSupportActionBar(toolbar);

    }

}
