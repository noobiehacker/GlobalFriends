package co.mitoo.sashimi.models;

import android.content.res.Resources;

import co.mitoo.sashimi.models.Pojo.Auth_token;
import co.mitoo.sashimi.models.Pojo.Login;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * Created by david on 14-11-11.
 */
public class LoginModel extends MitooModel implements ILoginModel {

    public LoginModel(Resources resources) {
        super( resources );
    }

    public Observable<Auth_token> login(String username, String password) {

        final Login loginPojo = new Login(username, password);

        return Observable.create(new Observable.OnSubscribe<Auth_token>() {
            @Override
            public void call(Subscriber<? super Auth_token> subscriber) {
                final Subscriber sub = subscriber;
                getSteakApiService().createLogin(loginPojo, new Callback<Auth_token>() {
                    @Override
                    public void success(Auth_token token, Response response) {
                        sub.onNext(token);
                        sub.onCompleted();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }).subscribeOn(Schedulers.io());

    }

}
