package co.mitoo.sashimi.network.mockNetwork;

import java.util.Collections;

import co.mitoo.sashimi.models.jsonPojo.send.EmailSend;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.UserSend;
import co.mitoo.sashimi.network.SteakApi;
import co.mitoo.sashimi.utils.BusProvider;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
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
    public Observable<UserRecieve> createRegistration(@Query("save_type") String param1,@Body UserSend user) {
        return createMockRespoonse(new UserRecieve());
    }

    @Override
    @POST("/sessions")
    public Observable<UserRecieve> createSession(@Body UserSend userSend) {
        return createMockRespoonse(new UserRecieve());
    }

    @Override
    public Observable<Response> resetPassword(@Body EmailSend jsonObject) {
        return createMockRespoonse(createResponse());
}

    @Override
    public Observable<UserRecieve> deleteSession() {
        return createMockRespoonse(new UserRecieve());
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

}
