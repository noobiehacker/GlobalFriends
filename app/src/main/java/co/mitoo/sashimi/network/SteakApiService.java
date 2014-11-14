package co.mitoo.sashimi.network;

import co.mitoo.sashimi.models.Pojo.Auth_token;
import co.mitoo.sashimi.models.Pojo.Login;
import co.mitoo.sashimi.models.Pojo.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by david on 14-11-10.
 */
public interface SteakApiService {

    @POST("/registrations")
    void createUser(@Body User user, Callback<Auth_token> cb);

    @POST("/sessions")
    void createLogin(@Body Login login, Callback<Auth_token> cb);

}
