package co.mitoo.sashimi.models;

import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import co.mitoo.sashimi.models.jsonPojo.UserCheck;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.CheckUserEvent;
import co.mitoo.sashimi.utils.events.ConfirmInfoSetPasswordRequestEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.RetriggerEmailResponseEvent;
import co.mitoo.sashimi.utils.events.RetriggerEmailSmsEvent;
import co.mitoo.sashimi.utils.events.UserInfoRequestEvent;
import co.mitoo.sashimi.utils.events.UserInfoResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 15-01-21.
 */

public class UserInfoService extends MitooService {

    public UserInfoService(MitooActivity activity) {
        super(activity);
    }

    private UserInfoRecieve userInfoRecieve;

    public UserInfoRecieve getUserInfoRecieve() {
        return userInfoRecieve;
    }

    public void setUserInfoRecieve(UserInfoRecieve userInfoRecieve) {
        this.userInfoRecieve = userInfoRecieve;
    }

    @Subscribe
    public void onUserInfoRequest(UserInfoRequestEvent event) {

        if (getUserInfoRecieve() == null ) {
            handleObservable(getSteakApiService().getUser(event.getUserID()), UserInfoRecieve.class);
        } else {
            postUserInfoRecieveResponse();
        }
    }

    @Subscribe
    public void requestToConfirmUser(ConfirmInfoSetPasswordRequestEvent event) {

        handleObservable(getSteakApiService().createUserFromConfirmation(event.getToken(), createConfirmJsonFrom(event.getPassword()))
                , UserInfoRecieve.class);

    }

    @Subscribe
    public void onRetriggerEvent(RetriggerEmailSmsEvent event) {

        Observable<Response> observable = getSteakApiService().retriggerConfirmationLink(event.getUserID());
        observable.subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Response response) {
                if(response.getStatus()==201)
                    BusProvider.post(new RetriggerEmailResponseEvent(true));

            }
        });
    }

    @Subscribe
    public void onCheckUserEvent(CheckUserEvent event) {

        Observable<UserCheck> observable = getSteakApiService().checkUser(event.getUserIdentifier());
        observable.subscribe(new Subscriber<UserCheck>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UserCheck response) {
                BusProvider.post(response);

            }
        });
    }

    @Subscribe
    public void onApiFailEvent(RetrofitError event) {

        BusProvider.post(new MitooActivitiesErrorEvent(event));
    }

    private void postUserInfoRecieveResponse() {

        BusProvider.post(new UserInfoResponseEvent(getUserInfoRecieve()));

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        setUserInfoRecieve((UserInfoRecieve) objectRecieve);
        updateSessionModelAuthToken();
        postUserInfoRecieveResponse();

    }

    @Override
    public void resetFields() {
        setUserInfoRecieve(null);
    }

    private void updateSessionModelAuthToken() {
        SessionService sessionModel = getActivity().getModelManager().getSessionModel();
        SessionRecieve session = sessionModel.getSession();
        sessionModel.updateSession(getUserInfoRecieve());

    }

    private JsonSignUpSend createConfirmJsonFrom(String password) {

        if(this.userInfoRecieve!=null)
            return new JsonSignUpSend( getTimeZone(),password, this.userInfoRecieve);
        return null;
    }

    private String getTimeZone() {

        DateTime dateTime = new DateTime();
        return dateTime.getZone().toString();
    }


}