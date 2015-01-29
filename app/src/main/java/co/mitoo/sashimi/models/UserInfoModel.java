package co.mitoo.sashimi.models;
import android.app.Activity;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.UserInfoModelRequestEvent;
import co.mitoo.sashimi.utils.events.UserInfoModelResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;


/**
 * Created by david on 15-01-21.
 */
public class UserInfoModel extends MitooModel{

    public UserInfoModel(MitooActivity activity) {
        super(activity);
    }

    private UserInfoRecieve userInfoRecieve;

    public UserInfoRecieve getUserInfoRecieve() {
        return userInfoRecieve;
    }

    public void setUserInfoRecieve(UserInfoRecieve userInfoRecieve) {
        this.userInfoRecieve = userInfoRecieve;
    }

    @Subscribe
    public void onUserInfoRequest(UserInfoModelRequestEvent event) {

        if(getUserInfoRecieve() == null){
            handleObservable(getSteakApiService().getUser(event.getUserID()) , UserInfoRecieve.class);
        }
        else{
            postUserInfoRecieveResponse();
        }
    }

    @Subscribe
    public void onApiFailEvent(RetrofitError event) {
        
        BusProvider.post(new MitooActivitiesErrorEvent(event));
    }

    private void postUserInfoRecieveResponse() {

        BusProvider.post(new UserInfoModelResponseEvent(getUserInfoRecieve()));

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve)  {
        
        setUserInfoRecieve((UserInfoRecieve)objectRecieve);
        postUserInfoRecieveResponse();

    }



}
