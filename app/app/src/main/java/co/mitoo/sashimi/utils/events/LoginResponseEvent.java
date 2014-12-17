package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.UserRecieve;
import retrofit.RetrofitError;

/**
 * Created by david on 14-11-26.
 */
public class LoginResponseEvent {

    public LoginResponseEvent(UserRecieve token ){
        this.user = token;
    }

    public UserRecieve getUser() {
        return user;
    }

    private UserRecieve user;

}
