package co.mitoo.sashimi.models.jsonPojo.recieve;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 14-12-02.
 */
@JsonRootName(value = "")
public class SessionRecieve {

    public int id;
    public String email ;
    public String name;
    public String auth_token ;
    public String phone ;

    public SessionRecieve(UserInfoRecieve userInfoRecieve){

        this.id = userInfoRecieve.id;
        this.email = userInfoRecieve.email;
        this.name = userInfoRecieve.name;
        this.auth_token = userInfoRecieve.auth_token;
        this.phone = userInfoRecieve.phone;

    }

    public SessionRecieve(){

    }

}
