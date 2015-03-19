package co.mitoo.sashimi.models;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FixtureWrapper;
import co.mitoo.sashimi.utils.events.FixtureModelResponseEvent;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.views.activities.MitooActivity;
import rx.Observable;

/**
 * Created by david on 15-03-10.
 */
public class FixtureModel extends MitooModel{

    private List<FixtureWrapper> schedule;
    private List<FixtureWrapper> result;

    public FixtureModel(MitooActivity activity) {
        super(activity);
    }

    public void requestFixtureByCompetition(int competitionSeasonID, boolean refresh) {

        if(fixtureIsEmpty() || refresh){
            Observable<Fixture[]> observable = getSteakApiService()
                    .getFixtureFromCompetitionID(getActivity().getString(R.string.steak_api_param_filter_all), competitionSeasonID);
            handleObservable(observable, Fixture[].class);
        }

        else{
            BusProvider.post(new FixtureModelResponseEvent());
        }

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof Fixture[]) {
            clearFixtures();
            addFixtures((Fixture[]) objectRecieve);
            BusProvider.post(new FixtureModelResponseEvent());
        }
    }

    public void addFixtures(Fixture[] fixtures) {

        for (Fixture item : fixtures) {
            FixtureWrapper fixtureWrapper = new FixtureWrapper(item , getActivity());
            Date now = new Date();
            Date fixtureDate = fixtureWrapper.getFixtureDate();
            if(fixtureWrapper.getFixture().isTime_tbc() || fixtureDate.after(now))
                getSchedule().add(fixtureWrapper);
            else
                getResult().add(fixtureWrapper);
        }
        Collections.sort(getSchedule());
        Collections.sort(getResult());
    }

    @Override
    protected void resetFields() {
        clearFixtures();
    }

    private void clearFixtures(){

        setResult(new ArrayList<FixtureWrapper>());
        setSchedule(new ArrayList<FixtureWrapper>());
    }

    public List<FixtureWrapper> getSchedule() {
        if(schedule == null)
            setSchedule(new ArrayList<FixtureWrapper>());
        return schedule;
    }

    public List<FixtureWrapper> getResult() {
        if(result == null)
            setResult(new ArrayList<FixtureWrapper>());
        return result;
    }

    public void setSchedule(List<FixtureWrapper> schedule) {
        this.schedule = schedule;
    }

    public void setResult(List<FixtureWrapper> result) {
        this.result = result;
    }

    private boolean fixtureIsEmpty(){
        return getSchedule().size()==0 && getResult().size() == 0;
    }
}