package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationReceive;

/**
 * Created by david on 15-03-29.
 */
public class NotificationEvent {

    private NotificationReceive notificationReceive;

    public NotificationEvent(NotificationReceive notificationReceive) {
        this.notificationReceive = notificationReceive;
    }

    public NotificationReceive getNotificationReceive() {
        return notificationReceive;
    }

    public void setNotificationReceive(NotificationReceive notificationReceive) {
        this.notificationReceive = notificationReceive;
    }
}
