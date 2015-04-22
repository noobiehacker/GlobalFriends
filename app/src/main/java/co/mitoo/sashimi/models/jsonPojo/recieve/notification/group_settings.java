package co.mitoo.sashimi.models.jsonPojo.recieve.notification;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.io.Serializable;

/**
 * Created by david on 15-04-08.
 */

@JsonRootName("group_settings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class group_settings implements Serializable {

    private group_team_games group_team_games;
    private group_team_results group_team_results;
    private group_league_results group_league_results;

    public group_team_games getGroup_team_games() {
        return group_team_games;
    }

    public void setGroup_team_games(group_team_games group_team_games) {
        this.group_team_games = group_team_games;
    }

    public group_team_results getGroup_team_results() {
        return group_team_results;
    }

    public void setGroup_team_results(group_team_results group_team_results) {
        this.group_team_results = group_team_results;
    }

    public group_league_results getGroup_league_results() {
        return group_league_results;
    }

    public void setGroup_league_results(group_league_results group_league_results) {
        this.group_league_results = group_league_results;
    }

}
