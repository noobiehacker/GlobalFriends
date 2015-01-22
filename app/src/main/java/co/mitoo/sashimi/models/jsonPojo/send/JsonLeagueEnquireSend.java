package co.mitoo.sashimi.models.jsonPojo.send;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 15-01-21.
 */
@JsonRootName(value = "")
public class JsonLeagueEnquireSend {
    
    public String user_id;
    public String sport;

    public JsonLeagueEnquireSend(String user_id, String sport) {
        this.user_id = user_id;
        this.sport = sport;
    }
}
