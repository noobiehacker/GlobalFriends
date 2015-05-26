package co.mitoo.sashimi.network.Services;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.models.FixtureModel;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FixtureDataClearEvent;
import co.mitoo.sashimi.utils.events.FixtureIndividualRequestEvent;
import co.mitoo.sashimi.utils.events.FixtureListRequestEvent;
import co.mitoo.sashimi.utils.events.FixtureListResponseEvent;
import co.mitoo.sashimi.utils.events.FixtureModelIndividualResponse;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.utils.events.FixtureNotificaitonRequestEvent;
import co.mitoo.sashimi.utils.events.FixtureNotificationResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 15-03-10.
 */
public class FixtureService extends MitooService {

    private List<FixtureModel> schedule;
    private List<FixtureModel> result;

    public FixtureService(MitooActivity activity) {
        super(activity);
    }

    @Subscribe
    public void onFixtureDataClearEvent(FixtureDataClearEvent event){
        this.schedule= null;
        this.result = null;
    }

    @Subscribe
    public void onFixtureNotificaitonRequestEvent(FixtureNotificaitonRequestEvent event){

        Observable<Fixture> observable = getSteakApiService().getFixtureFromFixtureID(event.getFixtureID());
        observable.subscribe(new Subscriber<Fixture>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                BusProvider.post(new FixtureNotificationResponseEvent(e));

            }

            @Override
            public void onNext(Fixture fixture) {
                BusProvider.post(new FixtureNotificationResponseEvent(new FixtureModel(fixture , getActivity())));

            }
        });
    }

    @Subscribe
    public void onFixtureListRequestEvent(FixtureListRequestEvent event) {

        if (!getResult().isEmpty() || !getSchedule().isEmpty()) {
            if (event.getTabType() == MitooEnum.FixtureTabType.FIXTURE_RESULT)
                BusProvider.post(new FixtureListResponseEvent(event.getTabType(), getResult()));
            else if (event.getTabType() == MitooEnum.FixtureTabType.FIXTURE_SCHEDULE)
                BusProvider.post(new FixtureListResponseEvent(event.getTabType(), getSchedule()));
        }
        requestFixtureByCompetition(event.getCompetitionSeasonID());

    }

    public void requestFixtureByCompetition(int competitionSeasonID) {

        Observable<Fixture[]> observable = getSteakApiService()
                .getFixtureFromCompetitionID(getActivity().getString(R.string.steak_api_param_filter_all), competitionSeasonID);
        handleObservable(observable, Fixture[].class);

    }

    @Subscribe
    public void requestIndividualFixture(FixtureIndividualRequestEvent event) {

        if(getFixtureFromModel(event.getFixtureID()) !=null ){
            BusProvider.post(new FixtureModelIndividualResponse(getFixtureFromModel(event.getFixtureID())));
        }
        Observable<Fixture> observable = getSteakApiService().getFixtureFromFixtureID(event.getFixtureID());
        handleObservable(observable, Fixture.class);

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof Fixture[]) {
            clearFixtures();
            addFixtures((Fixture[]) objectRecieve);
            BusProvider.post(new FixtureListResponseEvent(MitooEnum.FixtureTabType.FIXTURE_RESULT,getResult()));
            BusProvider.post(new FixtureListResponseEvent(MitooEnum.FixtureTabType.FIXTURE_SCHEDULE,getSchedule()));
        }else if(objectRecieve instanceof Fixture) {
            Fixture fixture = (Fixture) objectRecieve;
            addFixtureToList(fixture);
            BusProvider.post(new FixtureModelIndividualResponse(new FixtureModel(fixture, getActivity())));
        }
    }

    public void addFixtureToList(Fixture fixture) {

        Fixture[] fixtureArray = new Fixture[1];
        fixtureArray[0] = fixture;
        FixtureModel fixtureModel = new FixtureModel(fixture , getActivity());

        if(fixtureModel.isFutureFixture()){
            if(!listContainsFixture(getSchedule(), fixtureModel))
                addFixturesToSchedule(fixtureArray);
        }
        else{
            if(!listContainsFixture(getResult(), fixtureModel))
                addFixturesToResult(fixtureArray);
        }
    }

    public void addFixturesToResult(Fixture[] fixtures) {

        for (Fixture item : fixtures) {
            FixtureModel fixtureModel = new FixtureModel(item, getActivity());
            getResult().add(fixtureModel);
        }
        Collections.sort(getResult());
        Collections.reverse(getResult());
        getActivity().getDataHelper().setUpFixtureSection(getResult());
    }

    public void addFixturesToSchedule(Fixture[] fixtures) {

        for (Fixture item : fixtures) {
            FixtureModel fixtureModel = new FixtureModel(item, getActivity());
            getSchedule().add(fixtureModel);
        }
        Collections.sort(getSchedule());
        getActivity().getDataHelper().setUpFixtureSection(getSchedule());
    }

    public void addFixtures(Fixture[] fixtures) {

        for (Fixture item : fixtures) {
            FixtureModel fixtureModel = new FixtureModel(item , getActivity());
            if(fixtureModel.isFutureFixture()){
                if(!listContainsFixture(getSchedule(), fixtureModel))
                    getSchedule().add(fixtureModel);
            }
            else{
                if(!listContainsFixture(getResult(), fixtureModel))
                    getResult().add(fixtureModel);
            }
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

        setResult(new ArrayList<FixtureModel>());
        setSchedule(new ArrayList<FixtureModel>());
    }

    public List<FixtureModel> getSchedule() {
        if(schedule == null)
            setSchedule(new ArrayList<FixtureModel>());
        return schedule;
    }

    public List<FixtureModel> getResult() {
        if(result == null)
            setResult(new ArrayList<FixtureModel>());
        return result;
    }

    public FixtureModel getFixtureFromModel(int fixtureID){

        FixtureModel result = getFixtureFromList(fixtureID , getSchedule());
        if(result!=null)
            return result;
        else
            return getFixtureFromList(fixtureID, getResult());

    }

    public FixtureModel getFixtureFromList(int fixtureID, List<FixtureModel> fixtureList){

        FixtureModel result = null;
        loop:
        for(FixtureModel item : fixtureList){
            if(item.getFixture().getId()==fixtureID){
                result = item;
                break loop;
            }
        }
        return result;

    }

    public void setSchedule(List<FixtureModel> schedule) {
        this.schedule = schedule;
    }

    public void setResult(List<FixtureModel> result) {
        this.result = result;
    }

    private boolean fixtureIsEmpty(){
        return getSchedule().size()==0 && getResult().size() == 0;
    }

    private boolean listContainsFixture(List<FixtureModel> list, FixtureModel fixtureModel){

        boolean contains = false;
        loop:
        for(FixtureModel item : list){
            if(fixtureModel.getFixture().getId() == item.getFixture().getId()){
                contains=true;
                break loop;
            }
        }
        return contains;
    }


}