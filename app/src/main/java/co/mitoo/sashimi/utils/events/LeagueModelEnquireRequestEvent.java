package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.models.LeagueModel;

/**
 * Created by david on 15-01-22.
 */
public class LeagueModelEnquireRequestEvent {
    
    private int userID;
    private LeagueModel leagueModel;

    public LeagueModelEnquireRequestEvent(int userID){

        this.userID = userID;
    }

    public LeagueModelEnquireRequestEvent(int userID, LeagueModel leagueModel) {
        this.userID = userID;
        this.leagueModel = leagueModel;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LeagueModel getLeagueModel() {
        return leagueModel;
    }
}
