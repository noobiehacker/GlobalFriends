package co.mitoo.sashimi.views.activities;
import android.app.Activity;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MitooActivity extends Activity {

    private MitooLocationManager locationManager;
    private Handler handler;
    private Runnable runnable;
    private Stack<MitooFragment> fragmentStack;
    private ModelManager modelManager;
    private DataHelper dataHelper;
    private Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        startApp();
        initializeFields();
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
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    private void initializeFields(){
        initializeStatusBarColor();
        setModelManager(new ModelManager(this));
        setUpNewRelic();
        locationManager = new MitooLocationManager(this);
        BusProvider.register(this);
    }

    @Subscribe
    public void onFragmentChange(final FragmentChangeEvent event) {

        if (event.getFragmentId() == R.id.fragment_home) {
            popAllFragments();
        }
        getHandler().post(new Runnable() {
            public void run() {
                MitooActivity.this.fragmentTransition(event);
            }
        });
       
    }
    
    private void fragmentTransition(FragmentChangeEvent event){

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
        return locationManager.LocationServicesIsOn();
    }


    private void pushFragment(FragmentChangeEvent event){

        MitooFragment fragment = FragmentFactory.getInstance().buildFragment(event);
        if(event.getBundle()!=null)
            fragment.setArguments(event.getBundle());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_right,R.anim.exit_left,R.anim.enter_left);
        ft.addToBackStack(null);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        fragmentStack.push(fragment);

    }

    public void popFragment() {

        if (fragmentStack.size() > 0) {
            fragmentStack.pop();
            getFragmentManager().popBackStack();
        }

    }

    private void swapFragment(FragmentChangeEvent event){

        MitooFragment fragment = FragmentFactory.getInstance().buildFragment(event);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(event.getTransition() != MitooEnum.fragmentTransition.NONE){
            ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_right);
        }
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        if(fragmentStack.size()>0)
            fragmentStack.pop();
        fragmentStack.push(fragment);

    }

    private void popAllFragments(){

        while(fragmentStack.size()>0){
            popFragment();
        }

    }

    public boolean NetWorkConnectionIsOn() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    private void setUpNewRelic(){
        
      /*  NewRelic.withApplicationToken(getString(R.string.API_key_new_relic)
        ).start(this.getApplication());
        */
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.moveTaskToBack(true);
        } else {
            if (this.fragmentStack.peek().isAllowBackPressed()) {
                hideSoftKeyboard();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        popFragment();

                    }
                };
                setRunnable(runnable);
                getHandler().postDelayed(runnable, 75);
            }
        }
    }

    public void onBackPressed(View v) {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.moveTaskToBack(true);
        } else {
            if(this.fragmentStack.peek().isAllowBackPressed())
                hideSoftKeyboard();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        popFragment();

                    }
                };
                setRunnable(runnable);
                getHandler().postDelayed(runnable, 125);
        }

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

        if(MitooConstants.persistenceStorage){
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

        fragmentStack= new Stack<MitooFragment>();
        swapFragment(new FragmentChangeEvent(this, MitooEnum.fragmentTransition.NONE, R.id.fragment_splash));

    }

    @Subscribe
    public void logOut(LogOutEvent event){

        getModelManager().deleteAllPersistedData();
        resetAuthToken();
        popAllFragments();
        startApp();
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

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (MitooActivity.this.getCurrentFocus() != null) {
                    View view = MitooActivity.this.getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        };
        setRunnable(runnable);
        getHandler().postDelayed(getRunnable(),250);
        
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
        
        if (getHandler() != null && getRunnable() != null) {
            getHandler().removeCallbacks(getRunnable());
        }

    }

    public void tearDownReferences() {

        handleCallBacks();

    }

}
