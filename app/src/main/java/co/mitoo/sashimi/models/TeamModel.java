package co.mitoo.sashimi.models;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.events.TeamModelResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-03-10.
 */
public class TeamModel extends MitooModel {


    private List<Team> competitionTeams;

    public TeamModel(MitooActivity activity) {
        super(activity);
    }

    public void requestTeam(int id) {
        if (getCompetitionTeams().size()==0)
            handleObservable(getSteakApiService().getTeam(id), Team[].class);
        else
            BusProvider.post(new TeamModelResponseEvent());
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {
        if (objectRecieve instanceof Team[]) {
            clearLeaguesEnquired();
            addTeams((Team[]) objectRecieve);
            BusProvider.post(new TeamModelResponseEvent());
        }
    }

    public void addTeams(Team[] newTeams) {

        DataHelper helper = getActivity().getDataHelper();
        for (Team item : newTeams) {
            getCompetitionTeams().add(item);
        }
    }

    @Override
    protected void resetFields() {
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