package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.UserRecieve;

/**
 * Created by david on 15-01-21.
 */
public class UserInfoRecieveEvent {
    
    private UserRecieve info;

    public UserInfoRecieveEvent(UserRecieve info) {
        this.info = info;
    }

    public UserRecieve getInfo() {
        return info;
    }

    public void setInfo(UserRecieve info) {
        this.info = info;
    }
}
