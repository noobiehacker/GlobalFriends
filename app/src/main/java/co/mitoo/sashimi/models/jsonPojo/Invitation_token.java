package co.mitoo.sashimi.models.jsonPojo;

/**
 * Created by david on 15-03-17.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "")
public class Invitation_token {

    public String invitation_token;

}
