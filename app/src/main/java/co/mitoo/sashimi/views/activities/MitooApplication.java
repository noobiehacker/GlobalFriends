package co.mitoo.sashimi.views.activities;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;

import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.notifications.DefaultNotificationFactory;

import co.mitoo.sashimi.BuildConfig;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 15-03-26.
 */

public class MitooApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        UAirship.takeOff(this, createAirshipOptions(), new UAirship.OnReadyCallback() {
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

    private AirshipConfigOptions createAirshipOptions() {

        AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
        options.developmentAppKey = getString(R.string.UA_app_dev_key);
        options.productionAppKey = getString(R.string.UA_app_prod_key);
        options.developmentAppSecret = getString(R.string.UA_app_dev_secret);
        options.productionAppSecret = getString(R.string.UA_app_prod_secret);
        options.gcmSender = getString(R.string.API_key_gcm_sender);
        options.inProduction = (MitooConstants.getAppEnvironment() == MitooEnum.AppEnvironment.PRODUCTION);
        return options;

    }
}