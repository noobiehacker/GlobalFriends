package co.mitoo.sashimi.views.activities;
import android.app.Application;
import android.support.v4.app.NotificationCompat;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.notifications.DefaultNotificationFactory;
import com.urbanairship.richpush.RichPushManager;

import co.mitoo.sashimi.R;

/**
 * Created by david on 15-03-26.
 */

public class MitooApplication extends Application implements RichPushManager.Listener {

    @Override
    public void onUpdateMessages(boolean success) {
    }

    @Override
    public void onUpdateUser(boolean success) {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AirshipConfigOptions options = new AirshipConfigOptions();
        options.inProduction = false;
        options.developmentAppKey = getString(R.string.API_key_urban_air_ship_app_key);
        options.developmentAppSecret = getString(R.string.API_key_urban_air_ship_app_secret);

        UAirship.takeOff(this, new UAirship.OnReadyCallback() {
            @Override
            public void onAirshipReady(UAirship airship) {
                // Create a customized default notification factory
                DefaultNotificationFactory defaultNotificationFactory = new DefaultNotificationFactory(getApplicationContext());
                defaultNotificationFactory.setSmallIconId(R.drawable.ic_launcher);
                defaultNotificationFactory.setColor(NotificationCompat.COLOR_DEFAULT);

                // Set it
                airship.getPushManager().setNotificationFactory(defaultNotificationFactory);

                // Enable Push
                airship.getPushManager().setPushEnabled(true);
                airship.getPushManager().setUserNotificationsEnabled(true);
                String channelId = UAirship.shared().getPushManager().getChannelId();
                Logger.info("My Application Channel ID: " + channelId);
            }
        });
    }
}