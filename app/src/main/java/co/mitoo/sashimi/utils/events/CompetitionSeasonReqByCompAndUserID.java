package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-05-06.
 */
public class CompetitionSeasonReqByCompAndUserID {

    private int competitionSeasonID;
    private int userID;

    public int getCompetitionSeasonID() {
        return competitionSeasonID;
    }

    public int getUserID() {
        return userID;
    }

    public CompetitionSeasonReqByCompAndUserID(int competitionSeasonID, int userID) {
        this.competitionSeasonID = competitionSeasonID;
        this.userID = userID;
    }
}
