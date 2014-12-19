package co.mitoo.sashimi.models;

import android.content.res.Resources;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.models.jsonPojo.send.EmailSend;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserRecieve;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.StaticString;
import co.mitoo.sashimi.utils.events.JoinRequestEvent;
import co.mitoo.sashimi.utils.events.LoginRequestEvent;
import co.mitoo.sashimi.utils.events.MitooRequestEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordRequestEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordResponseEvent;
import co.mitoo.sashimi.utils.events.UserRecieveResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.Subscriber;


/**
 * Created by david on 14-11-11.
 */
public class UserModel extends MitooModel implements IUserModel {

    public UserModel(Resources resources) {
        super( resources );
    }

    private UserRecieve user;

    public UserRecieve getUser() {
        return user;
    }

    public void setUser(UserRecieve user) {
        this.user = user;
    }

    @Override
    @Subscribe public void onLoginAttempt(LoginRequestEvent event){

        if(user==null){
            handleRequestEvent(event);
        }else{
            postUserRecieveResponse();
        }

    }

    @Override
    @Subscribe public void onJoinAttempt(JoinRequestEvent event) {

        if(user==null){
            handleRequestEvent(event);
        }else{
            postUserRecieveResponse();
        }
    }

    @Override
    @Subscribe public void onResetPasswordAttempt(ResetPasswordRequestEvent event) {

        handleObservable(getSteakApiService().resetPassword(new EmailSend(event.getEmail())),
                Response.class);
    }

    @Subscribe public void onApiFailEvent(RetrofitError event){
        BusProvider.post(new MitooActivitiesErrorEvent(event));
    }

    private void handleRequestEvent(MitooRequestEvent event) {


        if (event instanceof JoinRequestEvent) {

            JoinRequestEvent joinRequestEvent = (JoinRequestEvent) event;
            handleObservable(getSteakApiService().createRegistration(
                            StaticString.apiConstantRegister, joinRequestEvent.getLogin()),
                    UserRecieve.class);

        } else if (event instanceof LoginRequestEvent) {

            LoginRequestEvent loginRequestEvent = (LoginRequestEvent) event;
            handleObservable(getSteakApiService().createSession(loginRequestEvent.getLogin()),
                    UserRecieve.class);
        }
    }

    private <T> void handleObservable(Observable<T> observable, Class<T> classType){
        observable.subscribe(createSubscriber(classType));
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

                handleObserverResponse(objectRecieve);
            }
        };
    }

    private void handleObserverResponse(Object objectRecieve){

        if(objectRecieve instanceof  UserRecieve)
        {
            UserModel.this.setUser((UserRecieve)objectRecieve);
            postUserRecieveResponse();

        }else if (objectRecieve instanceof Response){
            postResetPasswordResponse((Response)objectRecieve);
        }
    }

    private void postUserRecieveResponse(){

        BusProvider.post(new UserRecieveResponseEvent(this.user));

    }

    private void postResetPasswordResponse(Response response){

        BusProvider.post(new ResetPasswordResponseEvent(response));

    }
    
    private void handleHttpResponse(Response response){
        if(response.getStatus()==204) {
            postResetPasswordResponse(response);
        }else {
            BusProvider.post(new MitooActivitiesErrorEvent(StaticString.errorMessage));
        }
    }
}
