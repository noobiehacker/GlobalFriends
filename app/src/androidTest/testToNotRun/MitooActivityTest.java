package espresso;
import android.test.ActivityInstrumentationTestCase2;
import com.google.android.apps.common.testing.ui.espresso.ViewInteraction;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.squareup.otto.Subscribe;
import java.util.concurrent.CountDownLatch;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.registerIdlingResources;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;
import TestHelperClass.MitooIdlingResource;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.R;

/**
 * Created by david on 14-12-10.
 */
public class MitooActivityTest  extends ActivityInstrumentationTestCase2<MitooActivity> {

    public MitooActivityTest() {
        super(MitooActivity.class);
    }

    protected MitooActivitiesErrorEvent error;
    protected Object itemToCheck ;
    protected CountDownLatch signal = new CountDownLatch(1);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MitooActivity activity =getActivity();
        registerIdlingResources(new MitooIdlingResource(R.id.fragment_landing));
        BusProvider.register(this);
        signal.await();
    }

    public void testLandingFragmentExist() {

        //Test if certain elements of the Landing Fragment exists
        onView(withText(R.id.slider)).check(ViewAssertions.matches(isDisplayed()));
    }

    public void testCompeitionSearchButtonExist() {

        //Test if certain elements of the Landing Fragment exists
        onView(withId(R.id.searchButton)).check(ViewAssertions.matches(isDisplayed()));
    }

    public void testLoginButtonExist() {

        //Test if certain elements of the Landing Fragment exists
        onView(withId(R.id.searchButton)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Subscribe
    public void onObserverResponse(Object object){
        signal.countDown();
    }
}
