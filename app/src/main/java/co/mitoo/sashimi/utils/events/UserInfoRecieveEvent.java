package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;

/**
 * Created by david on 15-01-21.
 */
public class UserInfoRecieveEvent {
    
    private UserInfoRecieve info;

    public UserInfoRecieveEvent(UserInfoRecieve info) {
        this.info = info;
    }

    public UserInfoRecieve getInfo() {
        return info;
    }

    public void setInfo(UserInfoRecieve info) {
        this.info = info;
    }
}
