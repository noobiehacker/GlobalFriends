package co.mitoo.sashimi.views.widgets;

import co.mitoo.sashimi.R;
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

    public MitooTab(MitooEnum.FixtureTabType tabType) {
       setFixtureTabType(tabType);
    }

    public MitooFragment getFragment() {

        if(fragment==null)
            fragment= FragmentFactory.getInstance()
                    .createTabFragment(R.id.fragment_competition_tab, getFixtureTabType());
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
        if(fixtureTabType==null)
            fixtureTabType= MitooEnum.FixtureTabType.FIXTURE_SCHEDULE;
        return fixtureTabType;
    }

    public void setFixtureTabType(MitooEnum.FixtureTabType fixtureTabType) {
        this.fixtureTabType = fixtureTabType;
    }

}
