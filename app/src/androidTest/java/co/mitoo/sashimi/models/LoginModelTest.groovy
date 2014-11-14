package co.mitoo.sashimi.models

import android.content.res.Resources
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Created by david on 14-11-13.
 */
@RunWith(RobolectricTestRunner.class)
public class LoginModelTest {

    private LoginModel loginModel;

    void setUp() {
        super.setUp()
        loginModel = new LoginModel(Resources.getSystem());
    }

    void tearDown() {

    }

    @Test
    void testLogin() {
        loginModel.login("abc@mitoo.com" , "abc123");
    }
}
