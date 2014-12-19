package co.mitoo.sashimi.utils.events;
import retrofit.client.Response;

/**
 * Created by david on 14-12-16.
 */
public class ResetPasswordResponseEvent {
    
    private Response response;

    public ResetPasswordResponseEvent(Response response){
        this.response=response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
