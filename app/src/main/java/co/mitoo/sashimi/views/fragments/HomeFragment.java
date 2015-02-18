package co.mitoo.sashimi.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import co.mitoo.sashimi.views.adapters.LeagueAdapter;

/**
 * Created by david on 15-01-12.
 */
public class HomeFragment extends MitooFragment {

    private List<League> enquiredLeagueData;
    private ListView leagueList;
    private LeagueAdapter leagueDataAdapter;

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.search_bar:
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
        if(getEnquiredLeagueData().size()!=0)
            setUpListView(view);
        else
            setUpNoResultsTextView(view);
    }

    @Override
    protected void initializeOnClickListeners(View view) {

        super.initializeOnClickListeners(view);
        
    }

    @Override
    public void onResume(){
        super.onResume();
        getMitooActivity().hideSoftKeyboard(2000);
        
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

                    if(getDataHelper().isClickable()){
                        switch (menuItem.getItemId()){
                            case R.id.menu_feedback:
                                FeedBackDialogBuilder dialog = new FeedBackDialogBuilder(getActivity());
                                dialog.buildPrompt().show();
                                break;
                            case R.id.menu_settings:
                                BusProvider.post(new UserInfoModelRequestEvent(getUserId()));
                                break;
                            case R.id.menu_search:
                                fireFragmentChangeAction(R.id.fragment_search);
                                break;
                        }
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

        leagueList = (ListView) view.findViewById(R.id.leagueListView);
        leagueList.setAdapter(getLeagueDataAdapter());
        leagueList.setOnItemClickListener(getLeagueDataAdapter());
        leagueList.addHeaderView(getViewHelper().createViewFromInflator(R.layout.view_enquired_league_text_view));
    }

    @Override
    public void tearDownReferences(){

        super.tearDownReferences();
    }

    
    @Override
    protected void removeDynamicViews(){
        super.removeDynamicViews();
    }


    private void setUpNoResultsTextView(View view){

        TextView noResultsView = (TextView) view.findViewById(R.id.noEnquiredTextView);
        noResultsView.setVisibility(View.VISIBLE);
    }

    public LeagueAdapter getLeagueDataAdapter() {
        if(leagueDataAdapter==null)
            leagueDataAdapter = new LeagueAdapter(getActivity(), R.id.leagueListView, getEnquiredLeagueData(), this, false);
        return leagueDataAdapter;
    }

}
