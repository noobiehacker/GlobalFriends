package co.mitoo.sashimi.models;

import android.content.res.Resources;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.models.jsonPojo.recieve.UserRecieve;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LoginRequestEvent;
import co.mitoo.sashimi.utils.events.UserRecieveResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;


/**
 * Created by david on 14-11-11.
 */
public class LoginModel extends MitooModel implements IUserModel {

    private UserRecieve user;

    public LoginModel(Resources resources) {
        super( resources );
    }

    @Subscribe public void onLoginAttempt(LoginRequestEvent event){

        if(user==null){
            handleObservable(getSteakApiService().createSession(event.getLogin()));
        }
        else{
            postLoginResponse();
        }
    }

    @Subscribe public void onApiFailEvent(RetrofitError event){
        BusProvider.post(new MitooActivitiesErrorEvent(event));
    }

    private void postLoginResponse(){

        BusProvider.post(new UserRecieveResponseEvent(this.user));
    }

    public UserRecieve getUser() {
        return user;
    }

    public void setUser(UserRecieve user) {
        this.user = user;
    }

    private void handleObservable(Observable<UserRecieve> observable){
        observable.subscribe(new Subscriber<UserRecieve>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(UserRecieve object) {
                setUser(object);
                postLoginResponse();
            }

        });
    }
}
