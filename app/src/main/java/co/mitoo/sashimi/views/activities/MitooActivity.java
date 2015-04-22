package co.mitoo.sashimi.views.activities;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.ActivityNotFoundException;
import com.newrelic.agent.android.NewRelic;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.managers.MitooLocationManager;
import co.mitoo.sashimi.models.jsonPojo.Invitation_token;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationRecieve;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.network.DataPersistanceService;
import co.mitoo.sashimi.network.ServiceBuilder;
import co.mitoo.sashimi.utils.AppStringHelper;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.FragmentFactory;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.managers.ModelManager;
import co.mitoo.sashimi.utils.MitooNotificationIntentReceiver;
import co.mitoo.sashimi.utils.events.BranchIOResponseEvent;
import co.mitoo.sashimi.utils.events.ConfirmInfoModelResponseEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LogOutEvent;
import co.mitoo.sashimi.utils.events.LogOutNetworkCompleteEevent;
import co.mitoo.sashimi.utils.events.NotificationUpdateEvent;
import co.mitoo.sashimi.views.fragments.ConfirmAccountFragment;
import co.mitoo.sashimi.views.fragments.ConfirmDoneFragment;
import co.mitoo.sashimi.views.fragments.ConfirmSetPasswordFragment;
import co.mitoo.sashimi.utils.events.NotificationEvent;
import co.mitoo.sashimi.views.fragments.FixtureFragment;
import co.mitoo.sashimi.views.fragments.MitooFragment;
import co.mitoo.sashimi.views.fragments.SplashScreenFragment;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MitooActivity extends ActionBarActivity {

    public static boolean activityStarted =false;
    private MitooLocationManager locationManager;
    private Handler handler;
    private Runnable runnable;
    private Stack<MitooFragment> fragmentStack;
    private ModelManager modelManager;
    private DataHelper dataHelper;
    private Picasso picasso;
    protected DataPersistanceService persistanceService;
    private int firstFragmentToStart = R.id.fragment_splash;
    private Branch branch;
    private AppStringHelper appStringHelper;
    private boolean onSplashScreen= true;
    private Queue<Object> eventQueue;
    private Queue<NotificationRecieve> notificationQueue;
    private Branch.BranchReferralInitListener branchReferralInitListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        MitooActivity.activityStarted=true;
        setContentView(R.layout.activity_mitoo);
        initializeFields();
        startApp();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void consumeEventsInQueue(){

        if(getModelManager().getSessionModel().userIsLoggedIn()){
            Queue<Object> eventsQueue = getEventQueue();
            if(!eventsQueue.isEmpty())
                popAllFragments();
            while(!eventsQueue.isEmpty()){
                BusProvider.post(eventsQueue.poll());
            }
        }

    }

    @Override
    public void onPause() {
        tearDownReferences();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        tearDownReferences();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        branch.closeSession();
        tearDownReferences();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBranch().initSession(getBranchReferralInitListener(), getIntent().getData(), this);
        consumeEventsInQueue();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initializeFields() {

        setUpNewRelic();
        setUpInitialCalligraphy();
        setLocationManager(new MitooLocationManager(this));
        setUpBranch();
        initializeNotification();
        BusProvider.register(this);

    }

    private void initializeNotification(){

        Bundle extra = getIntent().getBundleExtra(MitooNotificationIntentReceiver.bundleKey);
        if(extra!=null){
            NotificationRecieve notificationRecieve = new NotificationRecieve(extra);
            getNotificationQueue().add(notificationRecieve);
            getIntent().removeExtra(MitooNotificationIntentReceiver.bundleKey);
        }

    }

    private void setUpInitialCalligraphy() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(getString(R.string.DIN_Regular))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    @Subscribe
    public void onFragmentQueueChange(LinkedList<FragmentChangeEvent> queueOfEvents) {

        popAllFragments();
        while(!queueOfEvents.isEmpty())
            BusProvider.post(queueOfEvents.pop());
    }

    @Subscribe
    public void onFragmentChange(final FragmentChangeEvent event) {

        if (fragmentIsRoot(event.getFragmentId()))
            popAllFragments();
        hideSoftKeyboard();
        switch (event.getTransition()) {
            case PUSH:
                pushFragment(event);
                break;
            case CHANGE:
            case NONE:
                swapFragment(event);
                break;
            case POP:
                popFragment();
                break;
        }
    }

    @Subscribe
    public void startIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public boolean LocationServicesIsOn() {
        return getLocationManager().LocationServicesIsOn();
    }


    private void pushFragment(FragmentChangeEvent event) {

        MitooFragment fragment = FragmentFactory.getInstance().buildFragment(event);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        setFragmentAnimation(ft, event.getAnimation());
        ft.addToBackStack(String.valueOf(event.getFragmentId()));
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        getFragmentStack().push(fragment);

    }

    private void swapFragment(FragmentChangeEvent event) {

        MitooFragment fragment = FragmentFactory.getInstance().buildFragment(event);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        setFragmentAnimation(ft, event.getAnimation());
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        getFragmentStack().push(fragment);

    }

    private void setFragmentAnimation(FragmentTransaction transction, MitooEnum.FragmentAnimation animation) {

        if (animation == MitooEnum.FragmentAnimation.HORIZONTAL)
            setLeftToRightAnimation(transction);
        else if (animation == MitooEnum.FragmentAnimation.VERTICAL)
            setBottomToTopAnimation(transction);
        else if (animation == MitooEnum.FragmentAnimation.DOWNLEFT)
            setBottomToTopAnimation(transction);
    }

    private void setBottomToTopAnimation(FragmentTransaction transaction) {

        transaction.setCustomAnimations(R.animator.enter_top, R.animator.no_animation,
                0, R.animator.enter_bottom);

    }

    private void setLeftToRightAnimation(FragmentTransaction transaction) {

        transaction.setCustomAnimations(R.animator.enter_right, R.animator.exit_right,
                R.animator.exit_left, R.animator.enter_left);
    }



    public void popFragment() {

        if (getFragmentStack().size() > 0) {
            getFragmentStack().pop();
            getFragmentManager().popBackStack();
            setPreviousFragmentBackClicked();
        }

    }

    public void popFragment(int delayed) {

        Runnable popFragmentRunnable = new Runnable() {
            @Override
            public void run() {
                popFragment();
            }
        };
        setRunnable(popFragmentRunnable);
        getHandler().postDelayed(getRunnable(), delayed);
    }

    public void popAllFragments() {

        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        while (getFragmentStack().size() > 0)
            getFragmentStack().pop();

    }

    public boolean NetWorkConnectionIsOn() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    private void setUpNewRelic() {

        NewRelic.withApplicationToken(getDataHelper().getNewRelicKey())
                .start(this.getApplication());

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.moveTaskToBack(true);
        } else {
            if (getFragmentStack().peek().backPressedAllowed()) {
                hideSoftKeyboard();
                MitooFragment fragmentToDesplay = getSecondTopFragment();
                if (fragmentToDesplay != null && fragmentToDesplay.popActionRequiresDelay())
                    popFragment(250);
                else
                    popFragment();
            }
        }
    }

    private MitooFragment getSecondTopFragment() {

        if (getFragmentStack().size() > 1) {
            return getFragmentStack().get(getFragmentStack().size() - 2);
        }
        return null;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public ModelManager getModelManager() {
        if(modelManager== null)
            this.modelManager = new ModelManager(this);
        return modelManager;
    }

    private void setUpPersistenceData() {

        if (MitooConstants.getPersistenceStorage()) {
            getModelManager().readAllPersistedData();
        } else {
            getModelManager().deleteAllPersistedData();
        }

    }

    public void updateAuthToken(SessionRecieve session) {

        if (session.auth_token != null)
            ServiceBuilder.getSingleTonInstance().setXAuthToken(session.auth_token);
    }

    public void resetAuthToken() {

        ServiceBuilder.getSingleTonInstance().resetXAuthToken();
    }

    public void startApp() {

        setFragmentStack(new Stack<MitooFragment>());
        FragmentChangeEvent event =
                new FragmentChangeEvent(this, MitooEnum.FragmentTransition.NONE,
                        getFirstFragmentToStart(), MitooEnum.FragmentAnimation.NONE);
        BusProvider.post(event);
        setUpPersistenceData();

    }

    @Subscribe
    public void logOut(LogOutEvent event) {

        logOutNetWorkCalls();

    }

    private void logOutNetWorkCalls(){
        getModelManager().getMobileTokenModel().requestDeviceTokenDisassociation();
    }

    @Subscribe
    public void logOutCleanUpAppReferences(LogOutNetworkCompleteEevent event){

        getModelManager().deleteAllPersistedData();
        resetAuthToken();
        popAllFragments();

        FragmentChangeEvent fragmentChangeEvent =
                new FragmentChangeEvent(this, MitooEnum.FragmentTransition.NONE,
                        R.id.fragment_landing, MitooEnum.FragmentAnimation.HORIZONTAL);

        BusProvider.post(fragmentChangeEvent);

    }

    public void contactMitooAction() {

        try {
            String emailText = getString(R.string.feedback_page_contact_mitoo_text_prefix);
            emailText =emailText + getModelManager().getUserInfoModel().getUserInfoRecieve().email;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.mitoo_support_email_address)});
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mitoo_support_email_subject));
            //intent.putExtra(Intent.EXTRA_TEXT, emailText);
            startActivity(Intent.createChooser(intent, "Send identifier..."));
        }
        catch (Exception e){

        }
    }

    public void reviewMitooAction() {

        String appPackageName = getPackageName();
        try {
            Uri marketUri = Uri.parse(getString(R.string.mitoo_play_store_market_prefix)+ appPackageName);
            startActivity(new Intent(Intent.ACTION_VIEW, marketUri ));
        } catch (ActivityNotFoundException e) {
            Uri marketUri = Uri.parse(getString(R.string.mitoo_play_store_url_prefix)+ appPackageName);
            startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
        }

    }

    public void hideSoftKeyboard(View view) {
        Activity activity = this;
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    
    public void hideSoftKeyboard(int delayed){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MitooActivity.this.hideSoftKeyboard();
            }
        };
        setRunnable(runnable);
        getHandler().postDelayed(getRunnable(), delayed);
        
    }
    public void hideSoftKeyboard() {
        
        if (this.getCurrentFocus() != null) {
            View view = this.getCurrentFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

    public Picasso getPicasso() {
        if (picasso == null) {
            OkHttpClient client = new OkHttpClient();
            client.setProtocols(Arrays.asList(Protocol.HTTP_1_1));
            Picasso picasso = new Picasso.Builder(this)
                    .downloader(new OkHttpDownloader(client))
                    .build();
            setPicasso(picasso);
        }
        return picasso;
    }

    public void setPicasso(Picasso picasso) {
        this.picasso = picasso;
    }

    public DataHelper getDataHelper() {
        if(dataHelper==null)
            setDataHelper(new DataHelper(this));
        return dataHelper;
    }

    public void setDataHelper(DataHelper dataHelper) {
        this.dataHelper = dataHelper;
    }
    
    public void showKeyboard(){

        if (MitooActivity.this.getCurrentFocus() != null) {
            View view = MitooActivity.this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
        
    }

    public void showKeyboard(int delayed){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                MitooActivity.this.showKeyboard();

            }
        };
        setRunnable(runnable);
        getHandler().postDelayed(getRunnable(),delayed);

    }

    public Handler getHandler() {
        if(handler == null)
            handler = new Handler();
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    private void handleCallBacks() {
        
        if (getHandler() != null ) {
            getHandler().removeCallbacksAndMessages(getRunnable());
        }

    }

    public void tearDownReferences() {

        handleCallBacks();

    }

    public DataPersistanceService getPersistanceService() {
        if(persistanceService==null)
            persistanceService = new DataPersistanceService(this);
        return persistanceService;
    }

    private boolean fragmentIsRoot(int id){
        
        return id==R.id.fragment_home || id == R.id.fragment_landing;
    }

    public Stack<MitooFragment> getFragmentStack() {
        if(fragmentStack== null)
            fragmentStack= new Stack<MitooFragment>();
        return fragmentStack;
    }

    public void setFragmentStack(Stack<MitooFragment> fragmentStack) {
        this.fragmentStack = fragmentStack;
    }

    public MitooLocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationManager(MitooLocationManager locationManager) {
        if(locationManager== null)
            locationManager = new MitooLocationManager(this);
        this.locationManager = locationManager;
    }

    public int getFirstFragmentToStart() {
        return firstFragmentToStart;
    }

    public void setFirstFragmentToStart(int firstFragmentToStart) {
        this.firstFragmentToStart = firstFragmentToStart;
    }

    public boolean isOnSplashScreen() {
        return onSplashScreen;
    }

    public void setOnSplashScreen(boolean onSplashScreen) {
        this.onSplashScreen = onSplashScreen;
    }

    private Branch getBranch() {
        if(branch ==null)
            branch = Branch.getInstance(this.getApplicationContext(), getAppStringHelper().getBranchAPIKey());
        return branch;
    }

    public AppStringHelper getAppStringHelper() {
        if(appStringHelper==null)
            appStringHelper = new AppStringHelper(this);
        return appStringHelper;
    }

    private void setUpBranch() {

        Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {

                if (error == null && !isDuringConfirmFlow()){

                    Invitation_token token = getDataHelper().getInvitationToken(referringParams);
                    /*
                    *
                    Hard Coding Data for Testing
                    if (token.invitation_token!= null && token.invitation_token.equalsIgnoreCase("hxR1FZ4cPsUTyQz985SL")) {
                        token = new Invitation_token();
                        token.setToken("Z_ryy7BchtV-s_MGEPPG");
                    }
                    *
                    */
                   /* if (token.invitation_token!= null ) {
                        token = new Invitation_token();
                        token.setToken("Z_ryy7BchtV-s_MGEPPG");
                    }*/
                    getModelManager().getSessionModel().setInvitation_token(token);
                    if (isOnSplashScreen()) {

                        BusProvider.post(new BranchIOResponseEvent(getModelManager().getSessionModel().getInvitation_token()));
                        setOnSplashScreen(false);

                    } else {
                        if (token.invitation_token != null && getModelManager().getSessionModel().userIsLoggedIn())
                            BusProvider.post(new LogOutEvent());
                        MitooActivity.this.branchIODataReceived();
                    }
                }
            }
        };
        setBranchReferralInitListener(branchReferralInitListener);

    }

    private void branchIODataReceived(){

        if(!userIsOnInviteFlow()) {
            Invitation_token token = getModelManager().getSessionModel().getInvitation_token();
            if (token != null && token.getToken() != null) {
                getModelManager().getConfirmInfoModel().requestConfirmationInformation(token.getToken());
            }
        }
    }

    private boolean userIsOnInviteFlow(){

        boolean result = false ;
        MitooFragment fragment = (MitooFragment) getFragmentStack().peek();
        if(fragment !=null ){
            if(fragment instanceof ConfirmAccountFragment ||
               fragment instanceof ConfirmSetPasswordFragment ||
               fragment instanceof ConfirmDoneFragment)
                result = true;
        }
        return result;

    }

    @Subscribe
    public void onConfirmInfoModelResponse(ConfirmInfoModelResponseEvent modelEvent){

        FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_confirm_account)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .build();
        BusProvider.post(event);

    }

    @Subscribe
    public void onNotificationRecieve(NotificationEvent event){

        if(topFragmentType() == FixtureFragment.class)
            BusProvider.post(new NotificationUpdateEvent(event.getNotificationRecieve()));
        else{
            FragmentChangeEvent firstEvent = FragmentChangeEventBuilder.getSingletonInstance()
                    .setFragmentID(R.id.fragment_home)
                    .build();

            FragmentChangeEvent seconndEvent = FragmentChangeEventBuilder.getSingletonInstance()
                    .setFragmentID(R.id.fragment_competition)
                    .build();

            FragmentChangeEvent thirdEvent = FragmentChangeEventBuilder.getSingletonInstance()
                    .setFragmentID(R.id.fragment_fixture)
                    .setBundle(createNotifcationBundle(event.getNotificationRecieve()))
                    .build();

            getEventQueue().offer(firstEvent);
            getEventQueue().offer(seconndEvent);
            getEventQueue().offer(thirdEvent);

        }

        if(topFragmentType() == SplashScreenFragment.class)
            consumeEventsInQueue();

    }

    private Bundle createNotifcationBundle(NotificationRecieve notificationRecieve){

        Bundle bundle = new Bundle();
        String bundleValue = getDataHelper().serializeObject(notificationRecieve);
        String bundleKey = getString(R.string.bundle_key_notification);
        bundle.putString(bundleKey,bundleValue);
        return bundle;

    }

    public Queue<Object> getEventQueue() {
        if(eventQueue==null)
            eventQueue = new LinkedList<Object>();
        return eventQueue;
    }

    private void setPreviousFragmentBackClicked(){
        if(getFragmentStack().size()>0 && getFragmentStack().peek()!=null)
            getFragmentStack().peek().setBackClicked(true);
    }

    public Class<?> topFragmentType(){
        if(getFragmentStack().size()!= 0){
            MitooFragment fragment = getFragmentStack().peek();
            return fragment.getClass();
        }
        return null;
    }

    public Queue<NotificationRecieve> getNotificationQueue() {
        if(notificationQueue==null)
            notificationQueue= new PriorityQueue<NotificationRecieve>();
        return notificationQueue;

    }

    public void consumeNotification(){

        while(getNotificationQueue().size()>0){
            BusProvider.post(new NotificationEvent(getNotificationQueue().poll()));
        }
    }

    public Branch.BranchReferralInitListener getBranchReferralInitListener() {
        return branchReferralInitListener;
    }

    public void setBranchReferralInitListener(Branch.BranchReferralInitListener branchReferralInitListener) {
        this.branchReferralInitListener = branchReferralInitListener;
    }

    public boolean isDuringConfirmFlow(){
        boolean result = false;
        if(topFragmentType()!= null ){

            if(topFragmentType() == ConfirmAccountFragment.class ||
                    topFragmentType() == ConfirmSetPasswordFragment.class ||
                    topFragmentType() == ConfirmDoneFragment.class)
                result =true;
        }
        return result;
    }
}
