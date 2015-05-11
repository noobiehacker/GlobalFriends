package co.mitoo.sashimi.utils.events;


import java.util.List;

import co.mitoo.sashimi.models.FixtureModel;

/**
 * Created by david on 15-03-11.
 */
public class FixtureModelResponseEvent {

    private List<FixtureModel> schedule;
    private List<FixtureModel> result;

    public List<FixtureModel> getSchedule() {
        return schedule;
    }

    public List<FixtureModel> getResult() {
        return result;
    }

    public FixtureModelResponseEvent(List<FixtureModel> schedule, List<FixtureModel> result) {
        this.schedule = schedule;
        this.result = result;
    }
}
