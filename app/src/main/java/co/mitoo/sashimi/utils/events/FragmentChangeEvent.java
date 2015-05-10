package co.mitoo.sashimi.utils.events;
import android.os.Bundle;

import java.util.EventObject;
import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 14-11-13.
 */
public class FragmentChangeEvent extends EventObject{

    private int fragmentId;
    private MitooEnum.FragmentTransition transition;
    private MitooEnum.FragmentAnimation animation = MitooEnum.FragmentAnimation.HORIZONTAL;
    private Bundle bundle;
    private boolean popPrevious = false;

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setTransition(MitooEnum.FragmentTransition transition) {
        this.transition = transition;
    }

    public MitooEnum.FragmentTransition getTransition() {
        return transition;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public FragmentChangeEvent(Object source, MitooEnum.FragmentTransition transition) {
        super(source);
        this.transition = transition;
    }

    public FragmentChangeEvent(Object source, MitooEnum.FragmentTransition transition , int fragmentId) {
        super(source);
        this.transition = transition;
        this.fragmentId= fragmentId;
    }

    public FragmentChangeEvent(Object source, MitooEnum.FragmentTransition transition , int fragmentId , MitooEnum.FragmentAnimation animation) {
        super(source);
        this.transition = transition;
        this.fragmentId= fragmentId;
        this.animation = animation;
    }

    public FragmentChangeEvent(Object source, MitooEnum.FragmentTransition transition , int fragmentId , MitooEnum.FragmentAnimation animation , Bundle bundle) {
        super(source);
        this.transition = transition;
        this.fragmentId= fragmentId;
        this.animation = animation;
        this.bundle = bundle;
    }

    public FragmentChangeEvent(Object source, MitooEnum.FragmentTransition transition , int fragmentId , MitooEnum.FragmentAnimation animation , Bundle bundle ,Boolean pop) {
        super(source);
        this.transition = transition;
        this.fragmentId= fragmentId;
        this.animation = animation;
        this.bundle = bundle;
        this.popPrevious=pop;
    }


    public FragmentChangeEvent(Object source, MitooEnum.FragmentTransition transition,int fragmentId,  Bundle bundle) {
        super(source);
        this.fragmentId = fragmentId;
        this.transition = transition;
        this.bundle = bundle;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public MitooEnum.FragmentAnimation getAnimation() {
        return animation;
    }

    public void setAnimation(MitooEnum.FragmentAnimation animation) {
        this.animation = animation;
    }

    public boolean popPrevious() {
        return popPrevious;
    }
}
