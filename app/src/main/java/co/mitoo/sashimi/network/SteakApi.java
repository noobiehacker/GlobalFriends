package co.mitoo.sashimi.network;

import co.mitoo.sashimi.models.jsonPojo.send.EmailSend;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.UserSend;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by david on 14-11-10.
 */
public interface SteakApi {

    @POST("/registrations")
    Observable<UserRecieve>  createRegistration(@Query("save_type") String param1, @Body UserSend jsonObject);

    @POST("/sessions")
    Observable<UserRecieve>  createSession(@Body UserSend jsonObject);

    @POST("/passwords/reset")
    Observable<Response>  resetPassword(@Body EmailSend jsonObject);

    @DELETE("/sessions")
    Observable<UserRecieve> deleteSession();

}