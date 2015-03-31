package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.Invitation_token;

/**
 * Created by david on 15-03-17.
 */
public class BranchIOResponseEvent {

    private Invitation_token token;

    public BranchIOResponseEvent(Invitation_token token) {
        this.token = token;
    }

    public Invitation_token getToken() {
        return token;
    }

    public void setToken(Invitation_token token) {
        this.token = token;
    }
}
