package co.mitoo.sashimi.models.jsonPojo;

/**
 * Created by david on 15-03-17.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Invitation_token implements Serializable {

    public String invitation_token;

    public String getToken() {
        return invitation_token;
    }

    public void setToken(String token) {
        this.invitation_token = token;
    }
}

