package co.mitoo.sashimi.models;
import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.CompetitionModelResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import rx.Observable;

/**
 * Created by david on 15-03-06.
 */
public class CompetitionModel extends MitooModel{

    private List<Competition> myCompetition;
    private Competition selectedCompetition;

    public CompetitionModel(MitooActivity activity) {
        super(activity);
    }

    public void requestCompetition(int userID){

        if(competiionIsEmpty()){
            String filterParam = getActivity().getString(R.string.steak_api_param_filter_all);
            String leagueInfoParam = getActivity().getString(R.string.steak_api_param_league_info_true);
            Observable<Competition[]> observable = getSteakApiService()
                    .getCompetitionSeasonFromUserID(filterParam, leagueInfoParam, userID);
            handleObservable(observable, Competition[].class);
        }
        else{
            BusProvider.post(new CompetitionModelResponseEvent());
        }
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof Competition[]) {
            setMyCompetition(new ArrayList<Competition>());
            addCompetition((Competition[]) objectRecieve);
            BusProvider.post(new CompetitionModelResponseEvent());
        }
    }

    public void addCompetition(Competition[] competitions) {

        for (Competition item : competitions) {
            getMyCompetition().add(item);
        }

    }

    public void resetFields(){
        this.myCompetition = null;
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

        if(selectedCompetition==null)
            selectedCompetition = getMyCompetition().get(0);
        return selectedCompetition;
    }

    public void setSelectedCompetition(Competition selectedCompetition) {
        this.selectedCompetition = selectedCompetition;
    }

    private boolean competiionIsEmpty(){
        return getMyCompetition().size()==0;
    }
}