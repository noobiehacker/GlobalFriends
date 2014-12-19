package co.mitoo.sashimi.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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
import co.mitoo.sashimi.utils.events.LocationPromptEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.adapters.LeagueAdapter;
import co.mitoo.sashimi.views.adapters.SportAdapter;

/**
 * Created by david on 14-11-05.
 */
public class CompetitionFragment extends MitooFragment implements AdapterView.OnItemClickListener{

    private ICompetitionModel model;
    private ListView leagueList;
    private GridView sportsGrid;
    private LeagueAdapter leagueDataAdapter;
    private SportAdapter sportsImageadapter;
    private List<League> leagueData ;
    private List<Sport> sportData ;
    private boolean filterOn = false;
    private boolean sportSelectionOn = false;
    public static CompetitionFragment newInstance() {

        return new CompetitionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_competition,
                container, false);
        initializeOnClickListeners(view);
        initializeFields();
        initializeViews(view);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        handleLocationServices();
        model.requestLocation();
    }

    private void initializeFields(){

        model = new CompetitionModel(getResources());
        sportData = new ArrayList<Sport>();
        leagueData = new ArrayList<League>();

    }

    private void initializeViews(View view){

        sportsImageadapter = new SportAdapter(getActivity(),R.id.sportSelectionGrid, sportData);
        sportsGrid = (GridView) view.findViewById(R.id.sportSelectionGrid);
        sportsGrid.setAdapter(sportsImageadapter);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        leagueDataAdapter = new LeagueAdapter(getActivity(),R.id.leagueListView,leagueData);
        leagueList = (ListView) view.findViewById(R.id.leagueListView);
        leagueList.setAdapter(leagueDataAdapter);
        leagueList.setOnItemClickListener(this);
    }

    private void initializeOnClickListeners(View view){

        view.findViewById(R.id.micButton).setOnClickListener(this);
        view.findViewById(R.id.filterButtonBottom).setOnClickListener(this);
        view.findViewById(R.id.filterButtonTop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.micButton:
                searchAction();
                break;
            case R.id.filterButtonBottom:
            case R.id.filterButtonTop:
                filterButtonAction();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==0)
            sportSelectionAction();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        handleAndDisplayError(error);
    }

    @Subscribe
    public void onLocationServicePrompt(LocationPromptEvent event){
        this.buildLocationServicePrompt();
    }

    private void sportSelectionAction(){
        sportData.addAll(MockPojo.getSportList());
        sportsImageadapter.notifyDataSetChanged();
        if(sportSelectionOn){
            slidDownView(R.id.sportSelectionFilter);
            fadeInView(R.id.filterButtonBottom);
        }
        else{
            slideUpView(R.id.sportSelectionFilter);
            fadeOutView(R.id.filterButtonBottom);
        }
        sportSelectionOn =!sportSelectionOn;
    }

    private void filterButtonAction(){
        if(filterOn){
            slidDownView(R.id.layout_filter);
            fadeInView(R.id.filterButtonBottom);
        }
        else{
            slideUpView(R.id.layout_filter);
            fadeOutView(R.id.filterButtonBottom);
        }
        filterOn = !filterOn;
    }

    private void searchAction(){
        search(getSearchInput());

    }

    private void search(String input){
        leagueData.addAll(MockPojo.getLeagueList());
        leagueDataAdapter.notifyDataSetChanged();
    }

    private String getSearchInput(){
        return this.getTextFromTextField(R.id.search_input);
    }

}
