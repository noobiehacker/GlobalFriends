package co.mitoo.sashimi.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.managers.ModelManager;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.UserInfoModelRequestEvent;
import co.mitoo.sashimi.utils.events.UserInfoModelResponseEvent;
import co.mitoo.sashimi.views.Dialog.FeedBackDialogBuilder;

/**
 * Created by david on 15-01-12.
 */
public class HomeFragment extends MitooFragment {

    private LinearLayout leagueListHolder;
    private List<League> enquiredLeagueData;
    private RelativeLayout searchPlaceHolder;

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.search_bar:
            case R.id.search_view:
                hideSearchPlaceHolder();
                fireFragmentChangeAction(R.id.fragment_search);
                break;
        }
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        super.onError(error);
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_home,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setUpEnquriedLeagueData();

    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setUpSearchPlaceHolder(view);
        setUpListView(view);
    }



    @Override
    protected void initializeOnClickListeners(View view) {
        
        view.findViewById(R.id.search_bar).setOnClickListener(this);
        SearchView searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnSearchClickListener(this);
        super.initializeOnClickListeners(view);
        
    }


    @Override
    protected void setUpToolBar(View view) {

        toolbar = (Toolbar)view.findViewById(R.id.app_bar);
        if(toolbar!=null){

            toolbar.setLogo(R.drawable.header_mitoo_logo);
            toolbar.setTitle("");
            toolbar.inflateMenu(R.menu.menu_main);
            toolbar.setPopupTheme(R.style.MyPopupMenu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.menu_feedback:
                            FeedBackDialogBuilder dialog = new FeedBackDialogBuilder(getActivity());
                            dialog.buildPrompt().show();
                            break;
                        case R.id.menu_settings:
                            BusProvider.post(new UserInfoModelRequestEvent(getUserId()));
                            break;
                    }
                    return false;
                }
            });
        }

    }
    
    private int getUserId(){
        
        ModelManager manager = getMitooActivity().getModelManager();
        if(manager!=null){
            SessionRecieve session = manager.getSessionModel().getSession();
            if(session!=null)
                return session.id;
        }
        return MitooConstants.invalidConstant;
    }

    public RelativeLayout getSearchPlaceHolder() {
        return searchPlaceHolder;
    }

    public void setUpSearchPlaceHolder(View view) {
        this.searchPlaceHolder =(RelativeLayout) view.findViewById(R.id.search_view_placeholder_container);
        TextView searchTextPlaceHolder = (TextView) view.findViewById(R.id.search_view_text_view_placeholder);
        searchTextPlaceHolder.setText(getString(R.string.home_page_text_2));
    }
    
    private void hideSearchPlaceHolder(){
        getSearchPlaceHolder().setVisibility(View.GONE);
        
    }
        
    @Subscribe
    public void onUserInfoReceieve(UserInfoModelResponseEvent event){

        fireFragmentChangeAction(R.id.fragment_settings);

    }

    public void setUpEnquriedLeagueData(){

        if(getLeagueModel().getLeaguesEnquired()!=null){
            DataHelper dataHelper = new DataHelper(getMitooActivity());
            dataHelper.clearList(getEnquiredLeagueData());
            dataHelper.addToListList(getEnquiredLeagueData(), getLeagueModel().getLeaguesEnquired());
        }
    }

    public List<League> getEnquiredLeagueData() {
        if (enquiredLeagueData == null) {
            enquiredLeagueData= new ArrayList<League>();
        }
        return enquiredLeagueData;
    }

    private void setUpListView(View view){

        int leagueLayout = R.layout.list_view_item_league;
        setLeagueListHolder((LinearLayout) view.findViewById(R.id.league_image_holder));
        getViewHelper().addLeagueDataToList(this, leagueLayout, getLeagueListHolder(), getEnquiredLeagueData());

    }

    @Override
    public void tearDownReferences(){

        getRootView().removeAllViews();
        super.tearDownReferences();
    }

    public LinearLayout getLeagueListHolder() {
        return leagueListHolder;
    }

    public void setLeagueListHolder(LinearLayout leagueListHolder) {
        this.leagueListHolder = leagueListHolder;
    }


}




