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

    public void setLoginUser(JsonLoginSend loginUser) {
        this.loginUser = loginUser;
    }

    public JsonSignUpSend getSingUpUser() {
        return singUpUser;
    }

    public void setSingUpUser(JsonSignUpSend singUpUser) {
        this.singUpUser = singUpUser;
    }

    public MitooEnum.SessionRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(MitooEnum.SessionRequestType requestType) {
        this.requestType = requestType;
    }
}
