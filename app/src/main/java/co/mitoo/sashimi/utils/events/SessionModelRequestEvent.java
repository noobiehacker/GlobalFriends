package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.send.JsonLoginSend;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 14-11-26.
 */
public class SessionModelRequestEvent extends TokenRequestEvent {

    private JsonLoginSend loginUser;
    private JsonSignUpSend singUpUser;
    private MitooEnum.SessionRequestType requestType;

    public SessionModelRequestEvent(MitooEnum.SessionRequestType requestType ,JsonSignUpSend singUpUser) {
        this.singUpUser = singUpUser;
        this.requestType = requestType;
    }

    public SessionModelRequestEvent(MitooEnum.SessionRequestType requestType, JsonLoginSend loginUser) {
        this.requestType = requestType;
        this.loginUser = loginUser;
    }

    public JsonLoginSend getLoginUser() {
        return loginUser;
    }

    public JsonSignUpSend getSingUpUser() {
        return singUpUser;
    }

    public MitooEnum.SessionRequestType getRequestType() {
        return requestType;
    }

}
