package co.mitoo.sashimi.network.Services;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.events.TeamIndividualRequestEvent;
import co.mitoo.sashimi.utils.events.TeamIndividualResponseEvent;
import co.mitoo.sashimi.utils.events.TeamListRequestEvent;
import co.mitoo.sashimi.utils.events.TeamListResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-03-10.
 */
public class TeamService extends MitooService {

    private List<Team> competitionTeams;

    public TeamService(MitooActivity activity) {
        super(activity);
    }

    @Subscribe
    public void requestTeamByCompetition(TeamListRequestEvent event) {
        if (getCompetitionTeams().size()==0 ){
            handleObservable(getSteakApiService().getTeamByCompetition(event.getCompetitionSeasonID()), Team[].class);
            getEventsStack().push(event);
        }
        else
            handleEvent(event);

    }

    @Subscribe
    public void requestIndividualTeam(TeamIndividualRequestEvent event) {
        if (getCompetitionTeams().size()==0 ){
            handleObservable(getSteakApiService().getTeamByCompetition(event.getCompetitionSeasonId()), Team[].class);
            getEventsStack().push(event);
        }
        else
            handleEvent(event);

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {
        if (objectRecieve instanceof Team[]) {
            clearLeaguesEnquired();
            addTeams((Team[]) objectRecieve);
            while(getEventsStack().size()>0){
                Object event = getEventsStack().pop();
                handleEvent(event);
            }
        }
    }

    private void handleEvent(Object event){

        if(event instanceof  TeamIndividualRequestEvent){
            TeamIndividualRequestEvent requestEvent = (TeamIndividualRequestEvent) event;
            BusProvider.post(new TeamIndividualResponseEvent(this.getTeam(requestEvent.getTeamID())));
        }
        else if(event instanceof  TeamListRequestEvent){
            BusProvider.post(new TeamListResponseEvent(this.competitionTeams));
        }
    }

    public void addTeams(Team[] newTeams) {

        DataHelper helper = getActivity().getDataHelper();
        for (Team item : newTeams) {
            getCompetitionTeams().add(item);
        }
    }

    @Override
    public void resetFields() {
        clearLeaguesEnquired();
    }

    private void clearLeaguesEnquired(){
        setCompetitionTeams(new ArrayList<Team>());
    }

    public List<Team> getCompetitionTeams() {
        if(competitionTeams==null)
            competitionTeams = new ArrayList<Team>();
        return competitionTeams;
    }

    public void setCompetitionTeams(List<Team> competitionTeams) {
        this.competitionTeams = competitionTeams;
    }

    public Team getTeam(int id) {

        Team result = null;
        loop:
        for(Team item : getCompetitionTeams()){
            if(item.getId() == id){
                result = item;
            }
            if(result!=null)
                break loop;
        }
        return result;
    }

}