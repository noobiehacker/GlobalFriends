package co.mitoo.sashimi.utils;

/**
 * Created by david on 15-05-19.
 */
public class RainOut {

    private String rainOutMessage;
    private String firstColor;
    private String secondColor;

    public String getRainOutMessage() {
        return rainOutMessage;
    }

    public String getFirstColor() {
        return firstColor;
    }

    public String getSecondColor() {
        return secondColor;
    }

    public RainOut(String rainOutMessage, String firstColor, String secondColor) {
        this.rainOutMessage = rainOutMessage;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }
}
