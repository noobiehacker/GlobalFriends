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
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.newrelic.agent.android.NewRelic;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import java.util.Stack;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.managers.MitooLocationManager;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.network.DataPersistanceService;
import co.mitoo.sashimi.network.ServiceBuilder;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.FragmentFactory;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.managers.ModelManager;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LogOutEvent;
import co.mitoo.sashimi.views.fragments.MitooFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MitooActivity extends Activity {

    private MitooLocationManager locationManager;
    private Handler handler;
    private Runnable runnable;
    private Stack<MitooFragment> fragmentStack;
    private ModelManager modelManager;
    private DataHelper dataHelper;
    private Picasso picasso;
    protected DataPersistanceService persistanceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initializeFields();
        startApp();
        setContentView(R.layout.activity_mitoo);
        setUpPersistenceData();

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
        tearDownReferences();
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initializeFields(){
        initializeStatusBarColor();
        setModelManager(new ModelManager(this));
        setUpNewRelic();
        setUpInitialCalligraphy();
        setLocationManager(new MitooLocationManager(this));
        BusProvider.register(this);
    }
    
    private void setUpInitialCalligraphy(){

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(getString(R.string.DIN_Regular))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    @Subscribe
    public void onFragmentChange(final FragmentChangeEvent event) {

        if(fragmentIsRoot(event.getFragmentId()))
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
    public void startIntent(Intent intent){
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public boolean LocationServicesIsOn(){
        return getLocationManager().LocationServicesIsOn();
    }


    private void pushFragment(FragmentChangeEvent event){

        MitooFragment fragment = FragmentFactory.getInstance().buildFragment(event);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        setFragmentAnimation(ft, event.getAnimation());
        ft.addToBackStack(String.valueOf(event.getFragmentId()));
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        getFragmentStack().push(fragment);

    }

    private void swapFragment(FragmentChangeEvent event){

        MitooFragment fragment = FragmentFactory.getInstance().buildFragment(event);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        setFragmentAnimation(ft, event.getAnimation());
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        getFragmentStack().push(fragment);

    }

    private void setFragmentAnimation(FragmentTransaction transction , MitooEnum.FragmentAnimation animation){
        
        if(animation == MitooEnum.FragmentAnimation.HORIZONTAL)
            setLeftToRightAnimation(transction);
        else if(animation == MitooEnum.FragmentAnimation.VERTICAL)
            setBottomToTopAnimation(transction);
        else if(animation == MitooEnum.FragmentAnimation.DOWNLEFT)
            setBottomToTopAnimation(transction);
    }
    
    private void setBottomToTopAnimation(FragmentTransaction transaction){

        transaction.setCustomAnimations(R.anim.enter_top, R.anim.no_animation,
                0, R.anim.enter_bottom);

    }

    private void setLeftToRightAnimation(FragmentTransaction transaction){

        transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_right,
                R.anim.exit_left,R.anim.enter_left);
    }

    private void setDownLeftAnimation(FragmentTransaction transaction){

        transaction.setCustomAnimations(R.anim.exit_bottom, R.anim.enter_top);
    }
    
    public void popFragment() {

        if (getFragmentStack().size() > 0) {
            getFragmentStack().pop();
            getFragmentManager().popBackStack();
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

    public void popAllFragments(){

        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        while(getFragmentStack().size() > 0)
            getFragmentStack().pop();

    }
    
    public boolean NetWorkConnectionIsOn() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    private void setUpNewRelic(){
        
        NewRelic.withApplicationToken(getDataHelper().getNewRelicKey())
                .start(this.getApplication());

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.moveTaskToBack(true);
        } else {
            if (getFragmentStack().peek().isAllowBackPressed())  {
                hideSoftKeyboard();
                MitooFragment fragmentToDesplay = getSecondTopFragment();
                if(fragmentToDesplay!=null && fragmentToDesplay.popActionRequiresDelay())
                    popFragment(250);
                else
                    popFragment();
            }
        }
    }
    
    private MitooFragment getSecondTopFragment(){
        
        if(getFragmentStack().size()>1){
            return getFragmentStack().get(getFragmentStack().size()-2);
        }
        return null;
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*
        if (requestCode == FacebookLoginActivity.FACEBOOK_LOGIN_REQUEST_CODE) {
            if(fragmentStack.peek() instanceof LoginFragment){
                LoginFragment loginFragment = (LoginFragment)fragmentStack.peek();
                loginFragment.onActivityResult(requestCode, resultCode, data);
            }
        }*/
    }

    public ModelManager getModelManager() {
        return modelManager;
    }

    public void setModelManager(ModelManager modelManager) {
        this.modelManager = modelManager;
    }

    private void setUpPersistenceData(){

        if(MitooConstants.getPersistenceStorage()){
            getModelManager().readAllPersistedData();
        }else{
            getModelManager().deleteAllPersistedData();
        }

    }

    public void updateAuthToken(SessionRecieve session){

        if(session.auth_token!=null)
            ServiceBuilder.getSingleTonInstance().setXAuthToken(session.auth_token);
    }

    public void resetAuthToken(){

        ServiceBuilder.getSingleTonInstance().resetXAuthToken();
    }

    public void startApp(){

        setFragmentStack(new Stack<MitooFragment>());

        FragmentChangeEvent event =
                new FragmentChangeEvent(this, MitooEnum.FragmentTransition.NONE,
                        R.id.fragment_splash , MitooEnum.FragmentAnimation.NONE);

        BusProvider.post(event);
    }

    @Subscribe
    public void logOut(LogOutEvent event){

        getModelManager().deleteAllPersistedData();
        resetAuthToken();
        popAllFragments();

        FragmentChangeEvent fragmentChangeEvent =
                new FragmentChangeEvent(this, MitooEnum.FragmentTransition.NONE,
                        R.id.fragment_landing , MitooEnum.FragmentAnimation.HORIZONTAL);

        BusProvider.post(fragmentChangeEvent );

    }

    public void contactMitoo(){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.mitoo_support_email_address)});
        intent.putExtra(Intent.EXTRA_SUBJECT , getString(R.string.mitoo_support_email_subject));
        intent.putExtra(Intent.EXTRA_TEXT , getModelManager().getUserInfoModel().getUserInfoRecieve().email);
        startActivity(Intent.createChooser(intent, "Send email..."));

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
        getHandler().postDelayed(getRunnable(),delayed);
        
    }
    public void hideSoftKeyboard() {
        
        if (this.getCurrentFocus() != null) {
            View view = this.getCurrentFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }
    private void initializeStatusBarColor(){

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_dark_six));
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
            getHandler().removeCallbacksAndMessages(null);
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
}


