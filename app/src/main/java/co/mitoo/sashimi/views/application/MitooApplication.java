package co.mitoo.sashimi.views.application;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.notifications.DefaultNotificationFactory;

import co.mitoo.sashimi.BuildConfig;
import io.keen.client.java.KeenClient;
import io.keen.client.android.AndroidKeenClientBuilder;
import io.keen.client.java.KeenLogging;
import io.keen.client.java.KeenProject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.managers.ModelManager;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.fragments.MitooFragment;


/**
 * Created by david on 15-03-26.
 */

public class MitooApplication extends Application{

    public static int userID = MitooConstants.invalidConstant;
    private Stack<MitooFragment> fragmentStack;
    private ModelManager modelManager;
    private boolean persistedDataLoaded = false;
    private Queue<Object> eventQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        this.modelManager=null;
        this.eventQueue = new LinkedList<Object>();
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

        // If the Keen Client isn't already initialized, initialize it.
        if (!KeenClient.isInitialized()) {

            // Create a new instance of the client.
            KeenClient client = new AndroidKeenClientBuilder(this).build();

            // Get the project ID and write key from string resources, then create a project and set
            // it as the default for the client.
            String projectId = getString(R.string.keen_project_id);
            String writeKey = getString(R.string.keen_write_key);
            KeenProject project = new KeenProject(projectId, writeKey, null);
            client.setDefaultProject(project);

            setUpKeenClientGlobalProperties(client);

            // Initialize the KeenClient singleton with the created client.
            KeenClient.initialize(client);
        }

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

    public Stack<MitooFragment> getFragmentStack() {
        if (fragmentStack == null)
            fragmentStack = new Stack<MitooFragment>();
        return fragmentStack;
    }

    public ModelManager getModelManager() {
        return this.modelManager;
    }

    public void setUpPersistenceData(MitooActivity activity) {

        if(this.persistedDataLoaded==false){
            this.modelManager = new ModelManager(activity);
            if (MitooConstants.getPersistenceStorage()) {
                this.modelManager.readAllPersistedData();
            } else {
                this.modelManager.deleteAllPersistedData();
            }
            this.persistedDataLoaded=true;
        }else{
            if(this.modelManager!=null){
                this.modelManager.setActivity(activity);
            }
        }

    }

    private void setUpKeenClientGlobalProperties(KeenClient client){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("platform", "android");
        map.put("app_version", BuildConfig.VERSION_NAME);
        client.setGlobalProperties(map);
    }


    public Queue<Object> getEventQueue() {
        return eventQueue;
    }
}