package co.mitoo.sashimi.utils.events;
import java.util.List;

import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.MitooEnum;
import retrofit.client.Response;

/**
 * Created by david on 15-01-22.
 */
public class LeagueModelEnquiresResponseEvent {
    
    private List<League> enquiredLeagues;
    private League selectedLeague;

    public LeagueModelEnquiresResponseEvent(List<League> enquiredLeagues) {
        this.enquiredLeagues = enquiredLeagues;
    }

    public List<League> getEnquiredLeagues() {
        return enquiredLeagues;
    }

}
