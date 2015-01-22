package co.mitoo.sashimi.models.jsonPojo.send;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 14-12-05.
 */
@JsonRootName(value = "")
public class JsonResetPasswordSend {

    public JsonResetPasswordSend(String email){
        this.email = email;
    }
    public String email;
}
