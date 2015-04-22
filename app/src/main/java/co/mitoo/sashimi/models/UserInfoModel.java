package co.mitoo.sashimi.models;
import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.UserInfoModelResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;
import rx.Subscriber;

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

    public void onUserInfoRequest(int userID, boolean refresh) {

        if(getUserInfoRecieve() == null  || refresh){
            handleObservable(getSteakApiService().getUser(userID) , UserInfoRecieve.class);
        }
        else{
            postUserInfoRecieveResponse();
        }
    }

    public void requestToConfirmUser(String token, JsonSignUpSend jsonObject) {

        handleObservable(getSteakApiService().createUserFromConfirmation(token, jsonObject)
                , UserInfoRecieve.class);

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
        updateSessionModelAuthToken();

    }

    @Override
    public void resetFields(){
        setUserInfoRecieve(null);
    }

    private void updateSessionModelAuthToken(){
        SessionModel sessionModel = getActivity().getModelManager().getSessionModel();
        SessionRecieve session = sessionModel.getSession();
        sessionModel.updateSession(getUserInfoRecieve());

    }
}
