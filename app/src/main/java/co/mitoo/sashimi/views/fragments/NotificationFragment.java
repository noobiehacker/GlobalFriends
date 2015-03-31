package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.appObject.Notification;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.adapters.NotificationListAdapter;

/**
 * Created by david on 15-03-12.
 */
public class NotificationFragment extends MitooFragment {

    private int teamColor = MitooConstants.invalidConstant;

    private ListView notificationListView;
    private NotificationListAdapter notificaitonAdapter;
    private List<Notification> notificationList;
    private League selectedLeague;
    @Override
    public void onClick(View v) {
    }

    public static NotificationFragment  newInstance() {
        NotificationFragment fragment = new NotificationFragment ();
        return fragment;
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
        if(selectedLeague == null)
            selectedLeague =getCompetitionModel().getSelectedCompetition().getLeague();
        return selectedLeague;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        setUpMyNotificationListView(view);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_notification));
        setUpNotificationListData();
    }

    private void setUpNotificationListData(){

        getNotificationList().add(new Notification(MitooEnum.NotificationType.NextGame));
        getNotificationList().add(new Notification(MitooEnum.NotificationType.RivalResults));
        getNotificationList().add(new Notification(MitooEnum.NotificationType.TeamResults));
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

    public ListView getNotificationListView() {
        return notificationListView;
    }

    public void setNotificationListView(ListView notificationListView) {
        this.notificationListView = notificationListView;
    }


    private void setUpMyNotificationListView(View view){

        setNotificationListView((ListView) view.findViewById(R.id.notification_list_view));
        getViewHelper().setUpListView(getNotificationListView(),
                getNotificaitonAdapter(), getString(R.string.notification_page_list_title));

    }

    public NotificationListAdapter getNotificaitonAdapter() {
        if(notificaitonAdapter== null)
            notificaitonAdapter  = new NotificationListAdapter(getActivity(),
                    R.id.notification_list_view, getNotificationList() , this);
        return notificaitonAdapter;
    }

    public List<Notification> getNotificationList() {
        if(notificationList == null)
            notificationList = new ArrayList<Notification>();
        return notificationList;
    }
}
