package androidJunit; /**
 * Created by david on 14-12-01.
 */
import com.squareup.otto.Subscribe;

import org.junit.After;
import org.junit.Before;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import co.mitoo.sashimi.models.jsonPojo.send.EmailSend;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.UserSend;
import co.mitoo.sashimi.models.jsonPojo.send.Login;
import co.mitoo.sashimi.network.ServiceBuilder;
import co.mitoo.sashimi.network.SteakApi;
import co.mitoo.sashimi.network.mockNetwork.MockSteakApiService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.StaticString;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import retrofit.MockRestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class SteakApiServiceTest extends MitooPojoTest{

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNoNetwork() throws Exception {

        ServiceBuilder builder = new ServiceBuilder();
        SteakApi api = builder.setEndPoint(StaticString.steakEndPoint)
                .create(SteakApi.class, new MockSteakApiService(200));
        MockRestAdapter adapter = builder.getMockRestAdapter();
        adapter.setErrorPercentage(100);
        String userID= UUID.randomUUID().toString();
        testCreateSession(userID);
        assertEquals(this.error.getRetrofitError().getKind(), RetrofitError.Kind.NETWORK);

    }

    public void testCreateRegSessionResetPasswordPass() throws Exception {

        String userID= UUID.randomUUID().toString();
        testCreateRegistration(userID );
        testCreateSession(userID);
        testResetPassword(userID);

    }

    public void testCreateRegFail() throws Exception {

        String userID= UUID.randomUUID().toString();
        testCreateRegistration(userID );
        testCreateRegistration(userID);

    }

    public void testCreateSessionFail() throws Exception {

        String userID= UUID.randomUUID().toString();
        testCreateRegistration(userID);

    }

    public void deleteSession() throws Exception {

        SteakApi api = new ServiceBuilder().setEndPoint(StaticString.steakEndPoint)
                                           .create(SteakApi.class);
        Observable<UserRecieve> recieve = api.deleteSession();
        recieve.subscribe(new Action1<UserRecieve>() {
            public void call(UserRecieve s) {
                assertNotNull(s);
            }
        });


    }
    private void testCreateRegistration(String userID) throws Exception {

        SteakApi api = new ServiceBuilder().setEndPoint(StaticString.steakEndPoint)
                .create(SteakApi.class);
        Observable<UserRecieve> observable = api.createRegistration(new UserSend(userID, StaticString.getString("password")));
        testApiMethod(observable,UserRecieve.class);
        assertNotNull(getItemToCheck());
        setItemToCheck(null);

    }

    private void testCreateSession(String userID) throws Exception {

        SteakApi api = new ServiceBuilder().setEndPoint(StaticString.steakEndPoint)
                .create(SteakApi.class);
        Observable<UserRecieve> observable = api.createSession(new Login(userID, StaticString.getString("password")));
        testApiMethod(observable, UserRecieve.class);
        assertNotNull(getItemToCheck());
        setItemToCheck(null);

    }

    private void testResetPassword(String userID) throws Exception {

        SteakApi api = new ServiceBuilder().setEndPoint(StaticString.steakEndPoint)
                .create(SteakApi.class);
        Observable<Response> observable = api.resetPassword(new EmailSend(userID));
        testApiMethod(observable , Response.class);
        assertNotNull(getItemToCheck());
        setItemToCheck(null);

    }

    private <T> void testApiMethod(Observable<T> observable , Class<T> classType) throws Exception {

        signal = new CountDownLatch(1);
        handleObservable(observable , classType);
        signal.await();;

    }

    private <T> void handleObservable(Observable<T> observable , Class<T> classType){
        observable.subscribe(createSubscriber(classType));
    }

    @Subscribe
    public void onObserverResponse(Object object){
        setItemToCheck(object);
        signal.countDown();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        this.error = error;
        signal.countDown();
    }


    private <T> Subscriber<T> createSubscriber(Class<T> objectRecieve){
        return new Subscriber<T>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(T objectRecieve) {
                BusProvider.post(objectRecieve);
            }
        };
    }

}
