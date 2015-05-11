package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.ConfirmInfo;

/**
 * Created by david on 15-03-20.
 */
public class ConfirmInfoResponseEvent {

    private ConfirmInfo confirmInfo;
    private String token;

    public ConfirmInfo getConfirmInfo() {
        return confirmInfo;
    }

    public String getToken() {
        return token;
    }

    public ConfirmInfoResponseEvent(ConfirmInfo confirmInfo, String token) {
        this.confirmInfo = confirmInfo;
        this.token = token;
    }
}
