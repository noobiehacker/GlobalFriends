package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.github.androidprogresslayout.ProgressLayout;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;

import co.mitoo.sashimi.models.appObject.MitooStandings;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.FixtureWrapper;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.events.LoadStandingsEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.StandingsLoadedEvent;
import co.mitoo.sashimi.views.adapters.StandingsGridAdapter;
import co.mitoo.sashimi.views.widgets.HeaderGridView;

/**
 * Created by david on 15-04-23.
 */
public class StandingsFragment extends MitooFragment {

    private List<MitooStandings> standingsList;
    private HeaderGridView standingsGridView;
    private StandingsGridAdapter standingsGridAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestData();
    }

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable(v.getId())){
            switch (v.getId()) {

            }
        }
    }

    public static StandingsFragment newInstance() {
        StandingsFragment fragment = new StandingsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_standings,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        this.standingsGridView = (HeaderGridView) view.findViewById(R.id.standings_grid_view);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        this.standingsList = new ArrayList<MitooStandings>();

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    @Override
    protected void requestData() {
        BusProvider.post(new LoadStandingsEvent(getCompetitionSeasonID()));
    }

    @Subscribe
    public void onStandingsLoaded(StandingsLoadedEvent event){

        //Update the data for our Grid Adapter, set header and update views
        updateStandingsData(event.getMitooStandings());
        getViewHelper().setUpListHeader(this.standingsGridView, R.layout.view_standings_list_header,
                getString(R.string.standing_page_header_text));

        this.standingsGridView.setAdapter(getStandingsGridAdapter());
        getStandingsGridAdapter().notifyDataSetChanged();

    }

    public void updateStandingsData(List<MitooStandings> standingsList) {

        if (standingsList != null) {
            DataHelper dataHelper = getDataHelper();
            dataHelper.clearList(this.standingsList);
            dataHelper.addToListList(this.standingsList, standingsList);
        }
    }

    private int getCompetitionSeasonID(){

        int result = MitooConstants.invalidConstant;
        Competition selectedCompetion = getCompetitionModel().getSelectedCompetition();
        if(selectedCompetion!=null){
            result= selectedCompetion.getId();
        }
        return result;
    }

    public StandingsGridAdapter getStandingsGridAdapter() {
        if(standingsGridAdapter == null){
            standingsGridAdapter = new StandingsGridAdapter(getActivity(), R.id.standings_grid_view,
                            standingsList , this);
        }
        return standingsGridAdapter;
    }
}
