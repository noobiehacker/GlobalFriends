package co.mitoo.sashimi.utils.events;
import java.util.List;

import co.mitoo.sashimi.models.FixtureModel;
import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 15-05-06.
 */
public class FixtureListResponseEvent {

    private List<FixtureModel> fixtureList;
    private MitooEnum.FixtureTabType tabType;

    public List<FixtureModel> getFixtureList() {
        return fixtureList;
    }

    public MitooEnum.FixtureTabType getTabType() {
        return tabType;
    }

    public FixtureListResponseEvent( MitooEnum.FixtureTabType tabType , List<FixtureModel> fixtureList) {
        this.fixtureList = fixtureList;
        this.tabType = tabType;
    }
}
