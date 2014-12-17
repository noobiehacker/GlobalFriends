package androidJunit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.views.activities.MitooActivity;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.robolectric.Robolectric.clickOn;
import static org.robolectric.Robolectric.shadowOf;
/**
 * Created by david on 14-12-17.
 */
@RunWith(RobolectricTestRunner.class)
public class SamleRoboTest {
    @Test
    public void shouldHaveApplicationName() throws Exception {
        String appName = new MitooActivity().getResources().getString(R.string.app_name);
        assertThat(appName, equalTo("MyActivity"));
    }
}