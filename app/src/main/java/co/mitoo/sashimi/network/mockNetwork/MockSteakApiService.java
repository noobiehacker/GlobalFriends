package co.mitoo.sashimi.network.mockNetwork;

import java.util.Collections;

import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.Team;
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
    public Observable<Competition[]> getCompetitionSeasonFromUserID(@Query("filter") String filter, @Path("id") int id) {
        return null;
    }

    private <T> Observable<T> createMockRespoonse(T item){

        Observable<T> result = null;
        if(statusCode!=200){
            BusProvider.post(createError());
        }else{
            result= Observable.just(item);
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

    @Override
    public Observable<Team[]> getTeamByCompetition(@Path("id") int id) {
        return null;
    }

    @Override
    public Observable<Fixture[]> getFixtureFromTeamID(@Query("filter") String filter, @Path("id") int id) {
        return null;
    }
}
