package co.mitoo.sashimi.utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import co.mitoo.sashimi.R;
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
            displayableDate = dataHelper.getMediumDateString(getFixtureDate());
        }
        return displayableDate;
    }

    public String getDisplayableTime(){
        if(displayableTime == null){
            if(getFixture().isTime_tbc())
                displayableTime  = getMitooActivity().getString(R.string.fixture_page_tbd);
            else{
                DataHelper dataHelper = getMitooActivity().getDataHelper();
                displayableTime =dataHelper.getDisplayableTimeString(getFixtureDate());
            }
        }
        return displayableTime;
    }

    public String getDisplayableScore(){
        if(displayableScore == null){
            result result = getFixture().getResult();
            if(result!=null)
                displayableScore = result.getHome_score() + result.getDelimiter() +result.getAway_score();
            else
                displayableScore = getMitooActivity().getString(R.string.fixture_page_tbd);
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

    public MitooEnum.FixtureStatus getFixtureType(){


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

        MitooEnum.FixtureStatus tabType;
        switch(getFixture().getStatus()){
            case 0:
                tabType = MitooEnum.FixtureStatus.SCORE;
                break;
            case 1:
                tabType = MitooEnum.FixtureStatus.CANCELED;
                break;
            case 2:
                tabType = MitooEnum.FixtureStatus.VOID;
                break;
            case 3:
                tabType = MitooEnum.FixtureStatus.POSTPONED;
                break;
            case 4:
                tabType = MitooEnum.FixtureStatus.RESCHEDULE;
                break;
            case 5:
                tabType = MitooEnum.FixtureStatus.ABANDONED;
                break;
            case 6:
                tabType = MitooEnum.FixtureStatus.VOID;
                break;
            default:
                tabType = MitooEnum.FixtureStatus.VOID;
                break;

        }
        return tabType;
    }

    public LatLng getLatLng(){

        LatLng result = null;
        location location = getFixture().getLocation();
        if(location!=null ){
            result = new LatLng(location.getLat(), location.getLng());
        }
        return result;

    }

    public String getDisplayableAddress(){
        String result = "";
        if(getFixture().getLocation()!=null){
            location location = getFixture().getLocation();
            result = location.getStreet_1() ;
        }
        return result;
    }

    private DataHelper getDataHelper(){
        return getMitooActivity().getDataHelper();
    }

}
