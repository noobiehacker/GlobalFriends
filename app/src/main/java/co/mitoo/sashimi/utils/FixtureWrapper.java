package co.mitoo.sashimi.utils;
import java.util.Date;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.models.jsonPojo.result;
/**
 * Created by david on 15-03-11.
 */
public class FixtureWrapper implements Comparable<FixtureWrapper>{

    private Fixture fixture;
    private MitooActivity mitooActivity;
    private Date fixtureDate;
    private String displayableDate;
    private String displayableTime;
    private String displayableScore;

    public FixtureWrapper(Fixture fixture , MitooActivity activity) {
        this.fixture= fixture;
        this.mitooActivity = activity;
    }

    public Date getFixtureDate() {
        if(fixtureDate == null){
            fixtureDate = getMitooActivity().getDataHelper().getDateFromString(getFixture().getTime());
        }
        return fixtureDate;
    }

    @Override
    public int compareTo(FixtureWrapper another) {
        if(getFixtureDate()== null)
            return -1;
        else if(another.getFixtureDate() == null)
            return 1;
        return getFixtureDate().compareTo(another.getFixtureDate());
    }

    public MitooActivity getMitooActivity() {
        return mitooActivity;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public String getDisplayableDate() {
        if(displayableDate== null) {
            DataHelper dataHelper = getMitooActivity().getDataHelper();
            displayableDate = dataHelper.getDisplayableDateString(getFixtureDate());
        }
        return displayableDate;
    }

    public String getDisplayableTime(){
        if(displayableTime == null){
            DataHelper dataHelper = getMitooActivity().getDataHelper();
            displayableTime =dataHelper.getDisplayableTimeString(getFixtureDate());
        }
        return displayableTime;
    }

    public String getDisplayableScore(){
        if(displayableScore == null){
            result result = getFixture().getResult();
            if(result!=null)
                displayableScore = result.getHome_score() + result.getDelimiter() +result.getAway_score();
        }
        return displayableScore;
    }

}
