package co.mitoo.sashimi.views.activities;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
//import com.newrelic.agent.android.NewRelic;
import com.squareup.otto.Subscribe;
import java.util.Stack;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.managers.MitooLocationManager;
import co.mitoo.sashimi.managers.UserDataManager;
import co.mitoo.sashimi.models.LocationModel;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.network.ServiceBuilder;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentFactory;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.managers.ModelManager;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.GpsRequestEvent;
import co.mitoo.sashimi.utils.events.LocationPromptEvent;
import co.mitoo.sashimi.utils.events.LogOutEvent;
import co.mitoo.sashimi.views.fragments.MitooFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MitooActivity extends Activity {

    private MitooLocationManager locationManager;
    private Handler handler;
    private Stack<MitooFragment> fragmentStack;
    private ModelManager modelManager;
    private UserDataManager userDataManager;

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
        locationManager.connect();
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop() {
        locationManager.disconnect();
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
    
    private void initializeFields(){
        setModelManager(new ModelManager(this));
        setUpNewRelic();
        locationManager = new MitooLocationManager(this);
        BusProvider.register(this);
    }
    
    @Subscribe
    public void onFragmentChange(FragmentChangeEvent event) {

        if(event.getFragmentId()==R.id.fragment_home){
               popAllFragments();
        }
        switch(event.getTransition()) {
            case PUSH:
                pushFragment(event);
                break;
            case CHANGE:
            case NONE:
                swapFragment(event);
                break;
            case POP:
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        MitooActivity.this.popFragment();
                    }
                }, 1000);
                fragmentStack.pop();
                break;
        }
    }

    @Subscribe
    public void locationRequest(GpsRequestEvent event){

        if(locationManager.LocationServicesIsOn()){
            locationManager.locationRequest();
        }else{
            BusProvider.post(new LocationPromptEvent());
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
        ft.setCustomAnimations(R.anim.enter, R.anim.exit);
        ft.addToBackStack(null);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        fragmentStack.push(fragment);

    }

    private void popFragment(){

        if(fragmentStack.size()>0){
            getFragmentManager().popBackStack();
            fragmentStack.pop();
        }
    }

    private void swapFragment(FragmentChangeEvent event){

        MitooFragment fragment = FragmentFactory.getInstance().buildFragment(event);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(event.getTransition() != MitooEnum.fragmentTransition.NONE){
            ft.setCustomAnimations(R.anim.enter, R.anim.exit);
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
            if(this.fragmentStack.peek().isAllowBackPressed())
                popFragment();
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
    
    private boolean allDataLoaded(){
        return false;
        
    }
    
    public void startApp(){

        fragmentStack= new Stack<MitooFragment>();
        swapFragment(new FragmentChangeEvent(this, MitooEnum.fragmentTransition.NONE, R.id.fragment_splash));
        
    }
    
    @Subscribe
    public void logOut(LogOutEvent event){

        getModelManager().deleteAllPersistedData();
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

    public UserDataManager getUserDataManager() {
        return userDataManager;
    }

    public void setUserDataManager(UserDataManager userDataManager) {
        this.userDataManager = userDataManager;
    }


}
