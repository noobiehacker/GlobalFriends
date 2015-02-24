package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.androidprogresslayout.ProgressLayout;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.managers.ModelManager;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
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
    private TextView noResultsView ;
    private boolean userHasUsedApp;

    @Override
    public void onClick(View v) {

        switch(v.getId()){
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
        setUpUserHasUsedAppBoolean();
        saveUserAsSecondTimeUser();
        updateEnquriedLeagueData();
    }


    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        setUpListView(view);
        setUpNoResultsTextView(view);

    }
    
    @Override
    protected void initializeOnClickListeners(View view) {

        super.initializeOnClickListeners(view);
        
    }

    @Override
    public void onResume(){

        requestLeagueData();
        super.onResume();

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
    
    private void requestLeagueData(){
        
        setLoading(true);
        LeagueModelEnquireRequestEvent event = new LeagueModelEnquireRequestEvent(
                getUserId(), MitooEnum.APIRequest.UPDATE);
        getLeagueModel().requestEnquiredLeagues(event);

    }

    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {

        setLoading(false);
        updateEnquriedLeagueData();
        if(getEnquiredLeagueData().size()!=0)
            getLeagueDataAdapter().notifyDataSetChanged();
        else
            getNoResultsView().setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onUserInfoReceieve(UserInfoModelResponseEvent event){

        fireFragmentChangeAction(R.id.fragment_settings , MitooEnum.FragmentTransition.PUSH , MitooEnum.FragmentAnimation.HORIZONTAL);

    }

    @Override
    protected void handleAndDisplayError(MitooActivitiesErrorEvent error) {
        getProgressLayout().showErrorText("");
        super.handleAndDisplayError(error);
    }
    

    public void updateEnquriedLeagueData(){

        if(getLeagueModel().getLeaguesEnquired()!=null){
            getDataHelper().clearList(getEnquiredLeagueData());
            getDataHelper().addToListList(getEnquiredLeagueData(), getLeagueModel().getLeaguesEnquired());
        }
    }

    public List<League> getEnquiredLeagueData() {
        if (enquiredLeagueData == null) {
            enquiredLeagueData= new ArrayList<League>();
        }
        return enquiredLeagueData;
    }

    private void setUpListView(View view){

        setLeagueList((ListView) view.findViewById(R.id.leagueListView));
        getLeagueList().setAdapter(getLeagueDataAdapter());
        getLeagueList().setOnItemClickListener(getLeagueDataAdapter());
        getLeagueList().addHeaderView(getViewHelper().createViewFromInflator(R.layout.view_enquired_league_text_view));
        if(!getUserHasUsedApp())
            getLeagueList().addFooterView(getViewHelper().createViewFromInflator(R.layout.view_enquired_league_footer));

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

        setNoResultsView((TextView)view.findViewById(R.id.noEnquiredTextView));

    }

    public LeagueAdapter getLeagueDataAdapter() {
        if(leagueDataAdapter==null)
            leagueDataAdapter = new LeagueAdapter(getActivity(), R.id.leagueListView, getEnquiredLeagueData(), this, false);
        return leagueDataAdapter;
    }

    public boolean getUserHasUsedApp() {
        return userHasUsedApp;
    }

    private void setUpUserHasUsedAppBoolean(){

        if(getAppSettingsModel().getUserHasUsedApp() == null)
            userHasUsedApp = false;
        else
            userHasUsedApp = getAppSettingsModel().getUserHasUsedApp().booleanValue();
        
    }
    
    public void saveUserAsSecondTimeUser(){
        
        getAppSettingsModel().saveUsedAppBoolean();
        
    }

    public ListView getLeagueList() {
        return leagueList;
    }

    public void setLeagueList(ListView leagueList) {
        this.leagueList = leagueList;
    }


    @Override
    public void setLoading(boolean loading) {
        this.loading = loading;
        if (this.loading) {
            getProgressLayout().showProgress();
        } else {
            getProgressLayout().showContent();
        }
    }

    public TextView getNoResultsView() {
        return noResultsView;
    }

    public void setNoResultsView(TextView noResultsView) {
        this.noResultsView = noResultsView;
    }
}
