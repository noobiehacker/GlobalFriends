package co.mitoo.sashimi.network.Services;
import android.util.Log;

import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.FixtureModel;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.CompetitionDataClearEvent;
import co.mitoo.sashimi.utils.events.CompetitionListResponseEvent;
import co.mitoo.sashimi.utils.events.CompetitionNotificationRequestEvent;
import co.mitoo.sashimi.utils.events.CompetitionNotificationResponseEvent;
import co.mitoo.sashimi.utils.events.CompetitionRequestByUserID;
import co.mitoo.sashimi.utils.events.CompetitionSeasonReqByCompAndUserID;
import co.mitoo.sashimi.utils.events.CompetitionSeasonRequestByCompID;
import co.mitoo.sashimi.utils.events.CompetitionSeasonResponseEvent;
import co.mitoo.sashimi.utils.events.FixtureNotificaitonRequestEvent;
import co.mitoo.sashimi.utils.events.FixtureNotificationResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 15-03-06.
 */
public class CompetitionService extends MitooService {

    private List<Competition> myCompetition;
    private Competition selectedCompetition;

    public CompetitionService(MitooActivity activity) {
        super(activity);
    }

    @Subscribe
    public void onCompetitionUserIdRequest(CompetitionRequestByUserID event){
        if(this.myCompetition!=null && !this.myCompetition.isEmpty()){
            BusProvider.post(new CompetitionListResponseEvent(this.myCompetition));
        }else{
            requestCompetitionByUserID(event.getUserID());
        }
    }

    @Subscribe
    public void onCompetitionCompAndUserIdAnRequest(CompetitionSeasonReqByCompAndUserID event){
        Competition competition = getCompetitionFromID(event.getCompetitionSeasonID());
        if(competition!=null){
            BusProvider.post(new CompetitionSeasonResponseEvent(competition));
        }
            requestCompetitionByUserIDAndCompID(event.getUserID(), event.getCompetitionSeasonID());
    }

    @Subscribe
    public void onCompetitionSeasonDataClearEvent(CompetitionDataClearEvent event){
        this.myCompetition=null;
    }


    @Subscribe
    public void onCompetitionCompIdRequest(CompetitionSeasonRequestByCompID event) {
        Competition competition = getCompetitionFromID(event.getCompetitionSeasonID());
        if (competition != null) {
            BusProvider.post(new CompetitionSeasonResponseEvent(competition));
        }
        requestCompetitionByCompetitionID(event.getCompetitionSeasonID());
    }

    @Subscribe
    public void onCompetitionNotificaitonRequestEvent(CompetitionNotificationRequestEvent event){

        Observable<Competition> observable = getSteakApiService().getCompetitionSeasonByID(event.getCompetitionSeasonID());
        observable.subscribe(new Subscriber<Competition>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                BusProvider.post(new CompetitionNotificationResponseEvent(e));

            }

            @Override
            public void onNext(Competition competition) {
                removeCompetition(competition);
                addCompetition(competition);
                BusProvider.post(new CompetitionNotificationResponseEvent(competition));

            }
        });
    }

    private void requestCompetitionByUserID(int userID){

        if(competitionIsEmpty()){
            String filterParam = getActivity().getString(R.string.steak_api_param_filter_all);
            String leagueInfoParam = getActivity().getString(R.string.steak_api_param_league_info_true);
            Observable<Competition[]> observable = getSteakApiService()
                    .getCompetitionSeasonFromUserID(filterParam, leagueInfoParam, userID);
            handleObservable(observable, Competition[].class);
        }
        else{
            BusProvider.post(new CompetitionListResponseEvent(this.myCompetition));
        }
    }

    private void requestCompetitionByCompetitionID(int competitionSeasonID){

            Observable<Competition> observable = getSteakApiService()
                    .getCompetitionSeasonByID(competitionSeasonID);
            observable.subscribe(new Subscriber<Competition>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Competition competition) {
                    removeCompetition(competition);
                    addCompetition(competition);
                    BusProvider.post(new CompetitionSeasonResponseEvent(competition));

                }
            });

    }

    private void requestCompetitionByUserIDAndCompID(int userID, final int competitionSeasonID) {

        String filterParam = getActivity().getString(R.string.steak_api_param_filter_all);
        String leagueInfoParam = getActivity().getString(R.string.steak_api_param_league_info_true);
        Observable<Competition[]> observable = getSteakApiService()
                .getCompetitionSeasonFromUserID(filterParam, leagueInfoParam, userID);
        observable.subscribe(new Subscriber<Competition[]>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Competition[] competitions) {
                CompetitionService.this.myCompetition = new ArrayList<Competition>();
                addCompetition(competitions);
                BusProvider.post(new CompetitionSeasonResponseEvent(getCompetitionFromID(competitionSeasonID)));

            }
        });

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof Competition[]) {
            this.myCompetition = new ArrayList<Competition>();
            addCompetition((Competition[]) objectRecieve);
            BusProvider.post(new CompetitionListResponseEvent(this.myCompetition));
        }
    }

    public void addCompetition(Competition[] competitions) {

        for (Competition item : competitions) {
            getMyCompetition().add(item);
        }

    }

    public void addCompetition(Competition competition) {

        getMyCompetition().add(competition);

    }

    public void removeCompetition(Competition competition) {

        for(int i = 0 ; i<getMyCompetition().size() ; i++){
            if(getMyCompetition().get(i).getId() == competition.getId())
                getMyCompetition().remove(i);
        }

    }

    @Override
    public void resetFields(){
        this.myCompetition = null;
        this.selectedCompetition= null;
    }

    public List<Competition> getMyCompetition() {

        if(myCompetition==null)
            myCompetition = new ArrayList<Competition>();
        return myCompetition;
    }

    public Competition getSelectedCompetition() {

        if(selectedCompetition==null){
            if(!getMyCompetition().isEmpty())
                selectedCompetition = getMyCompetition().get(0);
        }
        return selectedCompetition;
    }

    public void setSelectedCompetition(Competition selectedCompetition) {
        this.selectedCompetition = selectedCompetition;
    }

    private boolean competitionIsEmpty(){
        return getMyCompetition().size()==0;
    }

    public Competition getCompetitionFromID(int id ){

        Competition result = null;
        loop:
        for(Competition item : getMyCompetition()){
            if(item.getId()==id){
                result = item;
                break loop;
            }
        }
        return result;

    }

}