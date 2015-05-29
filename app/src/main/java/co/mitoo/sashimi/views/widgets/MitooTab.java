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

    public MitooTab(MitooEnum.FixtureTabType tabType , String competitionSeaonKey , int competitionSeasonID ,
                    MitooMaterialsTab tab){
        this.competitionSeasonID = competitionSeasonID;
        this.competitionSeaonKey = competitionSeaonKey;
        this.fixtureTabType = tabType;
        this.tab= tab;
    }

    public MitooTab(MitooEnum.FixtureTabType tabType , MitooMaterialsTab tab){
        this.fixtureTabType = tabType;
        this.tab= tab;
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
                            .createTabFragment(R.id.fragment_competition_tab, this.fixtureTabType);
                    break;

            }
            fragment.setArguments(createBundle());

        }
        return fragment;
    }

    private Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(this.competitionSeaonKey, this.competitionSeasonID);
        return bundle;
    }

    public MitooMaterialsTab getTab() {
        return tab;
    }
}
