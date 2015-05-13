package co.mitoo.sashimi.views.fragments;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.github.androidprogresslayout.ProgressLayout;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.services.EventTrackingService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CompetitionListResponseEvent;
import co.mitoo.sashimi.utils.events.CompetitionRequestByUserID;
import co.mitoo.sashimi.utils.events.CompetitionSeasonReqByCompAndUserID;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.LeaguesAlreadyEnquiredRequest;
import co.mitoo.sashimi.utils.events.LogOutNetworkCompleteEevent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.UserInfoRequestEvent;
import co.mitoo.sashimi.utils.events.UserInfoResponseEvent;
import co.mitoo.sashimi.views.Dialog.FeedBackDialogBuilder;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.adapters.CompetitionAdapter;
import co.mitoo.sashimi.views.adapters.LeagueAdapter;
import co.mitoo.sashimi.views.widgets.HeaderListView;

/**
 * Created by david on 15-01-12.
 */

public class HomeFragment extends MitooFragment {

    private List<League> enquiredLeagueData;
    private HeaderListView enquiredLeagueList;
    private LeagueAdapter enquiredLeagueDataAdapter;
    private List<Competition> myCompetitionData;
    private HeaderListView  myCompetitionList;
    private CompetitionAdapter myCompetitionDataAdapter;
    private View myCompetitionListFooterView;
    private boolean userHasUsedApp;
    private boolean enquriedLeagueDataLoaded;
    private boolean myCompetitionDataLoaded;
    private boolean logOutEventFired;
    private int userID = MitooConstants.invalidConstant;

    private MitooEnum.MenuItemSelected menuItemSelected = MitooEnum.MenuItemSelected.NONE;

    @Override
    public void onClick(View v) {

        switch(v.getId()){
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            this.userID = savedInstanceState.getInt(getUserIDKey());
        }else{
            this.userID = getArguments().getInt(getUserIDKey());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(getUserIDKey(), this.userID);
        super.onSaveInstanceState(bundle);

    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_home,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);

        // Track this event
        EventTrackingService.userViewedHomeScreen(userID);

        return view;
    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setUpUserHasUsedAppBoolean();
        refreshEnquriedLeagueData();
        refreshMyCompetitionData();
        if(this.userID== MitooConstants.invalidConstant)
            this.userID =getUserID();
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        setUpEnquiredListView(view, getString(R.string.home_page_text_1));
        setUpMyCompetitionListView(view, getString(R.string.home_page_text_5));
        setPreDataLoading(true);
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {

        Animator anim  = AnimatorInflater.loadAnimator(getActivity().getApplicationContext(), nextAnim);
        final boolean enterToPassIn = enter;
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(enterToPassIn){
                    HomeFragment.this.requestData();
                    HomeFragment.this.updateMenu();
                    HomeFragment.this.updateListViews();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return anim;
    }
    
    @Override
    protected void initializeOnClickListeners(View view) {

        super.initializeOnClickListeners(view);
        
    }

    @Override
    protected Toolbar setUpToolBar(View view) {

        setToolbar((Toolbar)view.findViewById(R.id.app_bar));
        if(getToolbar()!=null){

            getToolbar().setLogo(R.drawable.header_mitoo_logo);
            getToolbar().setTitle("");
            getToolbar().inflateMenu(R.menu.menu_main);
            getToolbar().setPopupTheme(R.style.MyPopupMenu);
            getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    if (getDataHelper().isClickable(menuItem.getItemId())) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_feedback:
                                setMenuItemSelected(MitooEnum.MenuItemSelected.FEEDBACK);
                                BusProvider.post(new UserInfoRequestEvent(getUserID()));
                                break;
                            case R.id.menu_settings:
                                setMenuItemSelected(MitooEnum.MenuItemSelected.SETTINGS);
                                setLoading(true);
                                BusProvider.post(new UserInfoRequestEvent(getUserID()));
                                break;
                            case R.id.menu_search:
                                FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                                        .setFragmentID(R.id.fragment_search)
                                        .setTransition(MitooEnum.FragmentTransition.PUSH)
                                        .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                                        .build();
                                BusProvider.post(fragmentChangeEvent);
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
    protected void handleNetworkError(){
        if (getMitooActivity()!=null && !getMitooActivity().NetWorkConnectionIsOn()){
            if(getMitooActivity().topFragmentType()!=null && getMitooActivity().topFragmentType()==HomeFragment.class)
                displayTextWithToast(getString(R.string.error_no_internet));
        }
    }

    @Override
    protected void handleHttpErrors(int statusCode) {

        if(statusCode == 401)
            handleAuth401Error();
        if (statusCode == 404){
            //DO NOTHING
        }
        else
            super.handleHttpErrors(statusCode);
    }

    @Override
    public void tearDownReferences(){

        super.tearDownReferences();
    }


    @Override
    protected void requestData(){

        setEnquriedLeagueDataLoaded(false);
        setMyCompetitionDataLoaded(false);
        requestLeagueData();
        requestCompetitionData();
    }

    private void requestLeagueData(){

        getLeagueModel();
        BusProvider.post(new LeaguesAlreadyEnquiredRequest(this.userID));

    }

    private void requestCompetitionData() {

        getCompetitionModel();
        BusProvider.post(new CompetitionRequestByUserID(this.userID));

    }

    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {

        setEnquriedLeagueDataLoaded(true);
        saveUserAsSecondTimeUser();
        this.enquiredLeagueData=event.getEnquiredLeagues();
        updateEnqureLeagueListView();
        updateListViews();
    }

    @Subscribe
    public void onCompetitionResponse(CompetitionListResponseEvent event) {

        setMyCompetitionDataLoaded(true);
        this.myCompetitionData=event.getCometitions();
        updateMyCompetitionListView();
        updateListViews();

    }

    private void updateListViews(){

        if(enquriedLeagueDataHasLoaded() && myCompetitionDataHasLoaded()){
            setPreDataLoading(false);
            updateMenu();
        }

    }

    private void updateEnqureLeagueListView(){

        refreshEnquriedLeagueData();
        if(this.enquiredLeagueData == null || this.enquiredLeagueData.isEmpty()){
            this.enquiredLeagueList.setVisibility(View.GONE);
            this.enquiredLeagueList.setHeaderVisibility(View.GONE);
        }else{
            this.enquiredLeagueList.setVisibility(View.VISIBLE);
            this.enquiredLeagueList.setHeaderVisibility(View.VISIBLE);
        }

    }

    private void updateMyCompetitionListView(){

        refreshMyCompetitionData();
        updateMyCompetitionListFooter();
        if(this.myCompetitionData == null || this.myCompetitionData.isEmpty()){
            this.myCompetitionList.setVisibility(View.GONE);
            this.myCompetitionList.setHeaderVisibility(View.GONE);
        }else{
            this.myCompetitionList.setVisibility(View.VISIBLE);
            this.myCompetitionList.setHeaderVisibility(View.VISIBLE);

        }

    }

    private void updateMyCompetitionListFooter(){
        if(getMyCompetitionData().isEmpty()){
            View footerView = getViewHelper().setUpListFooter(this.myCompetitionList
                    ,R.layout.view_league_list_footer, getString(R.string.home_page_text_6));
            setMyCompetitionListFooterView(footerView);
        }else{
            if(getMyCompetitionListFooterView()!=null)
                getMyCompetitionListFooterView().setVisibility(View.GONE);
        }
    }

    private void updateMenu(){
        Menu menu = (Menu)getToolbar().getMenu();
        if(!getMyCompetitionData().isEmpty() && menu!=null){
            menu.removeItem(R.id.menu_search);
        }
    }

    @Subscribe
    public void onUserInfoReceieve(UserInfoResponseEvent event){

        switch(getMenuItemSelected()){
            case SETTINGS:
                FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                        .setFragmentID(R.id.fragment_settings)
                        .setTransition(MitooEnum.FragmentTransition.PUSH)
                        .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                        .build();
                BusProvider.post(fragmentChangeEvent);
                break;

            case FEEDBACK:
                FeedBackDialogBuilder dialog = new FeedBackDialogBuilder(getActivity());
                dialog.buildPrompt().show();
                break;
        }
    }

    @Override
    protected void handleAndDisplayError(MitooActivitiesErrorEvent error) {
        super.handleAndDisplayError(error);

    }

    public void refreshEnquriedLeagueData(){

        if(this.enquiredLeagueData!=null){
            getEnquiredLeagueDataAdapter().clear();
            getEnquiredLeagueDataAdapter().addAll(this.enquiredLeagueData);
            getEnquiredLeagueDataAdapter().notifyDataSetChanged();
        }

    }

    public void refreshMyCompetitionData(){

        if(this.myCompetitionData!=null){
            getMyCompetitionDataAdapter().clear();
            getMyCompetitionDataAdapter().addAll(this.myCompetitionData);
            getMyCompetitionDataAdapter().notifyDataSetChanged();
        }

    }

    private void setUpEnquiredListView(View view, String headerText){

        this.enquiredLeagueList = (HeaderListView) view.findViewById(R.id.enquiredLeagueListView);
        getViewHelper().setUpListView(this.enquiredLeagueList,
                getEnquiredLeagueDataAdapter(), headerText, getEnquiredLeagueDataAdapter());

    }

    private void setUpMyCompetitionListView(View view, String headerText){

        this.myCompetitionList = (HeaderListView) view.findViewById(R.id.myLeagueListView);
        getViewHelper().setUpListView(this.myCompetitionList,
                getMyCompetitionDataAdapter(), headerText, getMyCompetitionDataAdapter());

    }

    public LeagueAdapter getEnquiredLeagueDataAdapter() {
        if(enquiredLeagueDataAdapter ==null)
            enquiredLeagueDataAdapter = new LeagueAdapter(getActivity(), R.id.enquiredLeagueListView, new ArrayList<League>(), this);
        return enquiredLeagueDataAdapter;
    }

    private void setUpUserHasUsedAppBoolean() {

        //DEFENSIVE PROGRAMING JUST IN CASE modelManger got killed
        if (this.modelMangerExists() && getAppSettingsModel().getUserHasUsedApp() != null)
            userHasUsedApp = getAppSettingsModel().getUserHasUsedApp().booleanValue();
        else
            userHasUsedApp = false;

    }
    
    public void saveUserAsSecondTimeUser(){
        
        getAppSettingsModel().saveUsedAppBoolean();
        setUpUserHasUsedAppBoolean();
    }

    @Override
    protected void handleAuth401Error(){
        if(!isDuringConfirmFlow()&&! isLogOutEventFired())
            BusProvider.post(new LogOutNetworkCompleteEevent());
    }

    public List<Competition> getMyCompetitionData() {
        if (myCompetitionData == null) {
            myCompetitionData = new ArrayList<Competition>();
        }
        return myCompetitionData;
    }


    public CompetitionAdapter getMyCompetitionDataAdapter() {
        if (myCompetitionDataAdapter == null)
            myCompetitionDataAdapter = new CompetitionAdapter(getActivity(), R.id.myLeagueListView, getMyCompetitionData(), this);
        return myCompetitionDataAdapter;
    }

    public MitooEnum.MenuItemSelected getMenuItemSelected() {
        return menuItemSelected;
    }

    public void setMenuItemSelected(MitooEnum.MenuItemSelected menuItemSelected) {
        this.menuItemSelected = menuItemSelected;
    }

    public View getMyCompetitionListFooterView() {
        return myCompetitionListFooterView;
    }

    public void setMyCompetitionListFooterView(View myCompetitionListFooterView) {
        this.myCompetitionListFooterView = myCompetitionListFooterView;
    }

    public boolean enquriedLeagueDataHasLoaded() {
        return enquriedLeagueDataLoaded;
    }

    public void setEnquriedLeagueDataLoaded(boolean enquriedLeagueDataLoaded) {
        this.enquriedLeagueDataLoaded = enquriedLeagueDataLoaded;
    }

    public boolean myCompetitionDataHasLoaded() {
        return myCompetitionDataLoaded;
    }

    public void setMyCompetitionDataLoaded(boolean myCompetitionDataLoaded) {
        this.myCompetitionDataLoaded = myCompetitionDataLoaded;
    }

    public boolean isLogOutEventFired() {
        return logOutEventFired;
    }

    private boolean modelMangerExists(){
        //DEFENSIVE PROGRAMMING TO MAKE SURE MODELMANAGER EXSIST
        MitooActivity activity = getMitooActivity();
        if(activity!=null){
            return activity.getModelManager() !=null;
        }
        return false;
    }

}