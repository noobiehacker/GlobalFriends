package co.mitoo.sashimi.views.activities;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.notifications.DefaultNotificationFactory;

import co.mitoo.sashimi.R;

/**
 * Created by david on 15-03-26.
 */

public class MitooApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        UAirship.takeOff(this, new UAirship.OnReadyCallback() {
            @Override
            public void onAirshipReady(UAirship airship) {
                // Create a customized default notification factory
                DefaultNotificationFactory defaultNotificationFactory = new DefaultNotificationFactory(getApplicationContext());
                defaultNotificationFactory.setSmallIconId(R.drawable.notification_icon);
                defaultNotificationFactory.setColor(getResources().getColor(R.color.black));
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

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}