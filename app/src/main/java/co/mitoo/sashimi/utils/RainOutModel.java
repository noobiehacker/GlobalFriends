package co.mitoo.sashimi.utils;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.rain_out_message;

/**
 * Created by david on 15-05-19.
 */
public class RainOutModel {

    private rain_out_message rainOutMessage;

    public rain_out_message getRainOutMessage() {
        return rainOutMessage;
    }

    public void setRainOutMessage(rain_out_message rainOutMessage) {
        this.rainOutMessage = rainOutMessage;
    }

    public RainOutModel(rain_out_message rainOutMessage) {
        this.rainOutMessage = rainOutMessage;
    }
}
