package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LocationModel;
import co.mitoo.sashimi.models.jsonPojo.Sport;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.IsSearchable;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooSearchViewStyle;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
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
    private TextView searchMitooText;
    private View searchMitooForView;
    private String queryText;
    private SearchView searchView;

    public static SearchFragment newInstance() {

        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestGPSLocation();

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
        setUpIconifiedCallBack();

    }

    @Override
    protected void initializeFields() {

        updateFragmentTitle();
        setSportData(new ArrayList<IsSearchable>());
        getSportData().addAll(getDataHelper().getSports());

    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setUpSearchView(view);
        setUpSportsList(view);
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

        if(getDataHelper().isClickable()){
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
    }

    @Override
    public void onClick(View v) {

        if(getDataHelper().isClickable()){
            switch (v.getId()) {
                case R.id.search_bar:
                    getSearchView().requestFocusFromTouch();
                    break;
                case R.id.search_mitoo_for:
                    searchFieldAction();
                    break;
            }
        }
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        super.onError(error);
    }


    @Override
    protected Toolbar setUpToolBar(View view) {

        setToolbar((Toolbar)view.findViewById(R.id.app_bar));
        if (getToolbar() != null) {
            getToolbar().setTitle("");
            getToolbar().addView(createClickableTitleView());
            getToolbar().setNavigationIcon(R.drawable.header_back_icon);
            setUpBackButtonClickListner();
        }
        return getToolbar();
    }

    private void setUpSearchView(View view) {

        ViewGroup searchContainer = (ViewGroup)view.findViewById(R.id.search_view_container);
        SearchView searchView = createSearchView();
        searchContainer.addView(searchView);
     //   getViewHelper().recursivelyCenterVertically(searchView);

    }

    private SearchView createSearchView(){

        setSearchView(new SearchView(getActivity()));
        getViewHelper().customizeMainSearch(getSearchView());
        getSearchView().setOnQueryTextListener(createQueryTextChangeListner());
        return getSearchView();

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
        TextView suggestionTextView = (TextView) header.findViewById(R.id.header_text);
        suggestionTextView.setText(getString(R.string.search_page_text_2));
        return header;
    }

    private View createClickableTitleView() {

        int layoutId = R.layout.view_search_title_with_triangle;
        View view = getActivity().getLayoutInflater().inflate(layoutId ,null);
        TextView textViewToAdd = (TextView) view.findViewById(R.id.tool_bar_title);
        textViewToAdd.setText(getFragmentTitle());
        textViewToAdd.setTextAppearance(getActivity(), R.style.whiteLargeText);
        view.setLayoutParams(getViewHelper().createCenterInVerticalParam());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDataHelper().isClickable()) {
                    FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                            .setFragmentID(R.id.fragment_location_search)
                            .build();
                    postFragmentChangeEvent(event);
                }
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

    private void searchFieldAction() {

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_key_tool_bar_title), getQueryText());

        FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_search_results)
                .setBundle(bundle)
                .build();
        postFragmentChangeEvent(event);
        getLocationModel().requestSelectedLocationLatLng();

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

    private void setUpSearchMitooText(View view){

        TextView dynamicText = (TextView) view.findViewById(R.id.small_list_item_text);
        dynamicText.setText(getString(R.string.search_page_text_1));
        setSearchMitooText(dynamicText);

    }

    private void setUpSearchMitooForView(View view){

        View searchMitooFor = (View) view.findViewById(R.id.search_mitoo_for);
        setSearchMitooForView(searchMitooFor);
        setUpSearchMitooText(searchMitooFor);
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

    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    private SearchView.OnQueryTextListener createQueryTextChangeListner(){

        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchFieldAction();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")){
                    MitooSearchViewStyle.on(getSearchView()).hideCloseButton();
                }else{
                    MitooSearchViewStyle.on(getSearchView()).showCloseButton();
                }
                queryRefineAction(s);
                return false;
            }
        };
    }
    
    @Override 
    public void tearDownReferences(){
        super.tearDownReferences();
        getSearchView().setIconified(true);
        
    }
    
    private void setUpIconifiedCallBack(){
        setRunnable(new Runnable() {
            @Override
            public void run() {
                getSearchView().setIconified(false);
            }
        });
        getHandler().postDelayed(getRunnable(), MitooConstants.durationMedium);
        
    }

    private void requestGPSLocation(){

        if(getMitooActivity()!=null){
            LocationModel locationModel = getMitooActivity().getModelManager().getLocationModel();
            locationModel.GpsCurrentLocationRequest();
        }
    }
}

