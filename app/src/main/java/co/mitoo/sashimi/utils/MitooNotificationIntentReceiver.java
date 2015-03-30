package co.mitoo.sashimi.utils;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.urbanairship.push.BaseIntentReceiver;
import com.urbanairship.push.PushMessage;

import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationRecieve;
import co.mitoo.sashimi.utils.events.NotificationEvent;

/**
 * Created by david on 15-03-27.
 */
public class MitooNotificationIntentReceiver extends BaseIntentReceiver {

    private static final String TAG = "IntentReceiver";

    @Override
    protected void onChannelRegistrationSucceeded(Context context, String channelId) {
        Log.i(TAG, "Channel registration updated. Channel Id:" + channelId + ".");
    }

    @Override
    protected void onChannelRegistrationFailed(Context context) {
        Log.i(TAG, "Channel registration failed.");
    }

    @Override
    protected void onPushReceived(Context context, PushMessage message, int notificationId) {
        Log.i(TAG, "Received push notification. Alert: " + message.getAlert() + ". Notification ID: " + notificationId);
    }

    @Override
    protected void onBackgroundPushReceived(Context context, PushMessage message) {
        Log.i(TAG, "Received background push message: " + message);
    }

    @Override
    protected boolean onNotificationOpened(Context context, PushMessage message, int notificationId) {
        Log.i(TAG, "User clicked notification. Alert: " + message.getAlert());
        Bundle bundle = message.getPushBundle();
        NotificationRecieve notificationRecieve = new NotificationRecieve(bundle);
        BusProvider.post(new NotificationEvent(notificationRecieve));
        return false;
    }

    @Override
    protected boolean onNotificationActionOpened(Context context, PushMessage message, int notificationId, String buttonId, boolean isForeground) {
        Log.i(TAG, "User clicked notification button. Button ID: " + buttonId + " Alert: " + message.getAlert());
        // Return false to let UA handle launching the launch activity
        return false;
    }

    @Override
    protected void onNotificationDismissed(Context context, PushMessage message, int notificationId) {
        Log.i(TAG, "Notification dismissed. Alert: " + message.getAlert() + ". Notification ID: " + notificationId);
    }
}