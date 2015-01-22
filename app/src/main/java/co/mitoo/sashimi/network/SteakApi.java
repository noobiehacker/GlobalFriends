package co.mitoo.sashimi.network;

import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.JsonLeagueEnquireSend;
import co.mitoo.sashimi.models.jsonPojo.send.JsonResetPasswordSend;
import co.mitoo.sashimi.models.jsonPojo.send.JsonLoginSend;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by david on 14-11-10.
 */
public interface SteakApi {

    @POST("/registrations")
    Observable<SessionRecieve>  createRegistration(@Query("save_type") String param1, @Body JsonLoginSend jsonObject);

    @POST("/sessions")
    Observable<SessionRecieve>  createSession(@Body JsonLoginSend jsonObject);

    @POST("/leagues/{id}/league_enquiries")
    Observable<Response>  createLeagueEnquiries(@Path("id") int id , @Body JsonLeagueEnquireSend jsonObject);

    @POST("/passwords/reset")
    Observable<Response>  resetPassword(@Body JsonResetPasswordSend jsonObject);

    @GET("/users/{id}")
    Observable<UserRecieve>  getUser(@Path("id") int id);
    
    @DELETE("/sessions")
    Observable<SessionRecieve> deleteSession();

}
