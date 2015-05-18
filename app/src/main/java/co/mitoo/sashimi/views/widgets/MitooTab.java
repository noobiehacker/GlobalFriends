package co.mitoo.sashimi.views.widgets;

import android.os.Bundle;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentFactory;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.views.fragments.MitooFragment;

/**
 * Created by david on 15-03-08.
 */
public class MitooTab {

    private MitooFragment fragment;
    private MitooMaterialsTab tab;
    private MitooEnum.FixtureTabType fixtureTabType;
    private int competitionSeasonID;
    private String competitionSeaonKey;
    private Bundle fragmentArgument;

    public MitooTab(MitooEnum.FixtureTabType tabType , int competitionSeasonID , String competitionSeaonKey){
        this.competitionSeasonID = competitionSeasonID;
        this.competitionSeaonKey = competitionSeaonKey;
    }
    public MitooTab(MitooEnum.FixtureTabType tabType , Bundle fragmentArgument){
        this.fragmentArgument = fragmentArgument;
        setFixtureTabType(tabType);
    }

    public MitooFragment getFragment() {

        if(fragment==null){
            switch(this.fixtureTabType){
                case TEAM_STANDINGS:
                    fragment= FragmentFactory.getInstance().createFragment(R.id.fragment_standings);
                    break;
                case FIXTURE_RESULT:
                case FIXTURE_SCHEDULE:
                default:
                    fragment = FragmentFactory.getInstance()
                            .createTabFragment(R.id.fragment_competition_tab, getFixtureTabType());
                    fragment.setArguments(createBundle());
                    break;

            }
            fragment.setArguments(this.fragmentArgument);
        }
        return fragment;
    }

    public MitooMaterialsTab getTab() {
        return tab;
    }

    public void setTab(MitooMaterialsTab tab) {
        this.tab = tab;
    }

    public void setFragment(MitooFragment fragment) {

        this.fragment = fragment;
    }

    public MitooEnum.FixtureTabType getFixtureTabType() {
        if (fixtureTabType == null)
            fixtureTabType = MitooEnum.FixtureTabType.FIXTURE_SCHEDULE;
        return fixtureTabType;
    }

    public void setFixtureTabType(MitooEnum.FixtureTabType fixtureTabType) {
        this.fixtureTabType = fixtureTabType;
    }


    private Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(this.competitionSeaonKey, this.competitionSeasonID);
        return bundle;
    }

}
