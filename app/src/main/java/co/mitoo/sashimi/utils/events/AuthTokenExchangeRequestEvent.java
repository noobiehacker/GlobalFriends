package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-01-09.
 */
public class AuthTokenExchangeRequestEvent extends TokenRequestEvent{
    
    public AuthTokenExchangeRequestEvent(String faceBookToken) {
        this.faceBookToken = faceBookToken;
    }

    private String faceBookToken;

    public String getFaceBookToken() {
        return faceBookToken;
    }

    public void setFaceBookToken(String faceBookToken) {
        this.faceBookToken = faceBookToken;
    }
}
