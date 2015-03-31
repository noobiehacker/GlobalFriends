package co.mitoo.sashimi.models.appObject;

import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 15-03-12.
 */
public class Notification {

    private MitooEnum.NotificationType notificationType;

    public MitooEnum.NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(MitooEnum.NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Notification(MitooEnum.NotificationType notificationType) {
        this.notificationType = notificationType;
    }

}
