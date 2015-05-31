package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;

/**
 * Created by david on 15-05-30.
 */

public class InterestConfirmFragment extends  MitooFragment {

    public static InterestConfirmFragment newInstance() {
        InterestConfirmFragment fragment = new InterestConfirmFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_interest_confirm,
                container, false);
        initializeViews(view);
        initializeFields();
        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentChangeEvent fragmentChangeEvent ;
        if(getDataHelper().isClickable(v.getId())){
            switch (v.getId()) {
                case R.id.button:
                    fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                            .setFragmentID(R.id.fragment_location_selection)
                            .setTransition(MitooEnum.FragmentTransition.PUSH)
                            .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                            .build();
                    BusProvider.post(fragmentChangeEvent);
                    break;
            }
        }

    }

    @Override
    protected void initializeOnClickListeners(View view){
        view.findViewById(R.id.button).setOnClickListener(this);

    }

    @Override
    protected void initializeViews(View view){
        initializeOnClickListeners(view);

    }
}
