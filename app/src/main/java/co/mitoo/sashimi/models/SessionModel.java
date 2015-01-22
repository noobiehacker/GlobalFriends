package co.mitoo.sashimi.models;

import android.app.Activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.JsonResetPasswordSend;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.StaticString;
import co.mitoo.sashimi.utils.events.AuthTokenExchangeRequestEvent;
import co.mitoo.sashimi.utils.events.JoinRequestEvent;
import co.mitoo.sashimi.utils.events.LoginRequestEvent;
import co.mitoo.sashimi.utils.events.SessionLoadedResponseEvent;
import co.mitoo.sashimi.utils.events.TokenRequestEvent;
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

public class SessionModel extends MitooModel {

    public SessionModel(Activity activity) {
        super(activity);
        setSharedPreferenceKey(getActivity().getString(R.string.shared_preference_user_key));
    }

    private SessionRecieve session;

    public SessionRecieve getSession() {
        return session;
    }

    public void setSession(SessionRecieve user) {
        this.session = user;
    }

    @Subscribe public void onLoginRequest(LoginRequestEvent event){

        handleRequestEvent(event);

    }

    @Subscribe public void onJoinRequest(JoinRequestEvent event) {

        handleRequestEvent(event);

    }

    @Subscribe public void onResetPasswordRequest(ResetPasswordRequestEvent event) {

        handleObservable(getSteakApiService().resetPassword(new JsonResetPasswordSend(event.getEmail())),
                Response.class);
    }

    @Subscribe public void onAuthTokenExchangeRequest(AuthTokenExchangeRequestEvent event) {
        
        handleRequestEvent(event);
    }

    @Subscribe public void onApiFailEvent(RetrofitError event){
        BusProvider.post(new MitooActivitiesErrorEvent(event));
    }

    private void handleRequestEvent(TokenRequestEvent event) {

        if (event instanceof JoinRequestEvent) {

            JoinRequestEvent joinRequestEvent = (JoinRequestEvent) event;
            handleObservable(getSteakApiService().createRegistration(
                            StaticString.apiConstantRegister, joinRequestEvent.getCredentials()),
                    SessionRecieve.class);

        } else if (event instanceof LoginRequestEvent) {

            LoginRequestEvent loginRequestEvent = (LoginRequestEvent) event;
            handleObservable(getSteakApiService().createSession(loginRequestEvent.getLogin()),
                    SessionRecieve.class);
        }else if (event instanceof AuthTokenExchangeRequestEvent){
            
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

        if(objectRecieve instanceof SessionRecieve)
        {
            setSession((SessionRecieve) objectRecieve);
            saveUser();
            postUserRecieveResponse();

        }else if (objectRecieve instanceof Response){
            postResetPasswordResponse((Response)objectRecieve);
        }
    }

    private void postUserRecieveResponse(){

        BusProvider.post(new UserRecieveResponseEvent(getSession()));

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

    public void removeReferences(){
        super.removeReferences();
    }
    
    public void loadUser(){

        this.serializeRunnable =new Runnable() {
            @Override
            public void run() {

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String savedUserSerialized = getSavedObjectData(getSharedPreferenceKey() , getSharedPreferenceErrorValue());
                    if(savedUserSerialized!=getSharedPreferenceErrorValue()){
                        setSession(objectMapper.readValue(savedUserSerialized, SessionRecieve.class));
                    }
                    BusProvider.post(new SessionLoadedResponseEvent(getSession()));
                }
                catch(Exception e){
                }
            }
        };
        runRunnableOnNewThread(this.serializeRunnable);

    }

    public void saveUser(){
        
        this.serializeRunnable =new Runnable() {
            @Override
            public void run() {

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String serializedValue = objectMapper.writeValueAsString(getSession());
                    saveStringToPreference(getActivity().getString(R.string.shared_preference_user_key) , serializedValue);
                }
                catch(Exception e){
                }
            }
        };

        runRunnableOnNewThread(this.serializeRunnable);

    }
    
    public void deleteUser(){

        this.serializeRunnable = new Runnable() {
            @Override
            public void run() {

                try {
                    deleteStringFromPreference(getSharedPreferenceKey());
                    BusProvider.post(new SessionLoadedResponseEvent(null));
                }
                catch(Exception e){
                }
            }
        };

        runRunnableOnNewThread(this.serializeRunnable);

    }
    
    private void runRunnableOnNewThread(Runnable runnable){

        Thread t = new Thread(runnable);
        t.start();
        
    }
    

        
}
