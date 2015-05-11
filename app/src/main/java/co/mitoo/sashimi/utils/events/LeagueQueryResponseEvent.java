package co.mitoo.sashimi.utils.events;
import java.util.List;

import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.League;

/**
 * Created by david on 15-01-15.
 */
public class LeagueQueryResponseEvent {
    
    private List<LeagueModel> leagueModels;

    public List<LeagueModel> getLeagueModels() {
        return leagueModels;
    }

    public LeagueQueryResponseEvent(List<LeagueModel> leagueModels) {
        this.leagueModels = leagueModels;
    }
}
