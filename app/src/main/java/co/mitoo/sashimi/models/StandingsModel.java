package co.mitoo.sashimi.models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import co.mitoo.sashimi.models.appObject.MitooStandings;
import co.mitoo.sashimi.models.jsonPojo.recieve.standings.SteakStandings;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.StandingsModelResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-04-20.
 */
public class StandingsModel extends MitooModel {

    private SteakStandings steakStandings;
    private List<MitooStandings> mitooStandings;

    public StandingsModel(MitooActivity activity) {
        super(activity);
    }

    public void requestCompetitionStandings(int id , boolean refresh) {
        if (getMitooStandings().size()==0 || refresh)
            handleObservable(getSteakApiService().getCompetitionStandings(id), SteakStandings.class);
        else
            BusProvider.post(new StandingsModelResponseEvent());
    }


    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {
        if (objectRecieve instanceof SteakStandings) {
            SteakStandings steakStanding = (SteakStandings) objectRecieve;
            setSteakStandings(steakStanding);
            setMitooStandings(standingsTransform(steakStanding));
            BusProvider.post(new StandingsModelResponseEvent());
        }
    }

    @Override
    public void resetFields() {
    }

    public List<MitooStandings> getMitooStandings() {
        if(mitooStandings==null)
            mitooStandings = new ArrayList<MitooStandings>();
        return mitooStandings;
    }

    public void setMitooStandings(List<MitooStandings> mitooStandings) {
        this.mitooStandings = mitooStandings;
    }

    public SteakStandings getSteakStandings() {
        return steakStandings;
    }

    public void setSteakStandings(SteakStandings steakStandings) {
        this.steakStandings = steakStandings;
    }

    private List<MitooStandings> standingsTransform(SteakStandings steakStanding){
        MitooStandings.setUpClassData(steakStanding);
        List<MitooStandings> result = new ArrayList<MitooStandings>();
        int[] series = steakStanding.getSeries();
        for(int id : series){
            result.add(new MitooStandings(id));
        }
        Collections.sort(result);
        return result;
    }
}