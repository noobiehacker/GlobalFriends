package co.mitoo.sashimi.network.mockNetwork;
import java.util.Collections;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.ConfirmInfo;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.models.jsonPojo.UserCheck;
import co.mitoo.sashimi.models.jsonPojo.recieve.JsonDeviceInfo;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationPreferenceRecieved;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.JsonLeagueEnquireSend;
import co.mitoo.sashimi.models.jsonPojo.send.JsonResetPasswordSend;
import co.mitoo.sashimi.models.jsonPojo.send.JsonLoginSend;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
import co.mitoo.sashimi.network.SteakApi;
import co.mitoo.sashimi.utils.BusProvider;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by david on 14-12-04.
 */
public class MockSteakApiService implements SteakApi {

    private int statusCode;

    public MockSteakApiService (int statusCode){
        this.statusCode = statusCode;
    }

    @Override
    public Observable<SessionRecieve> createRegistration(@Query("save_type") String param1,@Body JsonSignUpSend user) {
        return createMockRespoonse(new SessionRecieve());
    }

    @Override
    @POST("/sessions")
    public Observable<SessionRecieve> createSession(@Body JsonLoginSend userSend) {
        return createMockRespoonse(new SessionRecieve());
    }

    @Override
    public Observable<Response> resetPassword(@Body JsonResetPasswordSend jsonObject) {
        return createMockRespoonse(createResponse());
    }

    @Override
    public Observable<SessionRecieve> deleteSession() {
        return createMockRespoonse(new SessionRecieve());
    }

    @Override
    public Observable<Response> createLeagueEnquiries(@Path("id") int id, @Body JsonLeagueEnquireSend jsonObject) {
        return null;
    }

    @Override
    public Observable<League[]> getLeagueEnquiries(@Query("filter") String filter, @Query("user_id") int user_id) {
        return null;
    }

    @Override
    public Observable<UserInfoRecieve> getUser(@Path("id") int id) {
        return null;
    }

    @Override
    public Observable<Response> createDeviceAssociation(@Path("user_id") int user_id, @Body JsonDeviceInfo jsonObject) {
        return null;
    }

    @Override
    public Observable<Response> deleteDeviceAssociation(@Path("token") String token) {
        return null;
    }

    @Override
    public Observable<Competition[]> getCompetitionSeasonFromUserID(@Query("filter") String filter, @Query("league_info") String league_info, @Path("id") int id) {
        return null;
    }

    @Override
    public Observable<Team[]> getTeamByCompetition(@Path("id") int id) {
        return null;
    }

    @Override
    public Observable<Fixture[]> getFixtureFromCompetitionID(@Query("filter") String filter, @Path("id") int id) {
        return null;
    }

    @Override
    public Observable<ConfirmInfo> getConfirmationInfo(@Path("token") String token) {
        return null;
    }

    @Override
    public Observable<UserInfoRecieve> createUserFromConfirmation(@Path("token") String token, @Body JsonSignUpSend jsonObject) {
        return null;
    }

    @Override
    public Observable<Fixture> getFixtureFromFixtureID(@Path("id") int id) {
        return null;
    }

    @Override
    public Observable<NotificationPreferenceRecieved> updateNotificationPreference(@Path("user_id") int user_id, @Path("competition_id") int competition_id, @Body NotificationPreferenceRecieved jsonObject) {
        return null;
    }

    @Override
    public Observable<NotificationPreferenceRecieved> getNotificationPreference(@Path("user_id") int user_id, @Path("competition_id") int competition_id) {
        return null;
    }

    @Override
    public Observable<UserCheck> checkUser(@Path("identifier") String identifier) {
        return null;
    }

    @Override
    public Observable<Response> retriggerConfirmationLink(@Path("id") String user_id) {
        return null;
    }

    private <T> Observable<T> createMockRespoonse(T item){

            Observable<T> result = null;
            if (statusCode != 200) {
                BusProvider.post(createError());
            } else {
                result = Observable.just(item);
            }
            return result;

        }

    private RetrofitError createError(){
        Response response = createResponse();
        return RetrofitError.httpError("", response, null,null);
    }

    private Response createResponse(){
        return new Response("", statusCode, "", Collections.<Header>emptyList(),null );
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


}
