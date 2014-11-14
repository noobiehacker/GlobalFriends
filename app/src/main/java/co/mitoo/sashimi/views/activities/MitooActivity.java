package co.mitoo.sashimi.views.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import com.manuelpeinado.glassactionbar.GlassActionBarHelper;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.listener.FragmentChangeListener;
import co.mitoo.sashimi.views.fragments.LandingFragment;
import co.mitoo.sashimi.views.fragments.MitooFragment;
import co.mitoo.sashimi.views.fragments.SplashScreenFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MitooActivity extends Activity implements FragmentChangeListener {

    private GlassActionBarHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitoo);
        changeFragment(SplashScreenFragment.newInstance(MitooActivity.this), false);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                changeFragment(LandingFragment.newInstance(MitooActivity.this), false);
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    public void onFragmentChange(FragmentChangeEvent event) {

        try{

            Fragment fragment =  (Fragment) event.getFragmentType().newInstance();
            if((MitooFragment.class).isAssignableFrom(event.getFragmentType())) {

                ((MitooFragment) fragment).setViewlistner(this);

            }
            changeFragment(fragment, event.isPush());

        }
        catch(Exception e){

        }

    }

    public void changeFragment(Fragment fragment, boolean push) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(push)
            ft.addToBackStack(null);
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }
}
