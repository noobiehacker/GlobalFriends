package androidJunit;

import com.squareup.otto.Subscribe;

import org.junit.After;
import org.junit.Before;

import java.util.concurrent.CountDownLatch;

import co.mitoo.sashimi.models.SessionModel;
import co.mitoo.sashimi.network.ServiceBuilder;
import co.mitoo.sashimi.network.SteakApi;
import co.mitoo.sashimi.network.mockNetwork.MockSteakApiService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LoginRequestEvent;
import co.mitoo.sashimi.utils.events.UserRecieveResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;


public class LoginModelTest extends MitooPojoTest{

    private SessionModel model;
    private SteakApi mockApi;
    private UserRecieveResponseEvent event;
    public LoginModelTest() {

    }

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());
        model = new SessionModel(null);
        BusProvider.register(this);
    }

    @After
    public void tearDown() throws Exception {
    }


    public void testLoginSuccess() throws Exception {

        signal = new CountDownLatch(1);
        mockApi = new ServiceBuilder().setEndPoint("http://www.mockendpoint.com")
                                      .create(SteakApi.class , new MockSteakApiService(200));
        model.setSteakApiService(mockApi);
        model.onLoginRequest(new LoginRequestEvent("tim@mitoo.com", "password"));
        signal.await();
        assertNotNull(model.getUser());
        assertNotNull(this.event);

    }

    public void testLoginFail() throws Exception {

        testFailureStatusCode(400);
        testFailureStatusCode(401);
        testFailureStatusCode(422);
        testFailureStatusCode(500);

    }

    private void testFailureStatusCode(int statusCode){

        signal = new CountDownLatch(1);
        mockApi = new ServiceBuilder().create(SteakApi.class , new MockSteakApiService(200));
        model.setSteakApiService(mockApi);
        model.onLoginRequest(new LoginRequestEvent("tim@mitoo.com", "password"));
        try{
            signal.await();
        }catch(Exception e){

        }
        assertEquals(this.error.getRetrofitError().getResponse().getStatus() , statusCode);
    }

    @Subscribe
    public void onLoginResponse(UserRecieveResponseEvent event){
        this.event=event;
        signal.countDown();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        this.error = error;
        signal.countDown();
    }

}