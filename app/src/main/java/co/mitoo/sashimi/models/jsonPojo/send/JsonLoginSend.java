package co.mitoo.sashimi.models.jsonPojo.send;

/**
 * Created by david on 14-11-13.
 */

public class JsonLoginSend
{

    public JsonLoginSend(String identifier, String password){
        this.identifier = identifier;
        this.password = password;
    }
    public String identifier;
    public String password ;
}
