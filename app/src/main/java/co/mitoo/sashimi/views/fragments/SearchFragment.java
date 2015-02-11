package co.mitoo.sashimi.views.fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LocationModel;
import co.mitoo.sashimi.models.jsonPojo.Sport;
import co.mitoo.sashimi.utils.IsSearchable;
import co.mitoo.sashimi.utils.events.AlgoliaLeagueSearchEvent;
import co.mitoo.sashimi.utils.events.LeagueQueryResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.adapters.SearchableAdapter;
import se.walkercrou.places.Place;

/**
 * Created by david on 14-11-05.
 */
public class SearchFragment extends MitooFragment implements AdapterView.OnItemClickListener {

    private ListView sportsList;
    private SearchableAdapter sportsDataAdapter;
    private List<IsSearchable> sportData;
    private RelativeLayout searchPlaceHolder;
    private TextView searchMitooText;
    private View searchMitooForView;
    private String queryText;

    public static SearchFragment newInstance() {

        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getMitooActivity().getModelManager().getLocationModel().GpsCurrentLocationRequest();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStaste) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_search,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handleLocationServices();
    }

    @Override
    protected void initializeFields() {

        updateFragmentTitle();
        sportData = new ArrayList<IsSearchable>();
        sportData.addAll(getDataHelper().getSports());

    }

    @Subscribe
    public void recieveLeagueResult(LeagueQueryResponseEvent event) {

        if (event.getResults() != null) {

            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.bundle_key_tool_bar_title), queryText);
            fireFragmentChangeAction(R.id.fragment_search_results, bundle);

        }
    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setUpSearchView(view);
        setUpSportsList(view);
        setUpSearchPlaceHolder(view);
        setUpSearchMitooForView(view);
    }

    @Override
    protected void initializeOnClickListeners(View view) {

        super.initializeOnClickListeners(view);
        view.findViewById(R.id.search_bar).setOnClickListener(this);
        view.findViewById(R.id.search_mitoo_for).setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == sportsList.getId()) {
            if (position != 0) {
                Sport item = (Sport) sportsList.getItemAtPosition(position);
                setQueryText(item.getName());
                searchFieldAction();
                parent.setFocusable(true);
                parent.requestFocus();

            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.search_bar:
                SearchView searchView = (SearchView) v.findViewById(R.id.search_view);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
            case R.id.search_view:
                hideSearchPlaceHolder();
                break;
            case R.id.search_mitoo_for:
                searchFieldAction();
                break;
        }
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        super.onError(error);
    }


    @Override
    protected void setUpToolBar(View view) {

        toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        if (toolbar != null) {

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

    private void setUpSearchView(View view) {

        final SearchView searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnSearchClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchFieldAction();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                queryRefineAction(s);
                return false;
            }
        });

        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setTextColor(getResources().getColor(R.color.gray_dark_three));
    }

    private void setUpSportsList(View view) {

        sportsDataAdapter = new SearchableAdapter(getActivity(), R.id.sportsListView, sportData);
        setSportsList((ListView) view.findViewById(R.id.sportsListView));
        getSportsList().addHeaderView(getSuggestedSearchHeader());
        getSportsList().setOnItemClickListener(this);
        getSportsList().setAdapter(sportsDataAdapter);
        sportsDataAdapter.notifyDataSetChanged();

    }

    private View getSuggestedSearchHeader() {

        View header = getActivity().getLayoutInflater().inflate(R.layout.list_view_header, null);
        TextView suggestionTextView = (TextView) header.findViewById(R.id.dynamicText);
        suggestionTextView.setText(getString(R.string.search_page_text_2));
        return header;
    }


    public RelativeLayout getSearchPlaceHolder() {
        return searchPlaceHolder;
    }

    public void setUpSearchPlaceHolder(View view) {
        this.searchPlaceHolder = (RelativeLayout) view.findViewById(R.id.search_view_placeholder_container);
        TextView searchTextPlaceHolder = (TextView) view.findViewById(R.id.search_view_text_view_placeholder);
        searchTextPlaceHolder.setText(getString(R.string.search_page_text_3));
    }

    private void hideSearchPlaceHolder() {
        getSearchPlaceHolder().setVisibility(View.GONE);

    }

    private View createClickableTitleView() {

        int layoutId = R.layout.view_search_title_with_triangle;
        View view = getActivity().getLayoutInflater().inflate(layoutId ,null);
        TextView textViewToAdd = (TextView) view.findViewById(R.id.tool_bar_title);
        textViewToAdd.setText(getFragmentTitle());
        textViewToAdd.setTextAppearance(getActivity(), R.style.whiteLargeText);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireFragmentChangeAction(R.id.fragment_location_search);
            }
        });
        return view;
    }

    public void updateFragmentTitle() {
        
        LocationModel locationModel = getLocationModel();
        if(locationModel.isUsingCurrentLocation()){
            this.fragmentTitle = getString(R.string.search_page_text_4);

        }else if(locationModel.getSelectedPlace()!=null){
            Place place= getLocationModel().getSelectedPlace();
            this.fragmentTitle = place.getName();
        }else{
            setFragmentTitle(getString(R.string.tool_bar_near_you));
        }

    }

    private LocationModel getLocationModel() {

        return (LocationModel) getMitooModel(LocationModel.class);
    }

    private void searchFieldAction() {

        getLocationModel().requestSelectedLocationLatLng();
        setLoading(true);

    }

    private void queryRefineAction(String query) {

        setQueryText(query);
        List<IsSearchable> updatedList = getDataHelper().getSports(query);
        handleViewVisibility(getSearchMitooForView(), getQueryText().length()>0);
        handleViewVisibility(getSportsList(), updatedList.size()>0);
        getDataHelper().clearList(getSportData());
        getDataHelper().addToListList(getSportData(), updatedList);
        sportsDataAdapter.notifyDataSetChanged();
    }



    public List<IsSearchable> getSportData() {
        return sportData;
    }

    public void setSportData(List<IsSearchable> sportData) {
        this.sportData = sportData;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
        updateSearchMitooText(queryText);
    }

    private void updateSearchMitooText(String query){
        
        if(getSearchMitooText()!=null){
            getSearchMitooText().setText(getString(R.string.search_page_text_1) + " " + query);
        }
            
    }

    public TextView getSearchMitooText() {
        return searchMitooText;
    }

    public void setSearchMitooText(TextView searchMitooText) {
        this.searchMitooText = searchMitooText;
    }

    private void setUpDynamicText(View view){

        TextView dynamicText = (TextView) view.findViewById(R.id.dynamicText);
        dynamicText.setText(getString(R.string.search_page_text_1));
        setSearchMitooText(dynamicText);

    }

    private void setUpSearchMitooForView(View view){

        View searchMitooFor = (View) view.findViewById(R.id.search_mitoo_for);
        setSearchMitooForView(searchMitooFor);
        setUpDynamicText(searchMitooFor);
    }

    @Subscribe
    public void onLatLngRecieved(LatLng latLng){

        if(getDataHelper().IsValid(latLng)){
            getLeagueModel().requestAlgoLiaSearch(new AlgoliaLeagueSearchEvent(getQueryText(), latLng));
        }
        else{
            getLeagueModel().requestAlgoLiaSearch(new AlgoliaLeagueSearchEvent(getQueryText()));
        }
    }
    
    public ListView getSportsList() {
        return sportsList;
    }

    public void setSportsList(ListView sportsList) {
        this.sportsList = sportsList;
    }

    public View getSearchMitooForView() {
        return searchMitooForView;
    }

    public void setSearchMitooForView(View searchMitooForView) {
        this.searchMitooForView = searchMitooForView;
    }
}

