package co.mitoo.sashimi.models.jsonPojo.recieve.standings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by david on 15-04-20.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class data implements Serializable {

    private String won;
    private String lost;
    private String drawn;
    private String win_pct;
    private String score_diff;

}
