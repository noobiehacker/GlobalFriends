package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;

/**
 * Created by david on 15-05-08.
 */
public class UserInfoResponseEvent {

    private UserInfoRecieve userInfoRecieve;

    public UserInfoRecieve getUserInfoRecieve() {
        return userInfoRecieve;
    }

    public UserInfoResponseEvent(UserInfoRecieve userInfoRecieve) {
        this.userInfoRecieve = userInfoRecieve;
    }
}
