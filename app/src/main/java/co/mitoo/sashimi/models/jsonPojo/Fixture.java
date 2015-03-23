package co.mitoo.sashimi.models.jsonPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by david on 15-03-09.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fixture implements Serializable {

    private int id;
    private int status;
    private int competition_season_id;
    private int home_team_id;
    private int away_team_id;
    private result result;
    private location location;
    private boolean time_tbc;
    private String local_time;
    private String time_zone;
    private String sport;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCompetition_season_id() {
        return competition_season_id;
    }

    public void setCompetition_season_id(int competition_season_id) {
        this.competition_season_id = competition_season_id;
    }

    public int getHome_team_id() {
        return home_team_id;
    }

    public void setHome_team_id(int home_team_id) {
        this.home_team_id = home_team_id;
    }

    public int getAway_team_id() {
        return away_team_id;
    }

    public void setAway_team_id(int away_team_id) {
        this.away_team_id = away_team_id;
    }

    public result getResult() {
        return result;
    }

    public void setResult(result result) {
        this.result = result;
    }

    public location getLocation() {
        return location;
    }

    public void setLocation(location location) {
        this.location = location;
    }

    public boolean isTime_tbc() {
        return time_tbc;
    }

    public void setTime_tbc(boolean time_tbc) {
        this.time_tbc = time_tbc;
    }

    public String getLocal_time() {
        return local_time;
    }

    public void setLocal_time(String local_time) {
        this.local_time = local_time;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

}
