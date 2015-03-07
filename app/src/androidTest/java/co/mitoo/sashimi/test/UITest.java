package co.mitoo.sashimi.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-02-24.
 */
public class UITest extends
        ActivityInstrumentationTestCase2<MitooActivity> {

    private Solo solo;

    public UITest() {
        super(MitooActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}