package co.mitoo.sashimi.views.fragments;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.androidprogresslayout.ProgressLayout;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FixtureModelResponseEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.TeamModelResponseEvent;
import co.mitoo.sashimi.views.adapters.MitooTabAdapter;
import co.mitoo.sashimi.views.widgets.MitooMaterialsTab;
import co.mitoo.sashimi.views.widgets.MitooTab;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by david on 15-03-06.
 */

public class CompetitionSeasonFragment extends MitooFragment implements MaterialTabListener {

    private MaterialTabHost tabHost;
    private ViewPager pager;
    private MitooTabAdapter adapter;
    private List<MitooTab> mitooTabsList;
    private RelativeLayout materialsTabContainer;
    private int teamColor = MitooConstants.invalidConstant;
    private boolean teamModelLoaded = false;
    private boolean fixtureModelLoaded = false;
    private Competition selectedCompetition;
    private MitooEnum.FixtureTabType tabselected;
    private boolean tabLoaded = false;

    @Override
    public void onClick(View v) {
    }

    public static CompetitionSeasonFragment newInstance() {
        CompetitionSeasonFragment fragment = new CompetitionSeasonFragment();
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_competition,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        setUpTabs();
        setPreDataLoading(true);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        setUpTabFromPreviousState();
        if(isBackClicked())
            requestData();
        //QUICK FIX REFACTOR LATER
        //Naming convention of isLoading should be hasLoaded! REFACTOR
        if(!isLoading() || isBackClicked())
            loadTabs();
    }

    private void setUpTabFromPreviousState(){
        if(getTabselected()== MitooEnum.FixtureTabType.FIXTURE_SCHEDULE)
            getPager().setCurrentItem(0);
        else if(getTabselected()== MitooEnum.FixtureTabType.FIXTURE_RESULT)
            getPager().setCurrentItem(1);
    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setMaterialsTabContainer((RelativeLayout)view.findViewById(R.id.materialTabContainer));
        RelativeLayout tabLayout =  (RelativeLayout)getViewHelper().createViewFromInflator(R.layout.partial_competition_tabs);
        getMaterialsTabContainer().addView(tabLayout);

        setUpTabView(tabLayout);
        setUpPager(tabLayout);
        setProgressLayout((ProgressLayout) tabLayout.findViewById(R.id.progressLayout));

    }

    @Override
    protected void initializeFields() {

        super.initializeFields();
        if(getSelectedCompetition()!=null)
            setFragmentTitle(getSelectedCompetition().getName());

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);
    }

    @Override
    protected void handleNetworkError() {

        if (getProgressLayout() != null) {

            centerProgressLayout();
            getProgressLayout().removeAllViews();
            getProgressLayout().addView(createNetworkFailureView());
        }

    }

    @Override
    protected void requestData() {

        setPreDataLoading(true);
        setFixtureModelLoaded(false);
        setTeamModelLoaded(false);
        setTeamModelLoaded(false);
        setFixtureModelLoaded(false);
        if(getSelectedCompetition()!=null){
            int competitionSeasonID = getSelectedCompetition().getId();
            getTeamModel().requestTeamByCompetition(competitionSeasonID, true);
            getFixtureModel().requestFixtureByCompetition(competitionSeasonID, true);
        }

    }

    @Subscribe
    public void onFixtureResponse(FixtureModelResponseEvent event) {

        setFixtureModelLoaded(true);
        attemptToDisplayData();

    }

    @Subscribe
    public void onTeamResponse(TeamModelResponseEvent event) {

        setTeamModelLoaded(true);
        attemptToDisplayData();

    }

    private void attemptToDisplayData(){
        if (allDataLoaded())
            updateView();
    }

    private void updateView() {

        loadTabs();
        setFragmentTitle(getSelectedCompetition().getName());
        getToolbar().setBackgroundColor(getTeamColor());
        getToolbar().setTitle(getFragmentTitle());

    }

    private void loadTabs() {

        //QUICK FIX, REFACTOR
        setRunnable(new Runnable() {
            @Override
            public void run() {
                //resetAdapter();
                setUpPagerAdapter();
                setPreDataLoading(false);
                if(isBackClicked()){
                    getPager().setCurrentItem(0 ,true);
                }
                /*
                android.app.FragmentManager fm =getAdapter().getFragmentManager();
                fm.beginTransaction();
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                    android.app.FragmentManager childFM = getChildFragmentManager();
                    FragmentTransaction ft = childFM.beginTransaction();
                    ft.replace(R.id.pager , getAdapter().getItem(0));
                    ft.commit();
                }*/
            }
        });
        getHandler().postDelayed(getRunnable(), MitooConstants.durationMedium);

    }

    private void setUpTabView(View view) {

        setTabHost((MaterialTabHost) view.findViewById(R.id.materialTabHost));
        getTabHost().setPrimaryColor(getTeamColor());

    }

    private void setUpTabs() {

        getDataHelper().clearList(getMitooTabsList());
        String[] tabNames = getResources().getStringArray(R.array.competitions_tabs_array);
        for (int tabIndex = 0; tabIndex < tabNames.length; tabIndex++) {

            String tabName = tabNames[tabIndex];

            MitooMaterialsTab tab = new MitooMaterialsTab(getMitooActivity(), false);
            tab.setText(tabName);
            tab.setTabListener(this);

            MitooTab mitooTab = new MitooTab(getDataHelper().getFixtureTabTypeFromIndex(tabIndex));
            mitooTab.setTab(tab);
            getMitooTabsList().add(mitooTab);
        }

        for (MitooTab item : getMitooTabsList()) {
            getTabHost().addTab(item.getTab());
        }

    }

    private void setUpPagerAdapter() {

        getPager().setAdapter(getAdapter());
        getPager().setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position<=getMitooTabsList().size()-1)
                    getTabHost().setSelectedNavigationItem(position);
            }
        });

    }

    private void setUpPager(View view) {

        setPager((ViewPager) view.findViewById(R.id.pager));

    }

    @Override
    public void onTabSelected(MaterialTab tab) {

        if (getPager() != null && !isLoading()) {
            getPager().setCurrentItem(tab.getPosition());
        }
        if(tab.getPosition()==0)
            setTabselected(MitooEnum.FixtureTabType.FIXTURE_SCHEDULE);
        else if(tab.getPosition()==0)
            setTabselected(MitooEnum.FixtureTabType.FIXTURE_RESULT);
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {
    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {
    }

    @Override
    protected Toolbar setUpToolBar(View view) {

        setToolbar((Toolbar) view.findViewById(R.id.app_bar));
        if (getToolbar() != null) {

            getToolbar().setBackgroundColor(getTeamColor());
            getToolbar().setTitle(getFragmentTitle());
            getToolbar().inflateMenu(R.menu.menu_with_notification);
            getToolbar().setNavigationIcon(R.drawable.header_back_icon);
            getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    if (getDataHelper().isClickable(menuItem.getItemId())) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_notification:
                                FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                                        .setFragmentID(R.id.fragment_notification)
                                        .setTransition(MitooEnum.FragmentTransition.PUSH)
                                        .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                                        .build();
                                postFragmentChangeEvent(fragmentChangeEvent);
                                break;
                        }
                    }
                    return false;
                }
            });
            setUpBackButtonClickListner();

        }
        return getToolbar();
    }

    public ViewPager getPager() {
        return pager;
    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }

    public MaterialTabHost getTabHost() {
        return tabHost;
    }


    public void setTabHost(MaterialTabHost tabHost) {
        this.tabHost = tabHost;
    }

    public int getTeamColor() {

        if (teamColor == MitooConstants.invalidConstant) {
            if(getCompetitionModel().getSelectedCompetition()!=null){
                String teamColorString = getSelectedCompetition().getLeague().getColor_1();
                teamColor = getViewHelper().getColor(teamColorString);
            }
        }

        return teamColor;
    }

    public MitooTabAdapter getAdapter() {
        if(adapter == null){
            android.app.FragmentManager fm = getFragmentManager();
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                fm  = getChildFragmentManager();
            }
            adapter = new MitooTabAdapter(getMitooTabsList(), fm);
        }
        return adapter;
    }

    public void resetAdapter(){
        getFragmentManager().beginTransaction().remove(getAdapter().getItem(0));
        getFragmentManager().beginTransaction().remove(getAdapter().getItem(1));
        adapter = new MitooTabAdapter(getMitooTabsList(), getFragmentManager());

    }

    public void setAdapter(MitooTabAdapter adapter) {
        this.adapter = adapter;
    }

    public List<MitooTab> getMitooTabsList() {
        if(mitooTabsList==null)
            mitooTabsList = new ArrayList<MitooTab>();
        return mitooTabsList;
    }

    public void setMitooTabsList(List<MitooTab> mitooTabsList) {
        this.mitooTabsList = mitooTabsList;
    }

    public boolean allDataLoaded() {
        return isTeamModelLoaded() && isFixtureModelLoaded() ;
    }

    public boolean isTeamModelLoaded() {
        return teamModelLoaded;
    }

    public void setTeamModelLoaded(boolean teamModelLoaded) {
        this.teamModelLoaded = teamModelLoaded;
    }

    public boolean isFixtureModelLoaded() {
        return fixtureModelLoaded;
    }

    public void setFixtureModelLoaded(boolean fixtureModelLoaded) {
        this.fixtureModelLoaded = fixtureModelLoaded;
    }

    public Competition getSelectedCompetition() {
        if(selectedCompetition==null)
            selectedCompetition= getCompetitionModel().getSelectedCompetition();
        return selectedCompetition;
    }

    public void resetFields(){
        setTabHost(null);
        setAdapter(null);
        setPager(null);
        setMitooTabsList(null);

    }

    public MitooEnum.FixtureTabType getTabselected() {
        if(tabselected==null)
            tabselected = MitooEnum.FixtureTabType.FIXTURE_SCHEDULE;
        return tabselected;
    }

    public void setTabselected(MitooEnum.FixtureTabType tabselected) {
        this.tabselected = tabselected;
    }

    public RelativeLayout getMaterialsTabContainer() {
        return materialsTabContainer;
    }

    public void setMaterialsTabContainer(RelativeLayout materialsTabContainer) {
        this.materialsTabContainer = materialsTabContainer;
    }

    @Override
    public void onDestroyView (){
        if(getMaterialsTabContainer()!=null){
            getTabHost().removeAllViews();
            getMaterialsTabContainer().removeAllViews();
        }
        super.onDestroyView();
    }

    public boolean isTabLoaded() {
        return tabLoaded;
    }

    public void setTabLoaded(boolean tabLoaded) {
        this.tabLoaded = tabLoaded;
    }
}
