package co.mitoo.sashimi.utils.events;

/**
 * Created by david on 15-01-13.
 */
public class ToolBarDisplayEvent {
    
    private boolean show;

    public ToolBarDisplayEvent(boolean show) {
        this.show = show;
    }

    public boolean isShow() {

        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
