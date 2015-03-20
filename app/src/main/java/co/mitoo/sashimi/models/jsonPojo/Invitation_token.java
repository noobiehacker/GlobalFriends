package co.mitoo.sashimi.models.jsonPojo;

/**
 * Created by david on 15-03-17.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Invitation_token implements Serializable {

    public String invitation_token;

    public String getInvitation_token() {
        return invitation_token;
    }

    public void setInvitation_token(String invitation_token) {
        this.invitation_token = invitation_token;
    }
}

