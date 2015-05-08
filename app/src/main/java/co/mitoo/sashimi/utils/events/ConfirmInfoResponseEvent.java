package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.ConfirmInfo;

/**
 * Created by david on 15-03-20.
 */
public class ConfirmInfoResponseEvent {

    private ConfirmInfo confirmInfo;

    public ConfirmInfoResponseEvent(ConfirmInfo confirmInfo) {
        this.confirmInfo = confirmInfo;
    }

    public ConfirmInfo getConfirmInfo() {
        return confirmInfo;
    }
}
