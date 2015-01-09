package co.mitoo.sashimi.views.activities;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.widget.Toast;

import com.algolia.search.saas.APIClient;
import com.greenhalolabs.facebooklogin.FacebookLoginActivity;
import com.newrelic.agent.android.NewRelic;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.managers.MitooLocationManager;
import co.mitoo.sashimi.models.IUserModel;
import co.mitoo.sashimi.models.UserModel;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentFactory;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LocationPromptEvent;
import co.mitoo.sashimi.utils.events.LocationRequestEvent;
import co.mitoo.sashimi.views.fragments.LoginFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MitooActivity extends Activity  {

    private MitooLocationManager locationManager;
    private APIClient algoliaClient;
    private Fragment topFragment;
    private Handler handler;
    private Runnable runnable;
    private IUserModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BusProvider.register(this);
        super.onCreate(savedInstanceState);
        hideActionBar();
        locationManager = new MitooLocationManager(this);
        setContentView(R.layout.activity_mitoo);
        changeFragment(R.id.fragment_splash, false);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                MitooActivity.this.changeFragment(R.id.fragment_landing, false);
            }
        }, 1000);
        setUpNewRelic();
        setUpAlgolia();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mitoo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        model = new UserModel(getResources());
    }
    
    @Subscribe
    public void onFragmentChange(FragmentChangeEvent event) {

        if(!event.isPush()){
            popTopFragment();
        }
        else{
            try{

                changeFragment(event.getFragmentId(), event.isPush());

            }
            catch(Exception e){

            }
        }
    }

    @Subscribe
    public void locationRequest(LocationRequestEvent event){

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

    public boolean LocationServicesIsOn(){
        return locationManager.LocationServicesIsOn();
    }

    public void changeFragment(int fragmentId, boolean push) {

        Fragment fragment = FragmentFactory.getInstance().buildFragment(fragmentId);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(push)
            ft.addToBackStack(null);
        ft.setCustomAnimations(R.anim.enter, R.anim.exit);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
        setTopFragment(fragment);

    }

    private void popTopFragment(){

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        }, 1000);

    }
    
    
    private void hideActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    public boolean NetWorkConnectionIsOn() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    
    private void setUpNewRelic(){
        NewRelic.withApplicationToken(
                "AA122023d85b82c7e121b4e55f6d8b0a524f5132d2"
        ).start(this.getApplication());
        
    }

    private void setUpAlgolia(){
        algoliaClient = new APIClient("1ESI9QYTPJ" , "8c45f667d9649e81fa2a735dacefaa42") ;
    }
    
    private void setTopFragment(Fragment fragment){
        topFragment=fragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FacebookLoginActivity.FACEBOOK_LOGIN_REQUEST_CODE) {
            if(topFragment instanceof LoginFragment){
                LoginFragment loginFragment = (LoginFragment)topFragment;
                loginFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
