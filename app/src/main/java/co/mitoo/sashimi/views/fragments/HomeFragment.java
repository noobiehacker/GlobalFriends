package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.os.Handler;
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
    private ListView enquiredLeagueList;
    private LeagueAdapter enquiredLeagueDataAdapter;

    private List<League> myLeagueData;
    private ListView myLeagueList;
    private LeagueAdapter myLeagueDataAdapter;

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
        refreshEnquriedLeagueData();
        setUpPopUpTask();
    }


    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        setUpEnquiredListView(view, getString(R.string.home_page_text_1));
        setUpMyLeagueListView(view, getString(R.string.home_page_text_5));
        setUpNoResultsTextView(view);
        if(getLeagueModel().getLeaguesEnquired().size()==0)
            setLoading(true);
    }
    
    @Override
    protected void initializeOnClickListeners(View view) {

        super.initializeOnClickListeners(view);
        
    }

    @Override
    public void onResume(){

        super.onResume();
        requestLeagueData();

    }

    @Override
    protected Toolbar setUpToolBar(View view) {

        setToolbar(super.setUpToolBar(view));
        if(getToolbar()!=null){

            getToolbar().setLogo(R.drawable.header_mitoo_logo);
            getToolbar().setTitle("");
            getToolbar().inflateMenu(R.menu.menu_main);
            getToolbar().setPopupTheme(R.style.MyPopupMenu);
            getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    if (getDataHelper().isClickable()) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_feedback:
                                FeedBackDialogBuilder dialog = new FeedBackDialogBuilder(getActivity());
                                dialog.buildPrompt().show();
                                break;
                            case R.id.menu_settings:
                                BusProvider.post(new UserInfoModelRequestEvent(getUserId()));
                                break;
                            case R.id.menu_search:
                                fireFragmentChangeAction(R.id.fragment_search, MitooEnum.FragmentTransition.PUSH, MitooEnum.FragmentAnimation.HORIZONTAL);
                                break;
                        }
                    }
                    return false;
                }
            });
        }
        return getToolbar();
    }

    @Override
    public void tearDownReferences(){

        super.tearDownReferences();
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

        LeagueModelEnquireRequestEvent event = new LeagueModelEnquireRequestEvent(
                getUserId(), MitooEnum.APIRequest.UPDATE);
        getLeagueModel().requestEnquiredLeagues(event);

    }

    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {

        setLoading(false);
        updateEnqureLeagueListView();
        updateMyLeagueListView();
        updateNoResultsView();
        saveUserAsSecondTimeUser();

    }

    private void updateEnqureLeagueListView(){

        refreshEnquriedLeagueData();
        if(!getUserHasUsedApp())
            setUpListFooter(getEnquiredLeagueList(), R.layout.view_league_list_footer, getString(R.string.home_page_text_4));

    }

    private void updateMyLeagueListView() {

        if (getMyLeagueData().size() == 0) {
            setUpListFooter(getMyLeagueList(), R.layout.view_league_list_footer, getString(R.string.home_page_text_6));
        } else {
            getMyLeagueDataAdapter().notifyDataSetChanged();
        }

    }

    private void updateNoResultsView() {

        if(getEnquiredLeagueData().size()!=0){
            getNoResultsView().setVisibility(View.VISIBLE);
        }

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
    

    public void refreshEnquriedLeagueData(){

        if(getLeagueModel().getLeaguesEnquired()!=null){
            getDataHelper().clearList(getEnquiredLeagueData());
            getDataHelper().addToListList(getEnquiredLeagueData(), getLeagueModel().getLeaguesEnquired());
        }
        getEnquiredLeagueDataAdapter().notifyDataSetChanged();

    }

    public List<League> getEnquiredLeagueData() {
        if (enquiredLeagueData == null) {
            enquiredLeagueData= new ArrayList<League>();
        }
        return enquiredLeagueData;
    }

    private void setUpEnquiredListView(View view, String headerText){

        setEnquiredLeagueList((ListView) view.findViewById(R.id.enquiredLeagueListView));
        getEnquiredLeagueList().setAdapter(getEnquiredLeagueDataAdapter());
        getEnquiredLeagueList().setOnItemClickListener(getEnquiredLeagueDataAdapter());
        setUpListHeader(getEnquiredLeagueList() , R.layout.view_league_list_header, headerText);

    }

    private void setUpMyLeagueListView(View view, String headerText){

        setMyLeagueList((ListView) view.findViewById(R.id.myleagueListView));
        getMyLeagueList().setAdapter(getMyLeagueDataAdapter());
        getMyLeagueList().setOnItemClickListener(getMyLeagueDataAdapter());
        setUpListHeader(getMyLeagueList(), R.layout.view_league_list_header, headerText);

    }

    private void setUpListHeader(ListView listView , int headerLayout , String headerText){

        View holder = getViewHelper().createHeadFooterView(headerLayout, R.id.header_text, headerText);
        listView.addHeaderView(holder);
    }

    private void setUpListFooter(ListView listView , int footerLayout , String footerText) {

        View holder = getViewHelper().createHeadFooterView(footerLayout, R.id.header_text, footerText);
        listView.addFooterView(holder);
    }

    private void setUpNoResultsTextView(View view){

        setNoResultsView((TextView)view.findViewById(R.id.noEnquiredTextView));

    }

    public LeagueAdapter getEnquiredLeagueDataAdapter() {
        if(enquiredLeagueDataAdapter ==null)
            enquiredLeagueDataAdapter = new LeagueAdapter(getActivity(), R.id.enquiredLeagueListView, getEnquiredLeagueData(), this, false);
        return enquiredLeagueDataAdapter;
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

    public ListView getEnquiredLeagueList() {
        return enquiredLeagueList;
    }

    public void setEnquiredLeagueList(ListView enquiredLeagueList) {
        this.enquiredLeagueList = enquiredLeagueList;
    }


    public TextView getNoResultsView() {
        return noResultsView;
    }

    public void setNoResultsView(TextView noResultsView) {
        this.noResultsView = noResultsView;
    }

    public List<League> getMyLeagueData() {
        if (myLeagueData == null) {
            myLeagueData= new ArrayList<League>();
        }
        return myLeagueData;
    }

    public ListView getMyLeagueList() {
        return myLeagueList;
    }

    public void setMyLeagueList(ListView myLeagueList) {
        this.myLeagueList = myLeagueList;
    }

    public LeagueAdapter getMyLeagueDataAdapter() {
        if(myLeagueDataAdapter ==null)
            myLeagueDataAdapter  = new LeagueAdapter(getActivity(), R.id.enquiredLeagueListView, getMyLeagueData(), this, false);
        return myLeagueDataAdapter ;
    }

    private void setUpPopUpTask(){

        if(!getUserHasUsedApp() && !getDataHelper().feedBackHasAppeared()){
            getDataHelper().setConfirmFeedBackPopped(true);
            Handler handler = getHandler();
            setRunnable( new Runnable() {
                @Override
                public void run() {
                    FeedBackDialogBuilder dialog = new FeedBackDialogBuilder(getActivity());
                    dialog.buildPrompt().show();
                }
            });
            handler.postDelayed(getRunnable(), MitooConstants.feedBackPopUpTime);
        }
    }


}
