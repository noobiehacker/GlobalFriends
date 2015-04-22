package co.mitoo.sashimi.models.jsonPojo.recieve.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by david on 15-04-08.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class group_league_results  implements Serializable {

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
