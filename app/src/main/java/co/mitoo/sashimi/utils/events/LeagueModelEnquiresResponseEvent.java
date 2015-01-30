package co.mitoo.sashimi.utils.events;
import java.util.List;

import co.mitoo.sashimi.models.jsonPojo.League;
import retrofit.client.Response;

/**
 * Created by david on 15-01-22.
 */
public class LeagueModelEnquiresResponseEvent {
    
    private Response response;
    private List<League> enquiredLeagues;
    private League selectedLeague;

    public LeagueModelEnquiresResponseEvent(Response response) {
        this.response = response;
    }

    public LeagueModelEnquiresResponseEvent(List<League> enquiredLeagues) {
        this.enquiredLeagues = enquiredLeagues;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<League> getEnquiredLeagues() {
        return enquiredLeagues;
    }

    public void setEnquiredLeagues(List<League> enquiredLeagues) {
        this.enquiredLeagues = enquiredLeagues;
    }

    public League getSelectedLeague() {
        return selectedLeague;
    }

    public void setSelectedLeague(League selectedLeague) {
        this.selectedLeague = selectedLeague;
    }
}
