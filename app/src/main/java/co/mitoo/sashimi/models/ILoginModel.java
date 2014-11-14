package co.mitoo.sashimi.models;

import co.mitoo.sashimi.models.Pojo.Auth_token;
import co.mitoo.sashimi.models.Pojo.Login;
import rx.Observable;

/**
 * Created by david on 14-11-11.
 */
public interface ILoginModel {

    Observable<Auth_token> login(String username, String password);

}
