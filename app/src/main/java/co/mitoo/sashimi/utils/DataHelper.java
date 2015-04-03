package co.mitoo.sashimi.utils;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.Invitation_token;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.Sport;
import co.mitoo.sashimi.models.jsonPojo.Team;
import co.mitoo.sashimi.views.activities.MitooActivity;
import se.walkercrou.places.Prediction;

/**
 * Created by david on 15-01-27.
 */
public class DataHelper {

    private MitooActivity activity;
    private long lastCLickTime = 0;
    private boolean confirmFeedBackPopped;
    private DisplayMetrics metrics;
    private ObjectMapper objectMapper;

    public DataHelper(MitooActivity activity) {
        this.activity = activity;
    }

    public <T> void addToListList(List<T> container, List<T> additionList) {
        for (T item : additionList) {
            container.add(item);
        }
    }

    public <T> void clearList(List<T> result) {
        if (result != null) {
            Iterator<T> iterator = result.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
    }

    public List<IsSearchable> getSports() {

        ArrayList<IsSearchable> returnList = new ArrayList<IsSearchable>();
        String[] sportsArray = getActivity().getResources().getStringArray(R.array.sports_array);
        for (String item : sportsArray) {
            returnList.add(new Sport(item));
        }
        return returnList;

    }

    public List<IsSearchable> getSports(String prefix) {

        ArrayList<IsSearchable> returnList = new ArrayList<IsSearchable>();
        String[] sportsArray = getActivity().getResources().getStringArray(R.array.sports_array);
        for (String item : sportsArray) {
            if (item.toLowerCase().startsWith(prefix.toLowerCase()))
                returnList.add(new Sport(item));
        }
        return returnList;

    }

    public boolean IsValidLatLng(LatLng latLng) {

        return (latLng.latitude != MitooConstants.invalidConstant) ||
                (latLng.longitude != MitooConstants.invalidConstant);

    }

    public void removeNonCity(List<Prediction> predictions) {

        Iterator<Prediction> itr = predictions.iterator();
        while (itr.hasNext()) {
            Prediction item = itr.next();
            if (!predictionIsPlace(item))
                itr.remove();
        }
    }

    private boolean predictionIsPlace(Prediction prediction) {

        boolean result = false;

        List<String> types = prediction.getTypes();
        if (types.size() > 0) {
            int correctTerms = 0;
            forloop:

            for (String typeString : types) {
                if (isGoogleGeoType(typeString))
                    correctTerms++;
                if (correctTerms == 3)
                    result = true;
                if (result)
                    break forloop;
            }
        }

        return result;

    }

    private boolean isGoogleGeoType(String typeString) {

        String geoCode = getActivity().getString(R.string.google_place_api_geocode);
        String locality = getActivity().getString(R.string.google_place_api_locality);
        String political = getActivity().getString(R.string.google_place_api_political);
        return (typeString.equals(geoCode) || typeString.equals(locality) || typeString.equals(political));

    }

    private String getBullet() {
        return getActivity().getString(R.string.bullet);

    }

    public MitooActivity getActivity() {
        return activity;
    }

    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }

    public long getLastCLickTime() {
        return lastCLickTime;
    }

    public void setLastCLickTime(long lastCLickTime) {
        this.lastCLickTime = lastCLickTime;
    }

    public boolean isClickable() {
        //Only allow user to click if interval between two clicks is greater than 1 second
        boolean result = false;
        if (SystemClock.elapsedRealtime() > getLastCLickTime() + 1000) {
            result = true;
        }
        setLastCLickTime(SystemClock.elapsedRealtime());
        return result;
    }

    public boolean feedBackHasAppeared() {
        return confirmFeedBackPopped;
    }

    public void setConfirmFeedBackPopped(boolean confirmFeedBackPopped) {
        this.confirmFeedBackPopped = confirmFeedBackPopped;
    }

    public DisplayMetrics getMetrics() {
        if (metrics == null)
            metrics = getActivity().getResources().getDisplayMetrics();
        return metrics;
    }

    public void setMetrics(DisplayMetrics metrics) {
        this.metrics = metrics;
    }

    public boolean isHighDenstiryScreen() {
        boolean result = false;
        if(getMetrics().densityDpi > DisplayMetrics.DENSITY_HIGH)
            result=true;
        return result;
    }

    public String getRetinaURL(String url){
        //only works if url is not null and it has one dot and more than three chracters
        String result = "";
        if(url!=null){

            int dotIndex=url.lastIndexOf('.');
            if(dotIndex>=0 && url.length()>3){
                result= url.substring(0 , dotIndex);
                result= result + "@2x";
                result= result + url.substring(dotIndex, url.length());
            }
        }

        //HACK to make logo display for now since rails prefix the logo with local host
        //Take out for produciton

        result =replaceLocalHostPrefix(result ,StaticString.steakLocalEndPoint );

        return result;
    }

    public boolean isConfirmFeedBackPopped() {
        return confirmFeedBackPopped;
    }

    public String parseDateToDisplayFormat(String input) {

        String result = input;
        try {
            if (result != null) {
                Date date = getOldLongDateFormat().parse(input);
                result = getShortDateFormat().format(date);
            }
        } catch (Exception e) {
        }
        return result;
    }

    public Date getLongDateFromString(String input) {

        Date result = null;

        try {
            result = getOldLongDateFormat().parse(input);
        } catch (Exception e) {

        }

        return result;
    }

    public String getMdeiumDateString(Date date) {

        return getMediumDisplayableDateFormat().format(date);
    }

    public String getLongDateString(Date date) {

        return getLongDisplayableDateFormat().format(date);
    }

    public String getDisplayableTimeString(Date date) {

        return getDisplayableTimeFormat().format(date);

    }

    public SimpleDateFormat getOldLongDateFormat(){

        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    }

    public SimpleDateFormat getNewLongDateFormat(){

        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    }

    public SimpleDateFormat getShortDateFormat(){
        return new SimpleDateFormat("MMM dd, yyyy");
    }

    public SimpleDateFormat getMediumDisplayableDateFormat(){
        return new SimpleDateFormat("EEEE, dd MMM");
    }

    public SimpleDateFormat getLongDisplayableDateFormat(){
        return new SimpleDateFormat("EEEE, dd MMMM yyyy");
    }

    public SimpleDateFormat getDisplayableTimeFormat(){
        return new SimpleDateFormat("h:mm a");
    }


    public MitooEnum.TimeFrame getTimeFrame(Date date){
        if(date.after(new Date()))
            return MitooEnum.TimeFrame.FUTURE;
        else
            return MitooEnum.TimeFrame.PAST;
    }

    public boolean isSameDate(Date itemOne , Date itemTwo) {

        if (itemOne == null || itemTwo == null)
            return false;
        else {
            return (itemOne.getDate() == itemTwo.getDate() &&
                    itemOne.getMonth() == itemTwo.getMonth() &&
                    itemOne.getYear() == itemTwo.getYear());

        }
    }

    public String getResetPageBadEmailMessage(String email){

        String prefix= getActivity().getString(R.string.error_bad_email_prefix);
        String suffix= getActivity().getString(R.string.error_bad_email_suffix);
        return prefix + " " + email + " " + suffix;

    }

    public String removeSpaceAtEnd(String input){

        String result = input;
        Boolean validInput = input!=null && input.length()>0;

        if(validInput  && input.charAt(input.length()-1) == 8203 ){

            result = input.substring(0 , input.length()-1);

        }

        return result;
    }

    public String createSignUpInfo(String leagueName){

        String joinPagePrefix = getActivity().getString(R.string.join_page_info_prefix);
        String joinPageSuffix = getActivity().getString(R.string.join_page_info_suffix);
        return joinPagePrefix + "\n" + leagueName + " " + joinPageSuffix;

    }

    public boolean isBundleArgumentTrue(Object argument) {

        if (argument != null) {
            String stringArgument = argument.toString();
            if (stringArgument.equals(getActivity().getString(R.string.bundle_value_true)))
                return true;
        }
        return false;
    }

    public int getTextViewIDFromLayout(int layout) {

        int result = MitooConstants.invalidConstant;
        if (layout == R.layout.view_list_header)
            result = R.id.header_view;
        else if (layout == R.layout.view_league_list_footer)
            result = R.id.footer_view;
        return result;

    }

    public String getAlgoliaIndex(){

        String result = "";

        switch(MitooConstants.appEnvironment){
            case PRODUCTION:
                result = getActivity().getString(R.string.algolia_production_index);
                break;
            default:
                result = getActivity().getString(R.string.algolia_staging_index);
                break;
        }

        return result;

    }

    public String getNewRelicKey(){

        String result = "";

        switch(MitooConstants.appEnvironment){
            case PRODUCTION:
                result = getActivity().getString(R.string.API_key_new_relic_production);
                break;
            case STAGING:
                result = getActivity().getString(R.string.API_key_new_relic_staging);
                break;
            default:
                result = getActivity().getString(R.string.API_key_new_relic_staging);
                break;
        }

        return result;
    }

    public float getFloatValue(int floatID){
        TypedValue outValue = new TypedValue();
        getActivity().getResources().getValue(floatID, outValue, true);
        return outValue.getFloat();
    }

    public MitooEnum.FixtureTabType getFixtureTabTypeFromIndex(int index) {

        MitooEnum.FixtureTabType tabType;
        switch (index) {
            case 0:
                tabType = MitooEnum.FixtureTabType.FIXTURE_SCHEDULE;
                break;
            default:
                tabType = MitooEnum.FixtureTabType.FIXTURE_RESULT;
                break;
        }
        return tabType;
    }



    public String getNotificationText(MitooEnum.NotificationType notificationType){

        String result="";
        switch(notificationType){

            case NextGame:
                result = getActivity().getString(R.string.notification_page_list_item_next_game);
                break;
            case TeamResults:
                result = getActivity().getString(R.string.notification_page_list_item_your_results);
                break;
            case RivalResults:
                result = getActivity().getString(R.string.notification_page_list_rival_results);
                break;
            default:
                break;

        }
        return result;
    }

    public Team getTeam(int teamID){
        return getActivity().getModelManager().getTeamModel().getTeam(teamID);
    }

    public Invitation_token getInvitationToken(JSONObject referringParams){

        Invitation_token result = null;
        try{
            /*JsonNode node = getObjectMapper().valueToTree(referringParams);
            result = getObjectMapper().readValue(new TreeTraversingParser(node) ,Invitation_token.class);*/
            result = getObjectMapper().readValue(referringParams.toString(), Invitation_token.class);
        }catch(Exception e){
        }
        return result;

    }

    //NOT TESTED, not gonna work
    public List<League> getListOfLeauges(JSONObject leagueListJson){

        List<League> result = null;
        try{
            JSONArray hits = leagueListJson.getJSONArray(getActivity().getString(R.string.algolia_result_param));
            JsonNode node = getObjectMapper().valueToTree(hits);
            result = getObjectMapper().readValue(new TreeTraversingParser(node) ,new TypeReference<List<League>>(){});
        }catch(Exception e){

        }
        return result;
    }
    private String replaceLocalHostPrefix(String url , String newPrefix) {

        String result = url;
        int index = url.lastIndexOf("3000");
        if (index >= 0)
            result = newPrefix + url.substring(index + 5, url.length());
        return result;
    }

    public ObjectMapper getObjectMapper() {
        if(objectMapper == null)
            objectMapper = new ObjectMapper();
        return objectMapper;
    }

    public void addLeagueObjToCompetition(Competition[] competitions , League league){
        for(Competition comp : competitions){
            comp.setLeague(league);
        }
    }

    public MitooEnum.FixtureRowType getFixtureRowTypeFixture(FixtureWrapper fixtureWrapper){

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

        MitooEnum.FixtureRowType tabType;
            switch(fixtureWrapper.getFixture().getStatus()){
                case 0:
                    tabType = MitooEnum.FixtureRowType.SCORE;
                    break;
                case 1:
                    tabType = MitooEnum.FixtureRowType.CANCELED;
                    break;
                case 2:
                    tabType = MitooEnum.FixtureRowType.VOID;
                    break;
                case 3:
                    tabType = MitooEnum.FixtureRowType.POSTPONED;
                    break;
                case 4:
                    tabType = MitooEnum.FixtureRowType.RESCHEDULE;
                    break;
                case 5:
                    tabType = MitooEnum.FixtureRowType.ABANDONED;
                    break;
                case 6:
                    tabType = MitooEnum.FixtureRowType.VOID;
                    break;
                default:
                    tabType = MitooEnum.FixtureRowType.VOID;
                    break;

            }
        return tabType;
    }
}