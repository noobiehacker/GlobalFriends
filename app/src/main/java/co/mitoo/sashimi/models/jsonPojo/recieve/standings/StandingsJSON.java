package co.mitoo.sashimi.models.jsonPojo.recieve.standings;

import android.provider.ContactsContract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by david on 15-04-20.
 */
@JsonRootName(value = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandingsJSON implements Serializable {

    @JsonProperty(value = "data")
    private Map<String, Map<String, String>> data;
    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    @JsonProperty(value = "series")
    private int[] series;

    @JsonProperty(value = "cols")
    private String[] cols;

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public String[] getCols() {
        return cols;
    }

    public int[] getSeries() {
        return series;
    }

    public void setCols(String[] cols) {
        this.cols = cols;
    }
}
