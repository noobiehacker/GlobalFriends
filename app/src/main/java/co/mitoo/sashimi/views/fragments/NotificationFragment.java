package co.mitoo.sashimi.views.fragments;
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
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.NotificationModelEvent;
import co.mitoo.sashimi.views.adapters.NotificationListAdapter;

/**
 * Created by david on 15-03-12.
 */

public class NotificationFragment extends MitooFragment {

    private int teamColor = MitooConstants.invalidConstant;

    private ListView emailNotificationListView;
    private NotificationListAdapter emailNotificaitonAdapter;
    private List<MitooNotification> emailMitooNotificationList;

    private ListView pushNotificationListView;
    private NotificationListAdapter pushNotificaitonAdapter;
    private List<MitooNotification> pushMitooNotificationList;

    private League selectedLeague;
    private NotificationPreferenceRecieved previousPreferenceState;
    private boolean notificationDataLoaded;

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setPreDataLoading(true);
        requestData();
    }

    public static NotificationFragment  newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public void onResume(){

        super.onResume();
        //QUICK FIX REFACTOR LATER
        //Naming convention of isLoading should be hasLoaded! REFACTOR
        if(!isLoading() && isNotificationDataLoaded())
            updateView();
    }

    @Override
    protected void requestData() {
        setPreDataLoading(true);
        setNotificationDataLoaded(false);
        getNotificationPreferenceModel().requestNotificationPreference();
    }

    @Subscribe
    public void onNotificationModelResponse(NotificationModelEvent event){
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
        if(selectedLeague == null){
            Competition competition = getCompetitionModel().getSelectedCompetition();
            if(competition!=null)
                selectedLeague=competition.getLeague();
        }
        return selectedLeague;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        setProgressLayout((ProgressLayout) view.findViewById(R.id.progressLayout));
        setUpMyEmailListView(view);
        setUpMyPushListView(view);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_notifications));
        setUpEmailNotificationListData();
        setUpPushNotificationListData();
    }

    private void setUpEmailNotificationListData(){

        for(MitooEnum.NotificationCategory item :MitooEnum.NotificationCategory.values()){
            getEmailMitooNotificationList().add(new MitooNotification(item, MitooEnum.NotificationType.EMAIL, getMitooActivity()));

        }

    }

    private void setUpPushNotificationListData(){

        for(MitooEnum.NotificationCategory item :MitooEnum.NotificationCategory.values()){
            getPushMitooNotificationList().add(new MitooNotification(item, MitooEnum.NotificationType.PUSH, getMitooActivity()));

        }
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);

    }

    public int getTeamColor() {

        if (teamColor == MitooConstants.invalidConstant) {
            String teamColorString = getSelectedLeague().getColor_1();
            teamColor = getViewHelper().getColor(teamColorString);
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

    public void setTeamColor(int teamColor) {
        this.teamColor = teamColor;
    }

    public ListView getEmailNotificationListView() {
        return emailNotificationListView;
    }

    public void setEmailNotificationListView(ListView emailNotificationListView) {
        this.emailNotificationListView = emailNotificationListView;
    }

    public ListView getPushNotificationListView() {
        return pushNotificationListView;
    }

    public void setPushNotificationListView(ListView pushNotificationListView) {
        this.pushNotificationListView = pushNotificationListView;
    }

    private void setUpMyPushListView(View view){

        setPushNotificationListView((ListView) view.findViewById(R.id.push_list_view));
        getViewHelper().setUpListView(getPushNotificationListView(),
                getPushNotificaitonAdapter(), getString(R.string.notification_page_push_list_title));

    }

    private void setUpMyEmailListView(View view){

        setEmailNotificationListView((ListView) view.findViewById(R.id.email_list_view));
        getViewHelper().setUpListView(getEmailNotificationListView(),
                getEmailNotificaitonAdapter(), getString(R.string.notification_page_email_list_title));

    }

    public NotificationListAdapter getEmailNotificaitonAdapter() {
        if(emailNotificaitonAdapter == null)
            emailNotificaitonAdapter = new NotificationListAdapter(getActivity(),
                    R.id.email_list_view, getEmailMitooNotificationList() , this);
        return emailNotificaitonAdapter;
    }

    public List<MitooNotification> getEmailMitooNotificationList() {
        if(emailMitooNotificationList == null)
            emailMitooNotificationList = new ArrayList<MitooNotification>();
        return emailMitooNotificationList;
    }

    public NotificationListAdapter getPushNotificaitonAdapter() {
        if(pushNotificaitonAdapter == null)
            pushNotificaitonAdapter = new NotificationListAdapter(getActivity(), R.id.push_list_view ,
                    getPushMitooNotificationList() , this);
        return pushNotificaitonAdapter;
    }

    public List<MitooNotification> getPushMitooNotificationList() {
        if(pushMitooNotificationList == null)
            pushMitooNotificationList = new ArrayList<MitooNotification>();
        return pushMitooNotificationList;
    }

    private void updateView(){

        setRunnable(new Runnable() {
            @Override
            public void run() {
                getHandler().postDelayed(getRunnable(), MitooConstants.durationShort);
                setPreDataLoading(false);
                setPageFirstLoad(true);
                getEmailNotificaitonAdapter().notifyDataSetChanged();
                getPushNotificaitonAdapter().notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void handleNetworkError() {

        if (getProgressLayout() != null) {

            if (pageLoaded()) {
                getEmailNotificaitonAdapter().revertToPreviousState();
                getPushNotificaitonAdapter().revertToPreviousState();
                displayTextWithToast(getString(R.string.error_no_internet));
            } else {
                centerProgressLayout();
                getProgressLayout().removeAllViews();
                getProgressLayout().addView(createNetworkFailureView());
            }
        }

    }

    public NotificationPreferenceRecieved getPreviousPreferenceState() {
        return previousPreferenceState;
    }

    public void setPreviousPreferenceState(NotificationPreferenceRecieved previousPreferenceState) {
        this.previousPreferenceState = (NotificationPreferenceRecieved) previousPreferenceState.clone();
    }

    public boolean isNotificationDataLoaded() {
        return notificationDataLoaded;
    }

    public void setNotificationDataLoaded(boolean notificationDataLoaded) {
        this.notificationDataLoaded = notificationDataLoaded;
    }
}
