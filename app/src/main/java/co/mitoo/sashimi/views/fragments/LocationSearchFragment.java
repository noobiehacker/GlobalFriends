package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.mitoo.sashimi.R;

/**
 * Created by david on 15-01-23.
 */
public class LocationSearchFragment extends MitooFragment {


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
    public void onClick(View v) {


    }

    @Override
    protected void initializeFields(){

        setFragmentTitle(getString(R.string.tool_bar_location_search));

    }

    @Override
    protected void initializeViews(View view){

        setUpToolBar(view);

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

    private void initializeOnClickListeners(View view){


    }

    private SearchView createSearchView(View view){

        SearchView searchView = new SearchView(getActivity());
        searchView.setQueryHint(getString(R.string.location_search_page_text_1));
        searchView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        //searchView.setSuggestionsAdapter();
        return searchView;
    }

}
