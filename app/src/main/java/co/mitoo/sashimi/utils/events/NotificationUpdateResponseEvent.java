package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationReceive;

/**
 * Created by david on 15-04-09.
 */
public class NotificationUpdateResponseEvent {

    private NotificationReceive notificationReceive;

    public NotificationReceive getNotificationReceive() {
        return notificationReceive;
    }

    public void setNotificationReceive(NotificationReceive notificationReceive) {
        this.notificationReceive = notificationReceive;
    }

    public NotificationUpdateResponseEvent(NotificationReceive notificationReceive) {
        this.notificationReceive = notificationReceive;
    }
}
