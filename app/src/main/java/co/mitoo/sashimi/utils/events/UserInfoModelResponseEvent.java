package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;

/**
 * Created by david on 15-01-22.
 */
public class UserInfoModelResponseEvent {
    
    private UserInfoRecieve userInfoRecieve;

    public UserInfoRecieve getUserInfoRecieve() {
        return userInfoRecieve;
    }

    public void setUserInfoRecieve(UserInfoRecieve userInfoRecieve) {
        this.userInfoRecieve = userInfoRecieve;
    }

    public UserInfoModelResponseEvent(UserInfoRecieve userInfoRecieve) {
        this.userInfoRecieve = userInfoRecieve;
    }
}
