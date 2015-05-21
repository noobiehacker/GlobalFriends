package co.mitoo.sashimi.models.jsonPojo.recieve;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import co.mitoo.sashimi.models.jsonPojo.recieve.notification.*;

/**
 * Created by david on 15-04-08.
 */
@JsonRootName(value = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationPreferenceRecieved implements Serializable ,Cloneable {

    private group_settings group_settings;

    public group_settings getGroup_settings() {
        return group_settings;
    }

    public void setGroup_settings(group_settings group_settings) {
        this.group_settings = group_settings;
    }

    public Object clone() {
        //Deep copy
        NotificationPreferenceRecieved preferenceRecieved = new NotificationPreferenceRecieved();

        group_settings group_settings = new group_settings();
        group_team_results group_team_results = new group_team_results();
        group_team_games group_team_games = new group_team_games();
        group_league_results group_league_results = new group_league_results();
        group_league_alerts group_league_alerts = new group_league_alerts();

        group_team_results.setEmail(getGroup_settings().getGroup_team_results().isEmail());
        group_team_games.setEmail(getGroup_settings().getGroup_team_games().isEmail());
        group_league_results.setEmail(getGroup_settings().getGroup_league_results().isEmail());
        group_league_alerts.setEmail(getGroup_settings().getGroup_league_alerts().isEmail());

        group_team_results.setPush(getGroup_settings().getGroup_team_results().isPush());
        group_team_games.setPush(getGroup_settings().getGroup_team_games().isPush());
        group_league_results.setPush(getGroup_settings().getGroup_league_results().isPush());
        group_league_alerts.setPush(getGroup_settings().getGroup_league_alerts().isPush());

        group_settings.setGroup_league_results(group_league_results);
        group_settings.setGroup_team_results(group_team_results);
        group_settings.setGroup_team_games(group_team_games);
        group_settings.setGroup_league_alerts(group_league_alerts);
        preferenceRecieved.setGroup_settings(group_settings);

        return preferenceRecieved;
    }

}
