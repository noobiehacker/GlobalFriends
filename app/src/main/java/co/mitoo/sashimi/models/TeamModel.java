package co.mitoo.sashimi.models;

import java.util.List;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-03-10.
 */
public class TeamModel extends MitooModel{

    private List<Team> competitionTeams;
    public TeamModel(MitooActivity activity) {
        super(activity);
    }

    public void requestTeam(int id){

    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {


    }
    public void resetFields(){

    }

    public List<Team> getCompetitionTeams() {
        return competitionTeams;
    }

    public void setCompetitionTeams(List<Team> competitionTeams) {
        this.competitionTeams = competitionTeams;
    }

    public Team getTeam(int id){

    }
}