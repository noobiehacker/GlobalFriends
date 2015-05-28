package co.mitoo.sashimi.models.jsonPojo.recieve.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

/**
 * Created by david on 15-05-20.
 */
@JsonRootName("group_league_alerts")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupLeagueAlerts implements Serializable {

    private boolean email;
    private boolean push;

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }
}
