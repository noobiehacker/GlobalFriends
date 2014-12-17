package co.mitoo.sashimi.models.jsonPojo.send;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 14-11-13.
 */
@JsonRootName(value = "")
public class UserSend
{

    public UserSend(String email, String password){
        this.email = email;
        this.password = password;
    }
    public String email ;
    public String password ;
}
