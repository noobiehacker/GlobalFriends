package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-10.
 */
public class LeagueRequestFromIDEvent {

    private int leagueID;

    public LeagueRequestFromIDEvent(int leagueID) {
        this.leagueID = leagueID;
    }

    public int getLeagueID() {
        return leagueID;
    }
}
