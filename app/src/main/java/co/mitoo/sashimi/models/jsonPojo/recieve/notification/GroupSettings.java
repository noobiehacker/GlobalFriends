package co.mitoo.sashimi.models.jsonPojo.recieve.notification;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.io.Serializable;

/**
 * Created by david on 15-04-08.
 */

@JsonRootName("group_settings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupSettings implements Serializable {

    private GroupTeamGames GroupTeamGames;
    private GroupTeamResults GroupTeamResults;
    private GroupLeagueResults GroupLeagueResults;
    private GroupLeagueAlerts GroupLeagueAlerts;

    public GroupTeamGames getGroupTeamGames() {
        return GroupTeamGames;
    }

    @JsonProperty("group_team_games")
    public void setGroupTeamGames(GroupTeamGames groupTeamGames) {
        this.GroupTeamGames = groupTeamGames;
    }

    public GroupTeamResults getGroupTeamResults() {
        return GroupTeamResults;
    }

    @JsonProperty("group_team_results")
    public void setGroupTeamResults(GroupTeamResults groupTeamResults) {
        this.GroupTeamResults = groupTeamResults;
    }

    public GroupLeagueResults getGroupLeagueResults() {
        return GroupLeagueResults;
    }

    @JsonProperty("group_league_results")
    public void setGroupLeagueResults(GroupLeagueResults groupLeagueResults) {
        this.GroupLeagueResults = groupLeagueResults;
    }

    public GroupLeagueAlerts getGroupLeagueAlerts() {
        return GroupLeagueAlerts;
    }

    @JsonProperty("group_league_alerts")
    public void setGroupLeagueAlerts(GroupLeagueAlerts groupLeagueAlerts) {
        this.GroupLeagueAlerts = groupLeagueAlerts;
    }
}