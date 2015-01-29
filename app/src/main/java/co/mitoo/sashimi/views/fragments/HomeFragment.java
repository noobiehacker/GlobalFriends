package co.mitoo.sashimi.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.managers.ModelManager;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.utils.events.UserInfoModelRequestEvent;
import co.mitoo.sashimi.utils.events.UserInfoModelResponseEvent;
import co.mitoo.sashimi.views.Dialog.FeedBackDialogBuilder;
import co.mitoo.sashimi.views.Dialog.LogOutDialogBuilder;

/**
 * Created by david on 15-01-12.
 */
public class HomeFragment extends MitooFragment {
    
    private League[] enquiredLeague;
    private RelativeLayout searchPlaceHolder;

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.search_bar:
            case R.id.search_view:
                hideSearchPlaceHolder();
                fireFragmentChangeAction(R.id.fragment_search);
                break;
            case R.id.enquired_league:
                getLeagueModel().setSelectedLeague(getFirstEnquriredLeague());
                fireFragmentChangeAction(R.id.fragment_league);
                break;
        }
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
        initializeOnClickListeners(view);
        initializeFields();
        initializeViews(view);
        return view;
    }

    @Override
    protected void initializeFields(){

        super.initializeFields();        
        setEnquiredLeague(getMitooActivity().getModelManager().getLeagueModel().getLeagueEnquired());

    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setCheckBoxVisible(view);
        setUpEnquireLeagues(view);
        setUpSearchPlaceHolder(view);
    }

    private void initializeOnClickListeners(View view){
        
        view.findViewById(R.id.search_bar).setOnClickListener(this);
        view.findViewById(R.id.enquired_league).setOnClickListener(this);
        SearchView searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnSearchClickListener(this);
        
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
                        case R.id.menu_logout:
                            LogOutDialogBuilder builder = new LogOutDialogBuilder(getActivity());
                            builder.buildPrompt().show();
                            break;
                    }
                    return false;
                }
            });
        }

    }

    public League[] getEnquiredLeague() {
        return enquiredLeague;
    }

    public void setEnquiredLeague(League[] enquiredLeague) {
        this.enquiredLeague = enquiredLeague;
    }
    
    private void setCheckBoxVisible(View view){
        ImageView checkBoxImage = (ImageView)view.findViewById(R.id.checkBoxImage);
        checkBoxImage.setVisibility(View.VISIBLE);
        
    }
    
    private void setUpEnquireLeagues(View view){

        if(getEnquiredLeague()!=null && getEnquiredLeague().length>0){
            ViewHelper viewHelper = new ViewHelper(getActivity());
            viewHelper.setUpLeagueImage(view, getEnquiredLeague()[0]);
            viewHelper.setUpLeageText(view, getEnquiredLeague()[0]);
            viewHelper.setLineColor(view, getEnquiredLeague()[0]);
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

    private LeagueModel getLeagueModel(){

        return (LeagueModel) getMitooModel(LeagueModel.class);
    }
    
    private League getFirstEnquriredLeague(){
        if(getEnquiredLeague()!= null && getEnquiredLeague().length>0)
            return getEnquiredLeague()[0];
        return null;
        
    }
        
    @Subscribe
    public void onUserInfoReceieve(UserInfoModelResponseEvent event){

        fireFragmentChangeAction(R.id.fragment_settings);

    }

}




