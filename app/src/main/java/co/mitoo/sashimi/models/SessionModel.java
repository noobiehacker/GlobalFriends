package co.mitoo.sashimi.models;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Invitation_token;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.JsonResetPasswordSend;
import co.mitoo.sashimi.network.DataPersistanceService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.IsPersistable;
import co.mitoo.sashimi.utils.StaticString;
import co.mitoo.sashimi.utils.events.SessionPersistanceResponseEvent;
import co.mitoo.sashimi.utils.events.SessionModelRequestEvent;
import co.mitoo.sashimi.utils.events.SessionModelResponseEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordRequestEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by david on 14-11-11.
 */

public class SessionModel extends MitooModel implements IsPersistable {

    public SessionModel(MitooActivity activity) {
        super(activity);
    }

    private SessionRecieve session;

    private Invitation_token invitation_token;

    public SessionRecieve getSession() {
        return session;
    }

    public void setSession(SessionRecieve user) {
        this.session = user;
        saveData();
    }

    public void requestSession(SessionModelRequestEvent event) {

        handleRequestEvent(event);
    }

    public void requestPasswordRequest(ResetPasswordRequestEvent event) {

        handleObservable(getSteakApiService().resetPassword(new JsonResetPasswordSend(event.getEmail())),
                Response.class);
    }

    private void postUserRecieveResponse() {

        BusProvider.post(new SessionModelResponseEvent(getSession()));

    }

    private void postResetPasswordResponse(Response response) {

        BusProvider.post(new ResetPasswordResponseEvent(response));

    }

    private void handleRequestEvent(SessionModelRequestEvent event) {

        switch (event.getRequestType()){
            
            case SIGNUP:
                handleObservable(getSteakApiService().createRegistration(
                                StaticString.apiConstantRegister, event.getSingUpUser()),
                        SessionRecieve.class);
                break;
            case LOGIN:
                handleObservable(getSteakApiService().createSession(event.getLoginUser()),
                        SessionRecieve.class);
                break;
        }
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof SessionRecieve) {
            updateSession((SessionRecieve)objectRecieve);
            postUserRecieveResponse();

        } else if (objectRecieve instanceof Response) {
            postResetPasswordResponse((Response) objectRecieve);
        }
    }

    @Override
    public void readData() {

        DataPersistanceService service = getPersistanceService();
        setSession(service.readFromPreference(getPreferenceKey() , SessionRecieve.class));

    }

    @Override
    public void saveData() {

        getPersistanceService().saveToPreference(getPreferenceKey() , getSession());

    }

    @Override
    public void deleteData() {

        getPersistanceService().deleteFromPreference(getPreferenceKey());
        setSession(null);
    }

    @Override
    public String getPreferenceKey() {
        return getActivity().getString(R.string.shared_preference_session_key);
    }

    @Subscribe
    public void sessionPersistanceResponse(SessionPersistanceResponseEvent event) {

        setSession((SessionRecieve)event.getPersistedObject());
        postUserRecieveResponse();

    }
    
    private void updateToken(){
        if(getSession()!=null)
            getActivity().updateAuthToken(this.session);
    }
    
    public boolean  userIsLoggedIn(){
        
        return getSession()!=null;
        
    }

    public Invitation_token getInvitation_token() {
        return invitation_token;
    }

    public void setInvitation_token(Invitation_token invitation_token) {
        this.invitation_token = invitation_token;
    }

    public void updateSession(SessionRecieve session){
        setSession(session);
        updateToken();
    }
}




