package co.mitoo.sashimi.views.activities;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.managers.MitooLocationManager;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentFactory;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LocationPromptEvent;
import co.mitoo.sashimi.utils.events.LocationRequestEvent;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MitooActivity extends Activity  {

    private MitooLocationManager locationManager;
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

    @Subscribe
    public void onFragmentChange(FragmentChangeEvent event) {

        try{

            changeFragment(event.getFragmentId(), event.isPush());

        }
        catch(Exception e){

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


}
