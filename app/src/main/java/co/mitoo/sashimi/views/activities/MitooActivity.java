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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import com.greenhalolabs.facebooklogin.FacebookLoginActivity;
import com.newrelic.agent.android.NewRelic;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.managers.MitooLocationManager;
import co.mitoo.sashimi.models.IUserModel;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.MitooModel;
import co.mitoo.sashimi.models.SessionModel;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentFactory;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.ModelRetriever;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.GpsRequestEvent;
import co.mitoo.sashimi.utils.events.LocationPromptEvent;
import co.mitoo.sashimi.utils.events.SessionLoadedResponseEvent;
import co.mitoo.sashimi.views.fragments.LoginFragment;
import co.mitoo.sashimi.views.fragments.MitooFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MitooActivity extends Activity {

    private MitooLocationManager locationManager;
    private MitooFragment topFragment;
    private Handler handler;
    private Runnable runnable;
    private IUserModel model;
    private Stack<MitooFragment> fragmentStack;
    private List<MitooModel> mitooModelList;
    private ModelRetriever modelRetriever;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeFields();
        BusProvider.register(this);
        super.onCreate(savedInstanceState);
        locationManager = new MitooLocationManager(this);
        setContentView(R.layout.activity_mitoo);
        swapFragment(new FragmentChangeEvent(this, MitooEnum.fragmentTransition.NONE ,R.id.fragment_splash));
        setUpNewRelic();
        setModelRetriever(new ModelRetriever(this));
        setUpPersistenceData();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        fragmentStack= new Stack<MitooFragment>();
        mitooModelList = new ArrayList<MitooModel>();
        addModel(SessionModel.class);
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
    public void userLoadedResponse(SessionLoadedResponseEvent event) {

        final SessionLoadedResponseEvent eventToPassIn = event;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                getFragmentManager().popBackStackImmediate();
                if (eventToPassIn.getUserRecieve() != null) {
                    swapFragment(new FragmentChangeEvent(this, MitooEnum.fragmentTransition.PUSH, R.id.fragment_home));

                } else {
                    swapFragment(new FragmentChangeEvent(this, MitooEnum.fragmentTransition.PUSH, R.id.fragment_landing));

                }
            }
        }, 1000);

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

        getFragmentManager().popBackStack();
        fragmentStack.pop();
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
        
        NewRelic.withApplicationToken(getString(R.string.API_key_new_relic)
        ).start(this.getApplication());
        
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

    public void addModel(Class<?> modelClass) {
        
        if (!containsModelType(modelClass)) {
            if (modelClass == LeagueModel.class) {
                addModelToModelList(new LeagueModel(this));
            } else if (modelClass == SessionModel.class) {
                addModelToModelList(new SessionModel(this));
            }
        }
    }    
    
    private void addModelToModelList(MitooModel model){
        
        this.mitooModelList.add(model);
        
    }
    private boolean containsModelType(Class<?> modelClass){
        
        boolean result = false;
        forloop:
        for(MitooModel item : this.mitooModelList){
            if(modelClass.isInstance(item)){
                result = true;
            }
            if(result)
                break forloop;
        }
        return result;
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FacebookLoginActivity.FACEBOOK_LOGIN_REQUEST_CODE) {
            if(fragmentStack.peek() instanceof LoginFragment){
                LoginFragment loginFragment = (LoginFragment)topFragment;
                loginFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public MitooModel getModel(Class<?> modelClass) {

        MitooModel result = null;
        forloop:
        for(MitooModel item : this.mitooModelList){
            if(modelClass.isInstance(item)){
                result = item;
            }
            if(result!=null)
                break forloop;
        }
        return result;
    }

    public ModelRetriever getModelRetriever() {
        return modelRetriever;
    }

    public void setModelRetriever(ModelRetriever modelRetriever) {
        this.modelRetriever = modelRetriever;
    }
    
    private void setUpPersistenceData(){
        
        if(MitooConstants.persistenceStorage){
            getModelRetriever().getUserModel().loadUser();
        }else{
            getModelRetriever().getUserModel().deleteUser();
        }

    }
}
