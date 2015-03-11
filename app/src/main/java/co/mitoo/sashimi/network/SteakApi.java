package co.mitoo.sashimi.network;

import java.util.List;

import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.JsonLeagueEnquireSend;
import co.mitoo.sashimi.models.jsonPojo.send.JsonResetPasswordSend;
import co.mitoo.sashimi.models.jsonPojo.send.JsonLoginSend;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
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

    @POST("/auth/v1/registrations")
    Observable<SessionRecieve>  createRegistration(@Query("save_type") String param1, @Body JsonSignUpSend jsonObject);

    @POST("/auth/v1/sessions")
    Observable<SessionRecieve>  createSession(@Body JsonLoginSend jsonObject);

    @POST("/auth/v1/passwords/reset")
    Observable<Response>  resetPassword(@Body JsonResetPasswordSend jsonObject);

    @DELETE("/auth/v1/sessions")
    Observable<SessionRecieve> deleteSession();

    @POST("/leagues/v1/leagues/{id}/league_enquiries")
    Observable<Response>  createLeagueEnquiries(@Path("id") int id , @Body JsonLeagueEnquireSend jsonObject);

    @GET("/leagues/v1/leagues")
    Observable<League[]>  getLeagueEnquiries(@Query("filter") String filter , @Query("user_id") int user_id);

    @GET("/users/v1/users/{id}")
    Observable<UserInfoRecieve>  getUser(@Path("id") int id);

    @GET("/leagues/v1/competition_seasons/{id}/teams")
    Observable<Team[]>  getTeam(@Path("id") int id);

    @GET("/leagues/v1/teams/{id}/fixtures")
    Observable<Fixture[]>  getFixtureFromTeamID(@Query("filter") String filter ,@Path("id") int id);

}
