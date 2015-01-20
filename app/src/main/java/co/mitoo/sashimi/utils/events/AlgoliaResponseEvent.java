package co.mitoo.sashimi.utils.events;

import org.json.JSONObject;

/**
 * Created by david on 15-01-15.
 */
public class AlgoliaResponseEvent {
    
    private JSONObject result;

    public AlgoliaResponseEvent(JSONObject result) {
        this.result = result;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }
}
