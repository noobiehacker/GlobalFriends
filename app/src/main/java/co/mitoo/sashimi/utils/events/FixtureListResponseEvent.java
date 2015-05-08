package co.mitoo.sashimi.utils.events;
import java.util.List;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.utils.FixtureWrapper;
import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 15-05-06.
 */
public class FixtureListResponseEvent {

    private List<FixtureWrapper> fixtureList;
    private MitooEnum.FixtureTabType tabType;

    public List<FixtureWrapper> getFixtureList() {
        return fixtureList;
    }

    public MitooEnum.FixtureTabType getTabType() {
        return tabType;
    }

    public FixtureListResponseEvent( MitooEnum.FixtureTabType tabType , List<FixtureWrapper> fixtureList) {
        this.fixtureList = fixtureList;
        this.tabType = tabType;
    }
}
