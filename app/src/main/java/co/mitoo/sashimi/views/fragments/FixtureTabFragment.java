package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.utils.FixtureWrapper;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-03-08.
 */
public class FixtureTabFragment extends MitooFragment {

    private LinearLayout fixtureTabContainerView;
    private MitooEnum.FixtureTabType tabType;

    @Override
    public void onClick(View v) {
    }

    public static FixtureTabFragment newInstance() {
        FixtureTabFragment fragment = new FixtureTabFragment();
        return fragment;
    }

    public static FixtureTabFragment newInstance(MitooEnum.FixtureTabType tabType) {
        FixtureTabFragment fragment = new FixtureTabFragment();
        fragment.setTabType(tabType);
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
        setFixtureTabContainerView((LinearLayout) view.findViewById(R.id.scheduleContainerView));
        setUpScheduleContainerView();

    }

    private void setUpScheduleContainerView(){

        List<FixtureWrapper> fixtureList;
        switch(getTabType()){
            case FIXTURE_RESULT:
                fixtureList = getFixtureModel().getResult();
                break;
            case FIXTURE_SCHEDULE:
                fixtureList = getFixtureModel().getSchedule();
                break;
            default:
                fixtureList = getFixtureModel().getSchedule();
        }
        getViewHelper().setUpFixtureForTabRefrac(fixtureList, getFixtureTabContainerView());
    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    public LinearLayout getFixtureTabContainerView() {
        return fixtureTabContainerView;
    }

    public void setFixtureTabContainerView(LinearLayout fixtureTabContainerView) {
        this.fixtureTabContainerView = fixtureTabContainerView;
    }

    public MitooEnum.FixtureTabType getTabType() {
        if (tabType == null)
            setTabType(MitooEnum.FixtureTabType.FIXTURE_SCHEDULE);
        return tabType;
    }

    public void setTabType(MitooEnum.FixtureTabType tabType) {
        this.tabType = tabType;
    }
}
