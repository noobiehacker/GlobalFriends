package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationRecieve;

/**
 * Created by david on 15-04-09.
 */
public class NotificationUpdateEvent {

    private NotificationRecieve notificationRecieve;

    public NotificationRecieve getNotificationRecieve() {
        return notificationRecieve;
    }

    public void setNotificationRecieve(NotificationRecieve notificationRecieve) {
        this.notificationRecieve = notificationRecieve;
    }

    public NotificationUpdateEvent(NotificationRecieve notificationRecieve) {
        this.notificationRecieve = notificationRecieve;
    }
}
