package co.mitoo.sashimi.models;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Fixture;
import co.mitoo.sashimi.models.jsonPojo.Result;
import co.mitoo.sashimi.models.jsonPojo.location;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.views.activities.MitooActivity;

import org.joda.time.LocalDate;

/**
 * Created by david on 15-03-11.
 */
public class FixtureModel implements Comparable<FixtureModel>{

    private Fixture fixture;
    private MitooActivity mitooActivity;
    private Date fixtureDate;
    private LocalDate jodafixtureDate;
    private String displayableTime;
    private String displayableScore;
    private String displayablePlace;
    private boolean firstFixtureForDateGroup;

    public FixtureModel(Fixture fixture, MitooActivity activity) {
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
    public int compareTo(FixtureModel another) {
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
        DataHelper dataHelper = getMitooActivity().getDataHelper();
        return dataHelper.getLongDateString(getFixtureDate());
    }

    public String getMediumDisplayableDate() {
        DataHelper dataHelper = getMitooActivity().getDataHelper();
        return dataHelper.getMediumDateString(getFixtureDate());
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
            Result Result = getFixture().getResult();
            if(Result !=null)
                displayableScore = Result.getHome_score() + Result.getDelimiter() + Result.getAway_score();
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
                tabType = MitooEnum.FixtureStatus.RESCHEDULED;
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
            List<String> addressStrings = new ArrayList<String>();

            location location = getFixture().getLocation();
            addStringToList(location.getStreet_1() , addressStrings);
            addStringToList(location.getCity() , addressStrings);
            addStringToList(location.getState() , addressStrings);
            addStringToList(location.getPostal_code() , addressStrings);

            result = formatString(addressStrings);

        }
        return result;
    }

    private void addStringToList(String string, List<String> list){
        if(string!=null)
            list.add(string);
    }

    private String formatString(List<String> list) {

        String result = "";
        boolean firstString = true;

        for (String item : list) {
            if (!firstString)
                item = " " + item;
            result = result + item;
            firstString = false;
        }

        return result;
    }

    private DataHelper getDataHelper(){
        return getMitooActivity().getDataHelper();
    }

    public boolean isFirstFixtureForDateGroup() {
        return firstFixtureForDateGroup;
    }

    public void setFirstFixtureForDateGroup(boolean firstFixtureForDateGroup) {
        this.firstFixtureForDateGroup = firstFixtureForDateGroup;
    }
}
