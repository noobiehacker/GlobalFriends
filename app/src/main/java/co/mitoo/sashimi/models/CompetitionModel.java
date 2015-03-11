package co.mitoo.sashimi.models;
import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.CompetitionModelResponseEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-03-06.
 */
public class CompetitionModel extends MitooModel{

    private List<Competition> myCompetition;
    private Competition selectedCompetition;

    public CompetitionModel(MitooActivity activity) {
        super(activity);
    }

    public void requestCompetition(){

        setMyCompetition(mockLeague());
        BusProvider.post(new CompetitionModelResponseEvent());
    }

    @Override
    protected void handleSubscriberResponse(Object objectRecieve) {


    }
    public void resetFields(){
        this.myCompetition = null;

    }

    public List<Competition> getMyCompetition() {

        if(myCompetition==null)
            setMyCompetition(mockLeague());
        return myCompetition;
    }

    public void setMyCompetition(List<Competition> myCompetition) {
        this.myCompetition = myCompetition;
    }

    private List<Competition> mockLeague(){

        Competition comp = new Competition();
        comp.setId(123);
        comp.setSeason_name("Winter 2014");
        comp.setName("Wednesday Coed Soccer");
        List<Competition> result =new ArrayList<Competition>();
        result.add(comp);
        return result;
    }

    public Competition getSelectedCompetition() {
        return selectedCompetition;
    }

    public void setSelectedCompetition(Competition selectedCompetition) {
        this.selectedCompetition = selectedCompetition;
    }
}