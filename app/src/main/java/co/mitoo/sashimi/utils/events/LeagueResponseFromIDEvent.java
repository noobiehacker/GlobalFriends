package co.mitoo.sashimi.utils.events;
import co.mitoo.sashimi.models.LeagueModel;

/**
 * Created by david on 15-05-10.
 */
public class LeagueResponseFromIDEvent {

    private Throwable e;

    private LeagueModel leagueModel;

    public LeagueResponseFromIDEvent(Throwable e) {
        this.e = e;
    }

    public Throwable getE() {
        return e;
    }

    public LeagueResponseFromIDEvent(LeagueModel leagueModel) {
        this.leagueModel = leagueModel;
    }

    public LeagueModel getLeagueModel() {
        return leagueModel;
    }
}
