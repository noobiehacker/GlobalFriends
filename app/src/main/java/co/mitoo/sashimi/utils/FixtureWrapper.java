package co.mitoo.sashimi.utils;
import java.util.Date;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.models.jsonPojo.result;
import org.joda.time.LocalDate;

/**
 * Created by david on 15-03-11.
 */
public class FixtureWrapper implements Comparable<FixtureWrapper>{

    private Fixture fixture;
    private MitooActivity mitooActivity;
    private Date fixtureDate;
    private LocalDate jodafixtureDate;
    private String displayableDate;
    private String displayableTime;
    private String displayableScore;

    public FixtureWrapper(Fixture fixture , MitooActivity activity) {
        this.fixture= fixture;
        this.mitooActivity = activity;
        initializeNullParam();
    }

    public Date getFixtureDate() {
        if(fixtureDate == null){
            fixtureDate = getMitooActivity().getDataHelper().getDateFromString(getFixture().getLocal_time());
            if(fixtureDate == null){
                fixtureDate = new Date();
            }
        }
        return fixtureDate;
    }

    public LocalDate getJodafixtureDate() {
        if(jodafixtureDate==null){
            jodafixtureDate = LocalDate.fromDateFields(getFixtureDate());
        }
        return jodafixtureDate;
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

    private void initializeNullParam(){
        if(this.getFixture()!=null){
            if(this.getFixture().getSport()==null)
                this.getFixture().setSport("");
        }
    }

    public boolean isFutureFixture(){
        Date now = new Date();
        return getFixtureDate().after(now);

    }

}
