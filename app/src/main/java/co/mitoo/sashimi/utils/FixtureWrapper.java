package co.mitoo.sashimi.utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.models.jsonPojo.location;
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
    private String displayablePlace;

    public FixtureWrapper(Fixture fixture , MitooActivity activity) {
        this.fixture= fixture;
        this.mitooActivity = activity;
        initializeNullParam();
    }

    public Date getFixtureDate() {
        if(fixtureDate == null){
            fixtureDate = getMitooActivity().getDataHelper().getLongDateFromString(getFixture().getLocal_time());
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

    public String getLongDisplayableDate() {
        if(displayableDate== null) {
            DataHelper dataHelper = getMitooActivity().getDataHelper();
            displayableDate = dataHelper.getLongDateString(getFixtureDate());
        }
        return displayableDate;
    }

    public String getMediumDisplayableDate() {
        if(displayableDate== null) {
            DataHelper dataHelper = getMitooActivity().getDataHelper();
            displayableDate = dataHelper.getMdeiumDateString(getFixtureDate());
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

    public String getDisplayablePlace(){
        if(displayablePlace == null){
            location location = getFixture().getLocation();
            if(location!=null)
                displayablePlace = location.getTitle();
        }
        return displayablePlace;
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

    public MitooEnum.FixtureRowType getFixtureType(){

        /*Notes from BE

            The status attribute is a variable to show non-normal games
            0 = Normal
            1 = Cancelled
            2 = Deleted
            3 = Postponed
            4 = Rescheduled
            5 = Abandoned
            6 = Void Notes:

         */

        MitooEnum.FixtureRowType rowType;
            switch(getFixture().getStatus()){
                case 0:
                    MitooEnum.TimeFrame fixtureTimeFrame = getDataHelper().getTimeFrame(getFixtureDate());
                    if(fixtureTimeFrame == MitooEnum.TimeFrame.FUTURE)
                        rowType = MitooEnum.FixtureRowType.TIME;
                    else{
                        if(getFixture().getResult()==null)
                            rowType = MitooEnum.FixtureRowType.TBC;
                        else
                            rowType = MitooEnum.FixtureRowType.SCORE;
                    }
                    break;
                case 1:
                    rowType = MitooEnum.FixtureRowType.CANCELED;
                    break;
                case 2:
                    rowType = MitooEnum.FixtureRowType.VOID;
                    break;
                case 3:
                    rowType = MitooEnum.FixtureRowType.POSTPONED;
                    break;
                case 4:
                    rowType = MitooEnum.FixtureRowType.RESCHEDULE;
                    break;
                case 5:
                    rowType = MitooEnum.FixtureRowType.ABANDONED;
                    break;
                case 6:
                    rowType = MitooEnum.FixtureRowType.VOID;
                    break;
                default:
                    rowType = MitooEnum.FixtureRowType.TBC;
                    break;

        }
        return rowType;
    }

    public LatLng getLatLng(){

        LatLng result = null;
        location location = getFixture().getLocation();
        if(location!=null ){
            result = new LatLng(location.getLat(), location.getLng());
        }
        return result;

    }

    private DataHelper getDataHelper(){
        return getMitooActivity().getDataHelper();
    }

}
