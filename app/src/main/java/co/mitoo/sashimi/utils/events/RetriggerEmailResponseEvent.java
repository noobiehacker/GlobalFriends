package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-05.
 */
public class RetriggerEmailResponseEvent {

    private boolean responseSent;

    public boolean isResponseSent() {
        return responseSent;
    }

    public RetriggerEmailResponseEvent(boolean responseSent) {
        this.responseSent = responseSent;
    }
}
