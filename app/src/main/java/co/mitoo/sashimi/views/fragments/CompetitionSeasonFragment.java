package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.github.androidprogresslayout.ProgressLayout;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.events.FixtureModelResponseEvent;
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
    private int teamColor = MitooConstants.invalidConstant;
    private boolean teamModelLoaded = false;
    private boolean fixtureModelLoaded = false;
    private Competition selectedCompetition;

    @Override
    public void onClick(View v) {
    }

    public static CompetitionSeasonFragment newInstance() {
        CompetitionSeasonFragment fragment = new CompetitionSeasonFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_fixture,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setUpTabView(view);
        setUpPager(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        setPreDataLoading(true);

    }

    @Override
    protected void initializeFields() {

        super.initializeFields();
        setFragmentTitle(getSelectedCompetition().getName());
        setUpTabs();

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);
    }

    @Override
    public void onResume() {

        super.onResume();
        requestData();

    }

    @Override
    protected void requestData() {

        int competitionSeasonID = getSelectedCompetition().getFixed_competition_id();
        setTeamModelLoaded(false);
        setFixtureModelLoaded(false);
        getTeamModel().requestTeamByCompetition(competitionSeasonID, true);
        getFixtureModel().requestFixtureByCompetition(competitionSeasonID, true);

    }

    @Subscribe
    public void onFixtureResponse(FixtureModelResponseEvent event) {

        setFixtureModelLoaded(true);
        if (allDataLoaded())
            loadTabs();
    }

    @Subscribe
    public void onTeamResponse(TeamModelResponseEvent event) {

        setTeamModelLoaded(true);
        if (allDataLoaded())
            loadTabs();

    }

    private void loadTabs() {
        setUpPagerAdapter();
        setPreDataLoading(false);

    }


    private void setUpTabView(View view) {

        setTabHost((MaterialTabHost) view.findViewById(R.id.materialTabHost));
        setUpTabHost();

    }

    private void setUpTabHost() {
        tabHost.setPrimaryColor(getTeamColor());
    }

    private void setUpTabs() {

        String[] tabNames = getResources().getStringArray(R.array.competitions_tabs_array);
        for (int tabIndex = 0; tabIndex < tabNames.length; tabIndex++) {

            String tabName = tabNames[tabIndex];

            MitooMaterialsTab tab = new MitooMaterialsTab(getMitooActivity(), false);
            tab.setText(tabName);
            tab.setTabListener(this);
            getTabHost().addTab(tab);

            MitooTab mitooTab = new MitooTab(getDataHelper().getFixtureTabTypeFromIndex(tabIndex));
            mitooTab.setTab(tab);
            getMitooTabsList().add(mitooTab);

        }

    }

    private void setUpPagerAdapter() {

        getPager().setAdapter(getAdapter());
        getPager().setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position<getMitooTabsList().size()-1)
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

                    if (getDataHelper().isClickable()) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_notification:
                                fireFragmentChangeAction(R.id.fragment_notification);
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
            String teamColorString = getSelectedCompetition().getLeague().getColor_1();
            teamColor = getViewHelper().getColor(teamColorString);
        }

        return teamColor;
    }

    public MitooTabAdapter getAdapter() {
        if(adapter == null)
            adapter = new MitooTabAdapter(getMitooTabsList(), getFragmentManager());
        return adapter;
    }

    public List<MitooTab> getMitooTabsList() {
        if(mitooTabsList==null)
            mitooTabsList = new ArrayList<MitooTab>();
        return mitooTabsList;
    }

    public boolean allDataLoaded() {
        return isTeamModelLoaded() && isFixtureModelLoaded();
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
}
