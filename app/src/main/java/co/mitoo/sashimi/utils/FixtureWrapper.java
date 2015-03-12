package co.mitoo.sashimi.utils;
import java.util.Date;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.views.activities.MitooActivity;

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
    private String homeTeamName;
    private String awayTeamName;

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
        return getFixtureDate().compareTo(another.fixtureDate);
    }

    public MitooActivity getMitooActivity() {
        return mitooActivity;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public String getDisplayableDate() {
        if(displayableDate== null)
            displayableDate = getMitooActivity().getDataHelper().getDateString(getFixtureDate());
        return displayableDate;
    }

    public String getDisplayableTime(){
        if(displayableTime == null){
        }
        return displayableTime;
    }

    public String getDisplayableScore(){
        if(displayableScore == null){
        }
        return displayableScore;
    }

    public String getHomeTeamName(){
        if(homeTeamName== null){
        }
        return homeTeamName;
    }

    public String getAwayTeamName(){
        if(awayTeamName == null){
        }
        return awayTeamName;
    }
}
