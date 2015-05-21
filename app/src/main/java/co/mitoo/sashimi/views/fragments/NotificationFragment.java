package co.mitoo.sashimi.views.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.androidprogresslayout.ProgressLayout;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.appObject.MitooNotification;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationPreferenceRecieved;
import co.mitoo.sashimi.services.EventTrackingService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CompetitionSeasonReqByCompAndUserID;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.NotificationModelResponseEvent;
import co.mitoo.sashimi.utils.events.NotificationRequestEvent;
import co.mitoo.sashimi.views.adapters.NotificationListAdapter;

/**
 * Created by david on 15-03-12.
 */

public class NotificationFragment extends MitooFragment {

    private int teamColor = MitooConstants.invalidConstant;

    private ListView notificationListView;
    private NotificationListAdapter notificaitonAdapter;
    private List<MitooNotification> mitooNotificationList;

    private League selectedLeague;
    private NotificationPreferenceRecieved previousPreferenceState;
    private boolean notificationDataLoaded;
    private int competitionSeasonID;

    @Override
    public void onClick(View v) {
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {

        if (nextAnim != 0) {
            Animator anim = AnimatorInflater.loadAnimator(getActivity().getApplicationContext(), nextAnim);
            final boolean enterToPassIn = enter;
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (enterToPassIn) {
                        NotificationFragment.this.onFragmentAnimationFinish();

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
        } else {
            NotificationFragment.this.onFragmentAnimationFinish();
            return super.onCreateAnimator(transit, enter, nextAnim);
        }
    }

    private void onFragmentAnimationFinish(){
        requestData();
        if (!isLoading() && isNotificationDataLoaded())
            updateView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.competitionSeasonID = savedInstanceState.getInt(getCompetitionSeasonIdKey());
            this.teamColor = savedInstanceState.getInt(getTeamColorKey());
        } else {
            this.competitionSeasonID = getArguments().getInt(getCompetitionSeasonIdKey());
            this.teamColor = getArguments().getInt(getTeamColorKey());
        }
        setPreDataLoading(true);
    }

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(getCompetitionSeasonIdKey(), this.competitionSeasonID);
        bundle.putInt(getTeamColorKey(), this.teamColor);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume() {

        super.onResume();
        EventTrackingService.userViewedNotificationPreferencesScreen(this.getUserID(), this.competitionSeasonID);
    }

    @Override
    protected void requestData() {
        setPreDataLoading(true);
        getNotificationPreferenceModel();
        BusProvider.post(new NotificationRequestEvent(getUserID(),this.competitionSeasonID));

    }

    @Subscribe
    public void onNotificationModelResponse(NotificationModelResponseEvent event) {
        getNotificaitonAdapter().setNotificationPreferenceRecieved(event.getNotificationPrefReceive());
        setNotificationDataLoaded(true);
        updateView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_notification,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    public League getSelectedLeague() {
        if (selectedLeague == null) {
            Competition competition = getCompetitionModel().getSelectedCompetition();
            if (competition != null)
                selectedLeague = competition.getLeague();
        }
        return selectedLeague;
    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        setUpMyEmailListView(view);

    }

    @Override
    protected void initializeFields() {

        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_notifications));
        setUpNotificationListData();
    }

    private void setUpNotificationListData() {

        for (MitooEnum.NotificationCategory item : MitooEnum.NotificationCategory.values()) {
            getMitooNotificationList().add(new MitooNotification(item, MitooEnum.NotificationType.EMAIL, getMitooActivity()));

        }

        for (MitooEnum.NotificationCategory item : MitooEnum.NotificationCategory.values()) {
            getMitooNotificationList().add(new MitooNotification(item, MitooEnum.NotificationType.PUSH, getMitooActivity()));

        }

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);

    }

    public int getTeamColor() {

        if (teamColor == MitooConstants.invalidConstant) {
            teamColor = getMitooActivity().getResources().getColor(R.color.gray_dark_five);
        }
        return teamColor;

    }

    @Override
    protected Toolbar setUpToolBar(View view) {

        super.setUpToolBar(view);
        if (getToolbar() != null) {
            getToolbar().setBackgroundColor(getTeamColor());
        }

        return getToolbar();
    }

    private void setUpMyEmailListView(View view) {

        this.notificationListView = (ListView) view.findViewById(R.id.email_list_view);
        this.notificationListView.setAdapter(getNotificaitonAdapter());

    }

    public NotificationListAdapter getNotificaitonAdapter() {
        if (notificaitonAdapter == null)
            notificaitonAdapter = new NotificationListAdapter(getActivity(),
                    R.id.email_list_view, getMitooNotificationList(), this);
        return notificaitonAdapter;
    }

    public List<MitooNotification> getMitooNotificationList() {
        if (mitooNotificationList == null)
            mitooNotificationList = new ArrayList<MitooNotification>();
        return mitooNotificationList;
    }

    private void updateView() {

        setPreDataLoading(false);
        setPageFirstLoad(true);
        getNotificaitonAdapter().notifyDataSetChanged();

    }

    @Override
    protected void handleNetworkError() {

        if (getProgressLayout() != null) {

            if (pageLoaded()) {
                getNotificaitonAdapter().revertToPreviousState();
                displayTextWithToast(getString(R.string.error_no_internet));
            } else {
                centerProgressLayout();
                getProgressLayout().removeAllViews();
                getProgressLayout().addView(createNetworkFailureView());
            }
        }

    }

    public boolean isNotificationDataLoaded() {
        return notificationDataLoaded;
    }

    public void setNotificationDataLoaded(boolean notificationDataLoaded) {
        this.notificationDataLoaded = notificationDataLoaded;
    }

    public int getCompetitionSeasonID(){
        return this.competitionSeasonID;
    }


    public int getUserID(){
        return super.getUserID();
    }
}
