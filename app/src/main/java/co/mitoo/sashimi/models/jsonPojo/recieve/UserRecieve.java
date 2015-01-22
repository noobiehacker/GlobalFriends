package co.mitoo.sashimi.models.jsonPojo.recieve;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 15-01-21.
 */
@JsonRootName(value = "")
public class UserRecieve {
    
    public int id;
    public String email;
    public String name;
    public String phone;
    public String picture;
    public String picture_large;
    public String picture_medium;
    public String picture_small;
    public String picture_thumb;

}
