package co.mitoo.sashimi.models.jsonPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

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
    private String time;
    private String time_zone;
    private String sport;
}
