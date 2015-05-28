package co.mitoo.sashimi.models.jsonPojo.recieve;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import co.mitoo.sashimi.models.jsonPojo.recieve.notification.*;

/**
 * Created by david on 15-04-08.
 */
@JsonRootName(value = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationPreferenceRecieved implements Serializable ,Cloneable {


    private GroupSettings group_settings;

    public GroupSettings getGroup_settings() {
        return group_settings;
    }

    @JsonProperty("group_settings")
    public void setGroup_settings(GroupSettings group_settings) {
        this.group_settings = group_settings;
    }

    public Object clone() {
        //Deep copy
        NotificationPreferenceRecieved preferenceRecieved = new NotificationPreferenceRecieved();

        GroupSettings group_settings = new GroupSettings();
        GroupTeamResults GroupTeamResults = new GroupTeamResults();
        GroupTeamGames GroupTeamGames = new GroupTeamGames();
        GroupLeagueResults GroupLeagueResults = new GroupLeagueResults();
        GroupLeagueAlerts GroupLeagueAlerts = new GroupLeagueAlerts();

        GroupTeamResults.setEmail(getGroup_settings().getGroupTeamResults().isEmail());
        GroupTeamGames.setEmail(getGroup_settings().getGroupTeamGames().isEmail());
        GroupLeagueResults.setEmail(getGroup_settings().getGroupLeagueResults().isEmail());
        GroupLeagueAlerts.setEmail(getGroup_settings().getGroupLeagueAlerts().isEmail());

        GroupTeamResults.setPush(getGroup_settings().getGroupTeamResults().isPush());
        GroupTeamGames.setPush(getGroup_settings().getGroupTeamGames().isPush());
        GroupLeagueResults.setPush(getGroup_settings().getGroupLeagueResults().isPush());
        GroupLeagueAlerts.setPush(getGroup_settings().getGroupLeagueAlerts().isPush());

        group_settings.setGroupLeagueResults(GroupLeagueResults);
        group_settings.setGroupTeamResults(GroupTeamResults);
        group_settings.setGroupTeamGames(GroupTeamGames);
        group_settings.setGroupLeagueAlerts(GroupLeagueAlerts);
        preferenceRecieved.setGroup_settings(group_settings);

        return preferenceRecieved;
    }

}
