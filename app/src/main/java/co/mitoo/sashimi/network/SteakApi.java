package co.mitoo.sashimi.network;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.ConfirmInfo;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.models.jsonPojo.recieve.JsonDeviceInfo;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationPreferenceRecieved;
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
import retrofit.http.PATCH;
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
    Observable<Team[]> getTeamByCompetition(@Path("id") int id);

    @GET("/leagues/v1/competition_seasons/{id}/fixtures")
    Observable<Fixture[]> getFixtureFromCompetitionID(@Query("filter") String filter, @Path("id") int id);

    @GET("/leagues/v1/users/{id}/competition_seasons")
    Observable<Competition[]> getCompetitionSeasonFromUserID(@Query("filter") String filter ,@Query("league_info") String league_info ,@Path("id") int id);

    @GET("/users/v1/confirmations/{token}")
    Observable<ConfirmInfo> getConfirmationInfo(@Path("token") String token);

    @POST("/users/v1/confirmations/{token}/confirm")
    Observable<UserInfoRecieve> createUserFromConfirmation(@Path("token") String token, @Body JsonSignUpSend jsonObject);

    @POST("/users/v1/users/{user_id}/mobile_devices")
    Observable<Response> createDeviceAssociation(@Path("user_id") int user_id, @Body JsonDeviceInfo jsonObject);

    @DELETE("/users/v1/mobile_devices/{token}")
    Observable<Response> deleteDeviceAssociation(@Path("token") String token);

    @GET("/leagues/v1/fixtures/{id}")
    Observable<Fixture> getFixtureFromFixtureID(@Path("id") int id);

    @PATCH("/notifications/v1/users/{user_id}/preferences/competition/{competition_id}")
    Observable<NotificationPreferenceRecieved> updateNotificationPreference(@Path("user_id") int user_id,
                                                                     @Path("competition_id") int competition_id,
                                                                     @Body NotificationPreferenceRecieved jsonObject);

    @GET("/notifications/v1/users/{user_id}/preferences/competition/{competition_id}")
    Observable<NotificationPreferenceRecieved> getNotificationPreference(@Path("user_id") int user_id,
                                                                  @Path("competition_id") int competition_id);
}
