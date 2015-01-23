package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 15-01-22.
 */
public class LeagueModelEnquireRequestEvent {
    
    private int userID;
    private MitooEnum.crud requestType;

    public LeagueModelEnquireRequestEvent(int userID, MitooEnum.crud requestType) {
        this.userID = userID;
        this.requestType = requestType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public MitooEnum.crud getRequestType() {
        return requestType;
    }

    public void setRequestType(MitooEnum.crud requestType) {
        this.requestType = requestType;
    }
}
