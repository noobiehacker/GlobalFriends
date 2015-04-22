package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationRecieve;

/**
 * Created by david on 15-03-29.
 */
public class NotificationEvent {

    private NotificationRecieve notificationRecieve;

    public NotificationEvent(NotificationRecieve notificationRecieve) {
        this.notificationRecieve = notificationRecieve;
    }

    public NotificationRecieve getNotificationRecieve() {
        return notificationRecieve;
    }

    public void setNotificationRecieve(NotificationRecieve notificationRecieve) {
        this.notificationRecieve = notificationRecieve;
    }
}
