package co.mitoo.sashimi.utils.events;


import java.util.List;

import co.mitoo.sashimi.utils.FixtureWrapper;

/**
 * Created by david on 15-03-11.
 */
public class FixtureModelResponseEvent {

    private List<FixtureWrapper> schedule;
    private List<FixtureWrapper> result;

    public List<FixtureWrapper> getSchedule() {
        return schedule;
    }

    public List<FixtureWrapper> getResult() {
        return result;
    }

    public FixtureModelResponseEvent(List<FixtureWrapper> schedule, List<FixtureWrapper> result) {
        this.schedule = schedule;
        this.result = result;
    }
}
