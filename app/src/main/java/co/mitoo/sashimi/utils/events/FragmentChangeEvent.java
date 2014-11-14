package co.mitoo.sashimi.utils.events;

import java.util.EventObject;

/**
 * Created by david on 14-11-13.
 */
public class FragmentChangeEvent extends EventObject{


    private Class<?> fragmentType;
    private boolean push;

    public FragmentChangeEvent(Object source) {
        super(source);
    }

    public FragmentChangeEvent(Object source , Class<?> fragmentType) {
        super(source);
        this.fragmentType = fragmentType;
        push=false;
    }

    public Class<?> getFragmentType() {
        return fragmentType;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }
}
