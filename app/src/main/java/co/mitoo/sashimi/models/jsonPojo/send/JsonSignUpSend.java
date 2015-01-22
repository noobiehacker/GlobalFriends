package co.mitoo.sashimi.models.jsonPojo.send;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 14-11-12.
 */
@JsonRootName(value = "")
public class JsonSignUpSend {

    public JsonSignUpSend(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public String email;
    public String password;
    public String name;
    public String phone;
}
