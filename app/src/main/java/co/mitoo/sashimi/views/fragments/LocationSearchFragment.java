package co.mitoo.sashimi.views.fragments;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LocationModel;
import co.mitoo.sashimi.utils.IsSearchable;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.PredictionWrapper;
import co.mitoo.sashimi.utils.events.LocationModelLocationsSelectedEvent;
import co.mitoo.sashimi.utils.events.LocationModelQueryResultEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.adapters.SearchableAdapter;
import se.walkercrou.places.Place;

/**
 * Created by david on 15-01-23.
 */
public class LocationSearchFragment extends MitooFragment implements AdapterView.OnItemClickListener{

    private List<IsSearchable> predictions;
    private ListView placesList;
    private SearchableAdapter placeListAdapter;

    public static LocationSearchFragment newInstance() {
        LocationSearchFragment fragment = new LocationSearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_location_search,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeFields(){

        setFragmentTitle(getString(R.string.tool_bar_location_search));

    }

    @Override
    protected void initializeViews(View view){

        setUpToolBar(view);
        setUpPlacesList(view);
        setUpDynamicText(view);
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        super.onError(error);
    }
    
    @Override
    protected void setUpToolBar(View view) {

        toolbar = (Toolbar)view.findViewById(R.id.app_bar);
        if(toolbar!=null) {

            toolbar.setNavigationIcon(R.drawable.header_back_icon);
            toolbar.addView(createSearchView(view));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMitooActivity().onBackPressed();
                }
            });
        }
    }

    
    private void setUpDynamicText(View view){
        
        TextView dynamicText = (TextView) view.findViewById(R.id.dynamicText);
        dynamicText.setText(getString(R.string.location_search_page_text_2));
        
    }
    
    @Override
    protected void initializeOnClickListeners(View view){

        super.initializeOnClickListeners(view);
        view.findViewById(R.id.current_location).setOnClickListener(this);
    }

    private SearchView createSearchView(View view){

        SearchView searchView = new SearchView(getActivity());
        searchView.setQueryHint(getString(R.string.location_search_page_text_1));
        searchView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                querySearchAction(s);
                return false;
            }
        });
        return searchView;
    }
    
    @Subscribe
    public void onLocationModelQueryResult(LocationModelQueryResultEvent event) {

        updatePredictions(event.getPlaces());
    }

    public SearchableAdapter getPlaceListAdapter() {
        return placeListAdapter;
    }

    public List<IsSearchable> getPredictions() {
        if(predictions==null)
            predictions = new ArrayList<IsSearchable>();
        return predictions;
    }

    public void setPredictions(List<IsSearchable> predictions) {
        this.predictions = predictions;
    }

    public void updatePredictions(List<IsSearchable> predictions) {
        
        getDataHelper().clearList(getPredictions());
        getDataHelper().addToListList(getPredictions(),predictions);
        placeListAdapter.notifyDataSetChanged();
        handleViewVisibility(getPlacesList(),predictions.size()>0 );

    }

    private void querySearchAction(String query){
        
        if(query.equals("")){
            updatePredictions(new ArrayList<IsSearchable>());
        }else{
            LocationModel locationModel = getLocationModel();
            if(locationModel!=null){
                locationModel.searchForPrediction(query);
            }
        }
    }
    
    private LocationModel getLocationModel(){
        
        return (LocationModel) getMitooModel(LocationModel.class);
    }
    

    private void setUpPlacesList(View view){

        setPlacesList((ListView) view.findViewById(R.id.placesListView));
        getPlacesList().addHeaderView(getSuggestedSearchHeader());
        getPlacesList().setOnItemClickListener(this);
        setUpAdapter();

    }
    
    private void setUpAdapter(){
        placeListAdapter = new SearchableAdapter(getActivity(),R.id.sportsListView, getPredictions());
        getPlacesList().setAdapter(placeListAdapter);
        placeListAdapter.notifyDataSetChanged();
        
    }

    private View getSuggestedSearchHeader(){

        View header = getActivity().getLayoutInflater().inflate(R.layout.list_view_header , null);
        TextView suggestionTextView = (TextView) header.findViewById(R.id.dynamicText);
        suggestionTextView.setText(getString(R.string.search_page_text_2));
        return header;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == placesList.getId()) {
            if (position != 0) {
                parent.setFocusable(true);
                parent.requestFocus();
                getMitooActivity().hideSoftKeyboard(view);
                PredictionWrapper wrapper = (PredictionWrapper) placesList.getItemAtPosition(position);
                placeSelectionAction(wrapper);
            }
        }
    }
    
    private void placeSelectionAction(PredictionWrapper prediction){
        
        setLoading(true);
        getLocationModel().selectPlace(prediction);
        
    }
    
    @Subscribe
    public void onLocationSelected(LocationModelLocationsSelectedEvent event){

        fireFragmentChangeAction(MitooEnum.fragmentTransition.POP);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.current_location:
                getLocationModel().setToUseCurrentLocation(true);
                getMitooActivity().hideSoftKeyboard(v);
                setLoading(true);
                break;
        }
    }


    public ListView getPlacesList() {
        return placesList;
    }

    public void setPlacesList(ListView placesList) {
        this.placesList = placesList;
    }
}
