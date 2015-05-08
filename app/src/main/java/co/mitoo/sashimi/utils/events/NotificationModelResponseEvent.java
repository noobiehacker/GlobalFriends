package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationPreferenceRecieved;

/**
 * Created by david on 15-04-08.
 */
public class NotificationModelResponseEvent {

    private NotificationPreferenceRecieved notificationPrefReceive;

    public NotificationModelResponseEvent(NotificationPreferenceRecieved notificationPrefReceive) {
        this.notificationPrefReceive = notificationPrefReceive;
    }

    public NotificationPreferenceRecieved getNotificationPrefReceive() {
        return notificationPrefReceive;
    }
}
