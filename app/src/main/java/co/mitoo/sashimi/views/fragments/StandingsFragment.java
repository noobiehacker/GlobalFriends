package co.mitoo.sashimi.views.fragments;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.github.androidprogresslayout.ProgressLayout;
import com.squareup.otto.Subscribe;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.appObject.StandingsRow;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.TeamViewHelper;
import co.mitoo.sashimi.utils.events.LoadScoreTableEvent;
import co.mitoo.sashimi.utils.events.LoadStandingsEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.StandingsLoadedEvent;
import co.mitoo.sashimi.views.Listener.ScrollViewListener;
import co.mitoo.sashimi.views.widgets.ObservableScrollView;

/**
 * Created by david on 15-04-23.
 */
public class StandingsFragment extends MitooFragment implements ScrollViewListener {

    private int competitionSeasonID;
    private TableLayout scoreHeaderTable;
    private TableLayout teamTable;
    private TableLayout dataTable;
    private TeamViewHelper teamViewHelper;
    private ObservableScrollView leftScrollView;
    private ObservableScrollView rightScrollView;
    private List<StandingsRow> standingsRows;
    private ProgressLayout rightViewProgressLayout;
    private boolean dataRequested = false;
    private boolean dataReceived = false;
    private boolean tabClicked = false;
    private boolean tableLoaded;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (getDataHelper().isClickable(v.getId())) {
            switch (v.getId()) {

            }
        }
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
        return view;

    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        this.tabClicked = false;
        this.scoreHeaderTable = (TableLayout) view.findViewById(R.id.scoreHeaderView);
        this.leftScrollView = (ObservableScrollView) view.findViewById(R.id.leftScrollView);
        this.teamTable = (TableLayout) view.findViewById(R.id.teamTableLayout);
        this.rightViewProgressLayout = (ProgressLayout) view.findViewById(R.id.rightTableProgressLayout);
    }

    @Override
    protected void initializeFields() {

        super.initializeFields();
        String bundleValue = getArguments().getString(getString(R.string.bundle_key_competition_id));
        this.competitionSeasonID = Integer.parseInt(bundleValue);
        this.tableLoaded =false;

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);

    }

    @Override
    protected void requestData() {
        setPreDataLoading(true);
        setRunnable(new Runnable() {
            @Override
            public void run() {
                BusProvider.post(new LoadStandingsEvent(StandingsFragment.this.competitionSeasonID));
            }
        });
        getHandler().postDelayed(getRunnable(), MitooConstants.durationMedium);
        this.dataRequested=true;
    }

    @Override
    public void onResume() {

        super.onResume();
        if (this.dataRequested == false)
            requestData();
        loadStandingsView();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }


    private void loadStandingsView() {

        if(this.dataReceived==true){
            setUpTeamView(this.standingsRows);
            attemptToLoadTable();
        }

    }

    @Subscribe
    public void onStandingsLoaded(StandingsLoadedEvent event) {

        this.dataReceived = true;
        this.standingsRows = event.getStandingRows();
        loadStandingsView();

    }

    @Subscribe
    public void onLoadScoreTable(LoadScoreTableEvent event) {
        this.tabClicked = true;
        attemptToLoadTable();

    }

    private void attemptToLoadTable(){
        if (this.tabClicked == true && this.dataReceived == true) {

            setRunnable(new Runnable() {
                @Override
                public void run() {
                    setUpScoreTable(StandingsFragment.this.standingsRows);
                }
            });
            getHandler().postDelayed(getRunnable(), MitooConstants.durationMedium);

        }
    }

    private void setUpScoreTable(List<StandingsRow> listOfRows) {

        this.rightScrollView = (ObservableScrollView) getActivity().getLayoutInflater().inflate(R.layout.view_standings_data_table, null);

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
        setPreDataLoading(false);
        syncScrollViews();

    }

    @Override
    public void tearDownReferences(){

    //    this.scoreHeaderTable.removeView(this.rightScrollView);
        super.tearDownReferences();
    }

    private void dynamicallyResizeColumns() {


        TableRow headTableRow = (TableRow) this.scoreHeaderTable.getChildAt(0);
        TableRow dataTableRow = (TableRow) this.dataTable.getChildAt(0);

        if ((headTableRow.getChildCount() == dataTableRow.getChildCount()) && (dataTableRow.getChildCount() > 0)) {

            // headLayout
            for (int i = 0; i < headTableRow.getChildCount(); i++) {

                RelativeLayout headRelativeLayout = (RelativeLayout) headTableRow.getChildAt(i);
                RelativeLayout rowRelativeLayout = (RelativeLayout) dataTableRow.getChildAt(i);
                TextView headTextView = (TextView) headRelativeLayout.getChildAt(0);
                TextView rowTextView = (TextView) rowRelativeLayout.getChildAt(0);
                headRelativeLayout.setMinimumWidth(rowRelativeLayout.getMeasuredWidth());
                headTextView.setWidth(rowTextView.getMeasuredWidth());

            }

        }

        this.tableLoaded= true;
        this.rightViewProgressLayout.showContent();

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

                if (team != null) {

                    rankingsText.setText(Integer.toString(ranking));
                    getTeamViewHelper().setUpTeamName(team, teamName);
                    getTeamViewHelper().loadTeamIcon(teamIcon, team);
                }
                this.teamTable.addView(teamContainer);

            }
        }

    }

    public TeamViewHelper getTeamViewHelper() {

        if (teamViewHelper == null) {
            teamViewHelper = new TeamViewHelper(getViewHelper());
        }
        return teamViewHelper;

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