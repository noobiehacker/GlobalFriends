package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 15-01-22.
 */
public class LeagueModelEnquireRequestEvent {
    
    private int userID;
    private MitooEnum.APIRequest apiRequestType = MitooEnum.APIRequest.REQUEST;

    public LeagueModelEnquireRequestEvent(int userID){

        this.userID = userID;
    }

    public LeagueModelEnquireRequestEvent(int userID, MitooEnum.APIRequest apiRequestType) {
        this.userID = userID;
        this.apiRequestType = apiRequestType;
    }

    public MitooEnum.APIRequest getApiRequestType() {
        return apiRequestType;
    }

    public void setApiRequestType(MitooEnum.APIRequest apiRequestType) {
        this.apiRequestType = apiRequestType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}
