package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 15-05-06.
 */
public class FixtureListRequestEvent {

    private MitooEnum.FixtureTabType tabType;
    private int competitionSeasonID;

    public MitooEnum.FixtureTabType getTabType() {
        return tabType;
    }

    public int getCompetitionSeasonID() {
        return competitionSeasonID;
    }

    public FixtureListRequestEvent(MitooEnum.FixtureTabType tabType, int competitionSeasonID) {
        this.tabType = tabType;
        this.competitionSeasonID = competitionSeasonID;
    }

}
