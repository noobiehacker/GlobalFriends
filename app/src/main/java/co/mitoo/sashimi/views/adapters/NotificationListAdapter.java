package co.mitoo.sashimi.views.adapters;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.network.Services.NotificationPreferenceService;
import co.mitoo.sashimi.models.appObject.MitooNotification;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationPreferenceRecieved;
import co.mitoo.sashimi.models.jsonPojo.recieve.notification.group_settings;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.NotificationUpdateEvent;
import co.mitoo.sashimi.views.fragments.NotificationFragment;
import android.widget.CompoundButton.OnCheckedChangeListener;
/**
 * Created by david on 15-03-12.
 */

public class NotificationListAdapter extends ArrayAdapter<MitooNotification> {

    private NotificationFragment fragment;
    private MitooEnum.NotificationType notificationType ;
    private NotificationPreferenceRecieved notificationPreferenceRecieved;
    private NotificationPreferenceRecieved previousState;

    public NotificationListAdapter(Context context, int resourceId, List<MitooNotification> objects, NotificationFragment fragment){
        super(context, resourceId, objects);
        setNotificationType(getNotificationTypeFromList(objects));
        setFragment(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(getContext(), R.layout.list_view_item_notification ,null);
        MitooNotification mitooNotification = this.getItem(position);
        setUpText(convertView, mitooNotification);
        setUpToggle(convertView, mitooNotification);
        return convertView;
    }

    public NotificationFragment getFragment() {
        return fragment;
    }

    public void setFragment(NotificationFragment fragment) {
        this.fragment = fragment;
    }

    private void setUpText(View convertView, MitooNotification mitooNotification){

        TextView notificationTextView = (TextView) convertView.findViewById(R.id.list_item_text_view);
        String notificationText =mitooNotification.getNotificationText();
        notificationTextView.setText(notificationText);

    }

    private void setUpToggle(View convertView, MitooNotification mitooNotification) {

        final MitooNotification mitooNotificationPassIn = mitooNotification;
        CompoundButton toggle = (CompoundButton) convertView.findViewById(R.id.notification_switch);
        setUpCheckStatus(toggle, mitooNotification);
        toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                NotificationListAdapter.this.previousState = NotificationListAdapter.this.notificationPreferenceRecieved;
                BusProvider.post(new NotificationUpdateEvent(getUserID(),getCompetitionSeasonID(), isChecked, mitooNotificationPassIn));
            }
        });

    }

    private void setUpCheckStatus(CompoundButton toggle ,MitooNotification mitooNotification){

        if(this.notificationPreferenceRecieved!=null){
            boolean switchStatus = getNotificaitonCheckStatus(this.notificationPreferenceRecieved, mitooNotification);
            toggle.setChecked(switchStatus);
        }
    }

    private boolean getNotificaitonCheckStatus(NotificationPreferenceRecieved prefReceive, MitooNotification mitooNotification) {
        boolean result = false;

        if(getNotificationType()== MitooEnum.NotificationType.EMAIL)
            result = getEmailCheckStatus(prefReceive, mitooNotification);
        else if(getNotificationType()== MitooEnum.NotificationType.PUSH)
            result = getPushCheckStatus(prefReceive, mitooNotification);

        return result;
    }

    private boolean getEmailCheckStatus(NotificationPreferenceRecieved prefReceive, MitooNotification mitooNotification){

        boolean result = false;

        group_settings group_settings = prefReceive.getGroup_settings();
        switch(mitooNotification.getNotificationCategory()){
            case TeamGames:
                result= group_settings.getGroup_team_games().isEmail();
                break;
            case TeamResults:
                result= group_settings.getGroup_team_results().isEmail();
                break;
            case LeagueResults:
                result= group_settings.getGroup_league_results().isEmail();
                break;
        }
        return result;
    }

    private boolean getPushCheckStatus(NotificationPreferenceRecieved prefReceive, MitooNotification mitooNotification){

        boolean result = false;

        group_settings group_settings = prefReceive.getGroup_settings();
        switch(mitooNotification.getNotificationCategory()){
            case TeamGames:
                result= group_settings.getGroup_team_games().isPush();
                break;
            case TeamResults:
                result= group_settings.getGroup_team_results().isPush();
                break;
            case LeagueResults:
                result= group_settings.getGroup_league_results().isPush();
                break;
        }
        return result;
    }

    private NotificationPreferenceService getNotificationModel(){

        return getFragment().getMitooActivity().getModelManager().getNotificationPreferenceModel();
    }


    public void revertToPreviousState(){
        this.notificationPreferenceRecieved = this.previousState;
        this.notifyDataSetChanged();
    }

    public MitooEnum.NotificationType getNotificationTypeFromList(List<MitooNotification> objects){

        MitooEnum.NotificationType result = MitooEnum.NotificationType.PUSH;
        if(objects!=null && !objects.isEmpty()){
            MitooNotification notification = objects.get(0);
            result = notification.getNotificationType();
        }
        return result;

    }

    public MitooEnum.NotificationType getNotificationType() {
        if(notificationType == null)
            notificationType = MitooEnum.NotificationType.PUSH;
        return notificationType;
    }

    public void setNotificationType(MitooEnum.NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public void setNotificationPreferenceRecieved(NotificationPreferenceRecieved notificationPreferenceRecieved) {
        this.notificationPreferenceRecieved = notificationPreferenceRecieved;
    }

    private int getUserID(){
        return this.fragment.getUserID();
    }

    private int getCompetitionSeasonID(){
        return this.fragment.getCompetitionSeasonID();
    }
}
