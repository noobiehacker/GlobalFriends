package co.mitoo.sashimi.models.jsonPojo.Passwords;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 14-12-05.
 */
@JsonRootName(value = "")
public class PasswordReset {

    public PasswordReset(String email){
        this.email = email;
    }
    public String email;
}
