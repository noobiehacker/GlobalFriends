package co.mitoo.sashimi.models;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.CompetitionSeasonRequestEvent;
import co.mitoo.sashimi.utils.events.CompetitionSeasonResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 15-03-06.
 */
public class CompetitionModel extends MitooModel{

    private List<Competition> myCompetition;
    private Competition selectedCompetition;

    public CompetitionModel(MitooActivity activity) {
        super(activity);
    }

    @Subscribe
    public void onCompetitionRequest(CompetitionSeasonRequestEvent event){
        Competition competition = getCompetitionFromID(event.getCompetitionSeasonID());
        if(competition!=null){
            BusProvider.post(new CompetitionSeasonResponseEvent(competition));
        }else{
            requestCompetition( event.getUserID(), event.getCompetitionSeasonID());
        }
    }

    public void requestCompetition(int userID){

        if(competitionIsEmpty()){
            String filterParam = getActivity().getString(R.string.steak_api_param_filter_all);
            String leagueInfoParam = getActivity().getString(R.string.steak_api_param_league_info_true);
            Observable<Competition[]> observable = getSteakApiService()
                    .getCompetitionSeasonFromUserID(filterParam, leagueInfoParam, userID);
            handleObservable(observable, Competition[].class);
        }
        else{
            BusProvider.post(new CompetitionSeasonResponseEvent(null));
        }
    }

    public void requestCompetition(int userID , final int competitionSeasonID){

        if(competitionIsEmpty()){

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
                    setMyCompetition(new ArrayList<Competition>());
                    addCompetition(competitions);
                    BusProvider.post(new CompetitionSeasonResponseEvent(getCompetitionFromID(competitionSeasonID)));

                }
            });
        }
        else{
            Competition competition = getCompetitionFromID(competitionSeasonID);
            if(competition!=null)
                BusProvider.post(new CompetitionSeasonResponseEvent(null));
        }
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof Competition[]) {
            setMyCompetition(new ArrayList<Competition>());
            addCompetition((Competition[]) objectRecieve);
            BusProvider.post(new CompetitionSeasonResponseEvent(null));
        }
    }

    public void addCompetition(Competition[] competitions) {

        for (Competition item : competitions) {
            getMyCompetition().add(item);
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

    public void setMyCompetition(List<Competition> myCompetition) {
        this.myCompetition = myCompetition;
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