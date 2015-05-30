package co.mitoo.sashimi.views.fragments;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.github.androidprogresslayout.ProgressLayout;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.appObject.StandingsRow;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.TeamViewModel;
import co.mitoo.sashimi.utils.events.LoadStandingsEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.StandingsLoadedEvent;
import co.mitoo.sashimi.views.Listener.ScrollViewListener;
import co.mitoo.sashimi.views.adapters.ScoreGridAdapter;
import co.mitoo.sashimi.views.adapters.StandingsTeamAdapter;
import co.mitoo.sashimi.views.widgets.HeaderListView;
import co.mitoo.sashimi.views.widgets.ObservableScrollView;

/**
 * Created by david on 15-04-23.
 */
public class StandingsFragment extends MitooFragment implements ScrollViewListener {

    private int competitionSeasonID;
    private TableLayout teamTable;
    private TeamViewModel teamViewModel;
    private ObservableScrollView leftScrollView;
    private ObservableScrollView rightScrollView;
    private List<StandingsRow> standingsRows;
    private ProgressLayout rightViewProgressLayout;
    private boolean dataRequested = false;
    private boolean dataReceived = false;
    private boolean tabClicked = false;
    private boolean tableLoaded;
    private RecyclerView recyclerView;
    private ScoreGridAdapter gridAdapter;
    private HeaderListView teamListView;
    private StandingsTeamAdapter teamAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.competitionSeasonID = (int) savedInstanceState.get(getCompetitionSeasonIdKey());

        } else {
            this.competitionSeasonID = getArguments().getInt(getCompetitionSeasonIdKey());
        }

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(getCompetitionSeasonIdKey(), this.competitionSeasonID);
        super.onSaveInstanceState(bundle);

    }

    public static StandingsFragment newInstance() {
        StandingsFragment fragment = new StandingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_standings,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        setUpGridScoreView();
        return view;

    }

    private void setUpGridScoreView() {
        final StaggeredGridLayoutManager recyclerManager = new StaggeredGridLayoutManager( 5, StaggeredGridLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(recyclerManager);
    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        this.tabClicked = false;
//        this.scoreHeaderTable = (TableLayout) view.findViewById(R.id.scoreHeaderView);
        //      this.rightViewProgressLayout = (ProgressLayout) view.findViewById(R.id.rightTableProgressLayout);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.grid_score_view);
        this.teamListView = (HeaderListView) view.findViewById(R.id.team_list_view);
        setPreDataLoading(true);

    }

    @Override
    protected void initializeFields() {

        super.initializeFields();
        this.tableLoaded = false;

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);

    }

    @Override
    protected void requestData() {
        setPreDataLoading(true);
        BusProvider.post(new LoadStandingsEvent(StandingsFragment.this.competitionSeasonID));
        this.dataRequested = true;
    }

    @Override
    public void onResume() {

        super.onResume();
        BusProvider.post(new LoadStandingsEvent(StandingsFragment.this.competitionSeasonID));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Subscribe
    public void onStandingsLoaded(StandingsLoadedEvent event) {

        this.dataReceived = true;
        this.standingsRows = event.getStandingRows();
        setUpScoreTable(getScoreDataFromStandingsRows(this.standingsRows),5);

    }

    private List<String> getScoreDataFromStandingsRows(List<StandingsRow> standingsRows) {

        List<String> result = new ArrayList<String>();
        boolean firstItem = true;

        for (StandingsRow item : standingsRows) {
/*
            if (firstItem) {
                result.add(getString(R.string.standing_page_header_text));
                firstItem = !firstItem;
            } else {
                Team team = getDataHelper().getTeam(item.getId());
                if (team == null) {

                    team = new Team();
                    team.setName("Hello Team");
                    team.setLogo_small("http://www.bet.com/news/sports/photos/sports-buzz/2014/04/sports-buzz-4-27-5-3/_jcr_content/leftcol/flipbook/flipbookimage.flipfeature.dimg/052913-sports-teams-logo-golden-state-warriors.jpg");
                }

                result.add(team.getName());
            }
            result.addAll(item.getScore());*/

            result.addAll(item.getScore());

        }

        return result;
    }

    private void setUpScoreTable(List<String> scoreData, int rowCount) {

        this.gridAdapter = new ScoreGridAdapter(scoreData, rowCount);
        this.recyclerView.setAdapter(this.gridAdapter);
        this.teamAdapter = new StandingsTeamAdapter(getActivity(), R.id.team_list_view, this.standingsRows,this);
        this.teamListView.setAdapter(this.teamAdapter);

   /*     this.rightScrollView = (ObservableScrollView) getActivity().getLayoutInflater().inflate(R.layout.view_standings_data_table, null);

        this.dataTable = (TableLayout) this.rightScrollView.findViewById(R.id.scoreTableView);

        for (int i = 0; i < listOfRows.size(); i++) {

            TableRow tableRow = null;
            StandingsRow item = listOfRows.get(i);

            if (item.getId() == MitooConstants.standingHead) {

                //Add Header
                tableRow = createRow(R.layout.standing_head_text_view, item.getScore());
                this.scoreHeaderTable.addView(tableRow);

                //Add a line for visual
                tableRow = new TableRow(getActivity());
                tableRow.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                tableRow.setMinimumHeight((int) getResources().getDimension(R.dimen.list_divider_height));
                this.scoreHeaderTable.addView(tableRow);

            } else {


                //Add Data row
                tableRow = createRow(R.layout.standing_score_text_view, item.getScore());
                tableRow.setBackgroundColor(getResources().getColor(R.color.white));
                dataTable.addView(tableRow);


                //Call Back to Dynamically Resize Columns

                if (i == listOfRows.size() - 1) {
                    final TableRow tableRowToPassIn = tableRow;
                    tableRowToPassIn.getViewTreeObserver().addOnGlobalLayoutListener(

                            new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {
                                    // Ensure you call it only once :
                                    tableRowToPassIn.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                    setRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            dynamicallyResizeColumns();
                                        }
                                    });
                                    getHandler().postDelayed(getRunnable(), MitooConstants.durationMedium);

                                }
                            });
                }



            }

        }

        this.scoreHeaderTable.addView(this.rightScrollView);
                syncScrollViews();

        */
        setPreDataLoading(false);

    }

    @Override
    public void tearDownReferences() {

        //    this.scoreHeaderTable.removeView(this.rightScrollView);
        super.tearDownReferences();
    }


    private TableRow createRow(int textViewContainerID, List<String> data) {

        TableRow tableRow = new TableRow(getActivity());
        for (String item : data) {
            RelativeLayout container = (RelativeLayout) getActivity().getLayoutInflater().inflate(textViewContainerID, null);
            TextView textView = (TextView) container.findViewById(R.id.score_text_view);
            textView.setText(item);
            tableRow.addView(container);
        }
        return tableRow;

    }

    //Set up ther ranking, logo , and team name
    private void setUpTeamView(List<StandingsRow> listOfRows) {

        for (int ranking = 0; ranking < listOfRows.size(); ranking++) {

            StandingsRow item = listOfRows.get(ranking);

            if (item.getId() != MitooConstants.standingHead) {
                View teamContainer = getActivity().getLayoutInflater().inflate(R.layout.view_standings_row, null);

                //Get all the Static Views
                TextView rankingsText = (TextView) teamContainer.findViewById(R.id.rankingsText);
                ImageView teamIcon = (ImageView) teamContainer.findViewById(R.id.teamIcon);
                TextView teamName = (TextView) teamContainer.findViewById(R.id.teamName);

                //Set up all static Views
                Team team = getDataHelper().getTeam(item.getId());

                if (team == null) {

                    team = new Team();
                    team.setName("Hello Team");
                    team.setLogo_small("http://www.bet.com/news/sports/photos/sports-buzz/2014/04/sports-buzz-4-27-5-3/_jcr_content/leftcol/flipbook/flipbookimage.flipfeature.dimg/052913-sports-teams-logo-golden-state-warriors.jpg");
                }

                rankingsText.setText(Integer.toString(ranking));
                getTeamViewModel().setUpTeamName(team, teamName);
                getTeamViewModel().loadTeamIcon(teamIcon, team);

                this.teamTable.addView(teamContainer);

            }
        }

    }

    public TeamViewModel getTeamViewModel() {

        if (teamViewModel == null) {
            teamViewModel = new TeamViewModel(getViewHelper());
        }
        return teamViewModel;

    }

    private void syncScrollViews() {

        this.leftScrollView.setScrollViewListener(this);
        this.rightScrollView.setScrollViewListener(this);

    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

        if (scrollView == this.leftScrollView) {
            this.rightScrollView.scrollTo(x, y);
        } else if (scrollView == this.rightScrollView) {
            this.leftScrollView.scrollTo(x, y);
        }

    }
}