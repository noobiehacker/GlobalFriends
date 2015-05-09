package co.mitoo.sashimi.utils;

import android.os.Bundle;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;

/**
 * Created by david on 15-03-24.
 */
public class FragmentChangeEventBuilder {

    private static FragmentChangeEventBuilder singleTonInstance;
    private int fragmentId;
    private MitooEnum.FragmentTransition transition;
    private MitooEnum.FragmentAnimation animation ;
    private Bundle bundle;
    private boolean popPrevious=false;

    public static FragmentChangeEventBuilder getSingletonInstance(){
        if(FragmentChangeEventBuilder.singleTonInstance==null){
            FragmentChangeEventBuilder.singleTonInstance= new FragmentChangeEventBuilder();
        }
        return FragmentChangeEventBuilder.singleTonInstance;
    }

    public FragmentChangeEventBuilder() {
        setDefaultBuilderParamters();
    }

    public FragmentChangeEventBuilder setFragmentID(int fragmentID){
        this.fragmentId=fragmentID;
        return this;
    }

    public FragmentChangeEventBuilder setTransition(MitooEnum.FragmentTransition transition){
        this.transition=transition;
        return this;

    }

    public FragmentChangeEventBuilder setAnimation(MitooEnum.FragmentAnimation animation){
        this.animation=animation;
        return this;
    }

    public FragmentChangeEventBuilder setBundle(Bundle bundle){
        this.bundle = bundle;
        return this;
    }

    public FragmentChangeEventBuilder setPopPrevious(boolean bool){
        this.popPrevious=bool;
        return this;
    }

    public FragmentChangeEvent build(){
        FragmentChangeEvent event = new FragmentChangeEvent(this,getTransition() ,  getFragmentId(),getAnimation(), getBundle(), this.popPrevious);
        setDefaultBuilderParamters();
        return event;

    }

    private void setDefaultBuilderParamters(){
        setFragmentID(R.id.fragment_landing);
        setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL);
        setTransition(MitooEnum.FragmentTransition.PUSH);
        setBundle(null);
        this.popPrevious=false;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public MitooEnum.FragmentTransition getTransition() {
        return transition;
    }

    public MitooEnum.FragmentAnimation getAnimation() {
        return animation;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
