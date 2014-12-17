package androidJunit; /**
 * Created by david on 14-12-01.
 */

import android.test.AndroidTestCase;

import com.squareup.otto.Subscribe;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.CountDownLatch;

import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;


public abstract class MitooPojoTest extends AndroidTestCase{

    protected MitooActivitiesErrorEvent error;
    protected CountDownLatch signal = new CountDownLatch(1);
    protected Object itemToCheck ;

    @Before
    public void setUp() throws Exception {
        BusProvider.register(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    public Object getItemToCheck() {
        return itemToCheck;
    }

    public void setItemToCheck(Object itemToCheck) {
        this.itemToCheck = itemToCheck;
    }

    @Subscribe
    public void getError(MitooActivitiesErrorEvent error){
        this.error = error;
    }
}
