package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.*;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;

/**
 * Created by david on 14-11-05.
 */
public class LandingFragment extends MitooFragment{


    public static LandingFragment newInstance() {
        LandingFragment fragment = new LandingFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable(v.getId())){
            switch (v.getId()) {
                case R.id.signupButton:
                case R.id.searchButton:

                    FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                            .setFragmentID(R.id.fragment_connect)
                            .setTransition(MitooEnum.FragmentTransition.CHANGE)
                            .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                            .build();
                    BusProvider.post(fragmentChangeEvent);
                    break;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_landing,
                container, false);
        initializeViews(view);
        initializeFields();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void initializeFields(){
        super.initializeFields();
        setPopActionRequiresDelay(true);
    }

    @Override
    protected void initializeViews(View view){
        initializeOnClickListeners(view);

    }

    @Override
    protected void initializeOnClickListeners(View view){
        view.findViewById(R.id.signupButton).setOnClickListener(this);
        view.findViewById(R.id.searchButton).setOnClickListener(this);

    }

}
