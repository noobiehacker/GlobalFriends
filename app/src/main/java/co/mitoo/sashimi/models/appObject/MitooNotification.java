package co.mitoo.sashimi.models.appObject;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-03-12.
 */
public class MitooNotification {

    private MitooEnum.NotificationCategory notificationCategory;
    private MitooEnum.NotificationType notificationType;
    private MitooActivity activity;

    public MitooEnum.NotificationCategory getNotificationCategory() {
        return notificationCategory;
    }

    public void setNotificationCategory(MitooEnum.NotificationCategory notificationCategory) {
        this.notificationCategory = notificationCategory;
    }

    public MitooNotification(MitooEnum.NotificationCategory notificationCategory, MitooEnum.NotificationType notificationType, MitooActivity activity) {
        this.notificationCategory = notificationCategory;
        this.notificationType = notificationType;
        this.activity = activity;
    }

    public MitooActivity getActivity() {
        return activity;
    }

    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }

    public String getNotificationText() {

        String result = "";

        switch(getNotificationCategory()){
            case TeamGames:
                result= getActivity().getString(R.string.notification_page_list_item_team_games);
                break;
            case TeamResults:
                result= getActivity().getString(R.string.notification_page_list_item_team_results);
                break;
            case LeagueResults:
                result= getActivity().getString(R.string.notification_page_list_item_league_results);
                break;
            case RainOut:
                result = "";
                break;
        }

        return result;
    }

    public MitooEnum.NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(MitooEnum.NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isHeaderObject(){
        if(this.notificationCategory == MitooEnum.NotificationCategory.TeamGames)
            return true;
        return false;
    }
}
