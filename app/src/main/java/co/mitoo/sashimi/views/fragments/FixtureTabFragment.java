package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-03-08.
 */
public class FixtureTabFragment extends MitooFragment {

    private LinearLayout scheduleContainerView;

    @Override
    public void onClick(View v) {

    }

    public static FixtureTabFragment newInstance() {
        FixtureTabFragment fragment = new FixtureTabFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_schedule,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        setScheduleContainerView((LinearLayout)view.findViewById(R.id.scheduleContainerView));
        getScheduleContainerView().addView(getViewHelper().createFixtureGrouped(null));
        getScheduleContainerView().addView(getViewHelper().createFixtureGrouped(null));

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    public LinearLayout getScheduleContainerView() {
        return scheduleContainerView;
    }

    public void setScheduleContainerView(LinearLayout scheduleContainerView) {
        this.scheduleContainerView = scheduleContainerView;
    }
}
