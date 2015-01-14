package co.mitoo.sashimi.utils.events;
import java.util.EventObject;
import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 14-11-13.
 */
public class FragmentChangeEvent extends EventObject{

    private int fragmentId;
    private MitooEnum.fragmentTransition transition;

    public void setTransition(MitooEnum.fragmentTransition transition) {
        this.transition = transition;
    }

    public MitooEnum.fragmentTransition getTransition() {
        return transition;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public FragmentChangeEvent(Object source) {
        super(source);
    }

    public FragmentChangeEvent(Object source, MitooEnum.fragmentTransition transition , int fragmentId) {
        super(source);
        this.transition = transition;
        this.fragmentId= fragmentId;
    }

    public int getFragmentId() {
        return fragmentId;
    }

}
