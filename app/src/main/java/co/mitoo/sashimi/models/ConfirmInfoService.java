package co.mitoo.sashimi.models;
import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.managers.ModelManager;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.ConfirmInfo;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.ConfirmInfoResponseEvent;
import co.mitoo.sashimi.utils.events.ConfirmingUserRequestEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-03-20.
 */
public class ConfirmInfoService extends MitooService {

    private ConfirmInfo confirmInfo;
    private String token;
    public ConfirmInfoService(MitooActivity activity) {
        super(activity);
    }

    @Subscribe
    public void requestConfirmationInformation(ConfirmingUserRequestEvent event){

        if(event.getToken()!=null)
            this.token = event.getToken();
        if(getConfirmInfo()==null){
            handleObservable(getSteakApiService().getConfirmationInfo(event.getToken()), ConfirmInfo.class) ;
        }
        else{
            BusProvider.post(new ConfirmInfoResponseEvent(confirmInfo,this.token));
        }

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {

        if (objectRecieve instanceof ConfirmInfo) {
            ConfirmInfo confirmInfo = (ConfirmInfo) objectRecieve;
            setConfirmInfo(confirmInfo);
            setUpDataForOtherModels(confirmInfo);
            BusProvider.post(new ConfirmInfoResponseEvent(confirmInfo,this.token));

        }
    }

    @Override
    public void resetFields() {
        setConfirmInfo(null);
    }

    public ConfirmInfo getConfirmInfo() {
        return confirmInfo;
    }

    public void setConfirmInfo(ConfirmInfo confirmInfo) {
        this.confirmInfo = confirmInfo;
    }

    private void setUpDataForOtherModels(ConfirmInfo confirmInfo){

        addLeagueDataToComp();

        ModelManager manager = getActivity().getModelManager();
        UserInfoService userInfoModel = manager.getUserInfoModel();
        userInfoModel.setUserInfoRecieve(confirmInfo.getUser());

        LeagueService leagueModel = manager.getLeagueModel();
        leagueModel.setSelectedLeague(confirmInfo.getLeague());

        CompetitionService competitionModel = manager.getCompetitionModel();
        competitionModel.resetFields();
        competitionModel.addCompetition(confirmInfo.getCompetition_seasons());

    }

    private void addLeagueDataToComp() {
        Competition[] competitions = getConfirmInfo().getCompetition_seasons();
        League league = getConfirmInfo().getLeague();
        getActivity().getDataHelper().addLeagueObjToCompetition(competitions, league);
    }

}