package co.mitoo.sashimi.models;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FixtureWrapper;
import co.mitoo.sashimi.utils.events.FixtureModelIndividualResponse;
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
    private FixtureWrapper selectedFixture;

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

    public void requestFixtureByFixtureID(int fixtureID, boolean refresh) {

        if(getSelectedFixture() ==null || refresh){
            Observable<Fixture> observable = getSteakApiService().getFixtureFromFixtureID(fixtureID);
            handleObservable(observable, Fixture.class);
        }
        else{
            BusProvider.post(new FixtureModelIndividualResponse());
        }

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof Fixture[]) {
            clearFixtures();
            addFixtures((Fixture[]) objectRecieve);
            BusProvider.post(new FixtureModelResponseEvent());
        }else if(objectRecieve instanceof Fixture) {
            Fixture fixture = (Fixture)objectRecieve;
            setSelectedFixture(new FixtureWrapper(fixture, getActivity()));
            addFixtureToList(fixture);
            BusProvider.post(new FixtureModelIndividualResponse());
        }
    }

    public void addFixtureToList(Fixture fixture) {

        Fixture[] fixtureArray = new Fixture[1];
        fixtureArray[0] = fixture;
        FixtureWrapper fixtureWrapper = new FixtureWrapper(fixture , getActivity());

        if(fixtureWrapper.isFutureFixture()){
            if(!listContainsFixture(getSchedule(),fixtureWrapper))
                addFixturesToSchedule(fixtureArray);
        }
        else{
            if(!listContainsFixture(getResult(),fixtureWrapper))
                addFixturesToResult(fixtureArray);
        }
    }

    public void addFixturesToResult(Fixture[] fixtures) {

        for (Fixture item : fixtures) {
            FixtureWrapper fixtureWrapper = new FixtureWrapper(item, getActivity());
            getResult().add(fixtureWrapper);
        }
        Collections.sort(getResult());
        Collections.reverse(getResult());
        getActivity().getDataHelper().setUpFixtureSection(getResult());
    }

    public void addFixturesToSchedule(Fixture[] fixtures) {

        for (Fixture item : fixtures) {
            FixtureWrapper fixtureWrapper = new FixtureWrapper(item, getActivity());
            getSchedule().add(fixtureWrapper);
        }
        Collections.sort(getSchedule());
        getActivity().getDataHelper().setUpFixtureSection(getSchedule());
    }

    public void addFixtures(Fixture[] fixtures) {

        for (Fixture item : fixtures) {
            FixtureWrapper fixtureWrapper = new FixtureWrapper(item , getActivity());
            if(fixtureWrapper.isFutureFixture())
                getSchedule().add(fixtureWrapper);
            else
                getResult().add(fixtureWrapper);
        }
        Collections.sort(getSchedule());
        Collections.sort(getResult());
        Collections.reverse(getResult());
        getActivity().getDataHelper().setUpFixtureSection(getSchedule());
        getActivity().getDataHelper().setUpFixtureSection(getResult());
    }

    @Override
    public void resetFields() {
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

    public FixtureWrapper getFixtureFromModel(int fixtureID){

        FixtureWrapper result = getFixtureFromList(fixtureID , getSchedule());
        if(result!=null)
            return result;
        else
            return getFixtureFromList(fixtureID, getResult());

    }

    public FixtureWrapper getFixtureFromList(int fixtureID, List<FixtureWrapper> fixtureList){

        FixtureWrapper result = null;
        loop:
        for(FixtureWrapper item : fixtureList){
            if(item.getFixture().getId()==fixtureID){
                result = item;
                break loop;
            }
        }
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

    public FixtureWrapper getSelectedFixture() {
        return selectedFixture;
    }

    public void setSelectedFixture(FixtureWrapper selectedFixture) {
        this.selectedFixture = selectedFixture;
    }

    private boolean listContainsFixture(List<FixtureWrapper> list, FixtureWrapper fixtureWrapper){

        boolean contains = false;
        loop:
        for(FixtureWrapper item : list){
            if(fixtureWrapper.getFixture().getId() == item.getFixture().getId()){
                contains=true;
                break loop;
            }
        }
        return contains;
    }
}