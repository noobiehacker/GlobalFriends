package co.mitoo.sashimi.utils.events;

import java.util.EventObject;

/**
 * Created by david on 14-11-13.
 */
public class FragmentChangeEvent extends EventObject{


    private int fragmentId;
    private boolean push;

    public FragmentChangeEvent(Object source) {
        super(source);
    }

    public FragmentChangeEvent(Object source , int fragmentId) {
        super(source);
        this.fragmentId= fragmentId;
        push=false;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }
}
