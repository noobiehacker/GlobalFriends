package co.mitoo.sashimi.views.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Build;
import android.os.Bundle;
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
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CompetitionSeasonReqByCompAndUserID;
import co.mitoo.sashimi.utils.events.CompetitionSeasonResponseEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LeagueRequestFromIDEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
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
    private Competition competition;
    private MitooEnum.FixtureTabType tabselected;
    private int competitionSeasonID = MitooConstants.invalidConstant;
    private boolean viewLoaded=false;
    private String leagueColor;

    @Override
    public void onClick(View v) {
    }

    public static CompetitionSeasonFragment newInstance() {

        CompetitionSeasonFragment fragment = new CompetitionSeasonFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.competitionSeasonID =  savedInstanceState.getInt(getCompetitionSeasonIdKey());
            this.leagueColor =  savedInstanceState.getString(getTeamColorKey());
            this.fragmentTitle =savedInstanceState.getString(getToolBarTitle());

        } else {
            this.competitionSeasonID = getArguments().getInt(getCompetitionSeasonIdKey());
            this.leagueColor= getArguments().getString(getTeamColorKey());
            this.fragmentTitle =getArguments().getString(getToolBarTitle());

        }

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

        super.onSaveInstanceState(bundle);
        bundle.putInt(getCompetitionSeasonIdKey(), this.competitionSeasonID);
        bundle.putString(getTeamColorKey(), this.leagueColor);
        bundle.putString(getToolBarTitle(), this.fragmentTitle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_competition,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        setUpTabs();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAdapter().notifyDataSetChanged();
    }

    @Subscribe
    public void onCompetitionLoaded(CompetitionSeasonResponseEvent event) {
        this.competition = event.getCompetition();
        loadTabs();
        updateView();

    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {

        Animator anim ;
        if(enter)
            anim = AnimatorInflater.loadAnimator(getActivity().getApplicationContext(), R.animator.enter_right);
        else
            anim = AnimatorInflater.loadAnimator(getActivity().getApplicationContext(), R.animator.exit_right);
        final boolean enterToPassIn = enter;
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(enterToPassIn){
                    int id = CompetitionSeasonFragment.this.competitionSeasonID;
                    BusProvider.post(new CompetitionSeasonReqByCompAndUserID(id, getUserID()));
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
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setMaterialsTabContainer((RelativeLayout) view.findViewById(R.id.materialTabContainer));
        RelativeLayout tabLayout = (RelativeLayout) getViewHelper().createViewFromInflator(R.layout.partial_competition_tabs);
        getMaterialsTabContainer().addView(tabLayout);
        setUpTabView(tabLayout);
        setUpPager(tabLayout);
        setProgressLayout((ProgressLayout) tabLayout.findViewById(R.id.progressLayout));
        this.viewLoaded=true;

        if (getToolbar() != null) {
            getToolbar().setBackgroundColor(getTeamColor());
            getToolbar().setTitle(getFragmentTitle());
            getTabHost().setPrimaryColor(getTeamColor());
        }

        updateView();


    }

    @Override
    protected void initializeFields() {

        super.initializeFields();
        if (this.competition != null)
            setFragmentTitle(this.competition.getName());

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);
    }

    @Override
    protected void handleHttpErrors(int statusCode) {

        if (statusCode == 404){
            //DO NOTHING
        }
        else
            super.handleHttpErrors(statusCode);
    }

    @Override
    protected void handleNetworkError() {

        if (getProgressLayout() != null) {

            centerProgressLayout();
            getProgressLayout().removeAllViews();
            getProgressLayout().addView(createNetworkFailureView());
        }

    }

    private void updateView() {

        if(this.viewLoaded && this.competition!=null){
            setFragmentTitle(this.competition.getName());
            if (getToolbar() != null) {
                getToolbar().setBackgroundColor(getTeamColor());
                getToolbar().setTitle(getFragmentTitle());
                getTabHost().setPrimaryColor(getTeamColor());
            }
            loadTabs();
        }

    }

    private void loadTabs() {

        if(this.viewLoaded && this.competition!=null){
            if(getPager().getAdapter()==null){
                setUpPagerAdapter();
                getPager().setCurrentItem(0, true);

            }
        }

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

            MitooTab mitooTab = new MitooTab(getDataHelper().getFixtureTabTypeFromIndex(tabIndex)
                    , this.competitionSeasonID, getCompetitionSeasonIdKey());
            mitooTab.setTab(tab);
            getMitooTabsList().add(mitooTab);
        }

        for (MitooTab item : getMitooTabsList()) {
            getTabHost().addTab(item.getTab());
        }

    }

    private void setUpPagerAdapter() {

        getPager().setAdapter(getAdapter());
        getPager().setOffscreenPageLimit(2);
        getPager().setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position <= getMitooTabsList().size() - 1)
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
        if (tab.getPosition() == 0)
            setTabselected(MitooEnum.FixtureTabType.FIXTURE_SCHEDULE);
        else if (tab.getPosition() == 0)
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
                                        .setBundle(createBundle())
                                        .build();
                                BusProvider.post(fragmentChangeEvent);
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
            teamColor = getViewHelper().getColor(this.leagueColor);
        }
        return teamColor;
    }

    public MitooTabAdapter getAdapter() {
        if (adapter == null) {
            android.app.FragmentManager fm = getFragmentManager();
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                fm = getChildFragmentManager();
            }
            adapter = new MitooTabAdapter(getMitooTabsList(), fm);
        }
        return adapter;
    }


    public void setAdapter(MitooTabAdapter adapter) {
        this.adapter = adapter;
    }

    public List<MitooTab> getMitooTabsList() {
        if (mitooTabsList == null)
            mitooTabsList = new ArrayList<MitooTab>();
        return mitooTabsList;
    }

    public void setMitooTabsList(List<MitooTab> mitooTabsList) {
        this.mitooTabsList = mitooTabsList;
    }

    public void resetFields() {
        setTabHost(null);
        setAdapter(null);
        setPager(null);
        setMitooTabsList(null);

    }

    public MitooEnum.FixtureTabType getTabselected() {
        if (tabselected == null)
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
    public void onDestroyView() {
        if (getMaterialsTabContainer() != null) {

            /*getAdapter().getFragmentManager().beginTransaction().remove(getAdapter().getItem(0));
            getAdapter().getFragmentManager().beginTransaction().remove(getAdapter().getItem(1));
            getAdapter().notifyDataSetChanged();*/
            getTabHost().removeAllViews();
            getMaterialsTabContainer().removeAllViews();
            this.pager=null;
            this.adapter = null;
        }
        super.onDestroyView();
    }


    private Bundle createBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt(getCompetitionSeasonIdKey(), this.competitionSeasonID);
        bundle.putInt(getTeamColorKey(), getTeamColor());
        return bundle;
    }

}