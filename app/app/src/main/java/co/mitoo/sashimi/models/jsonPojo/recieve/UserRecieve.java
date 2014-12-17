package co.mitoo.sashimi.models.jsonPojo.recieve;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 14-12-02.
 */
@JsonRootName(value = "")
public class UserRecieve {

    public int id;
    public String email ;
    public String auth_token ;

}
