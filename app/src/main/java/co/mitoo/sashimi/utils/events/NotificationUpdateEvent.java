package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.appObject.MitooNotification;

/**
 * Created by david on 15-05-07.
 */
public class NotificationUpdateEvent {

    private int userID;
    private int competitionSeasonID;
    private boolean checked;
    private MitooNotification notification;

    public NotificationUpdateEvent(int userID, int competitionSeasonID, boolean checked, MitooNotification notification) {
        this.userID = userID;
        this.competitionSeasonID = competitionSeasonID;
        this.checked = checked;
        this.notification = notification;
    }

    public int getUserID() {
        return userID;
    }

    public int getCompetitionSeasonID() {
        return competitionSeasonID;
    }

    public boolean isChecked() {
        return checked;
    }

    public MitooNotification getNotification() {
        return notification;
    }
}
