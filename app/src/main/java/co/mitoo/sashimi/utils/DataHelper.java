package co.mitoo.sashimi.utils;
import android.content.pm.PackageInfo;
import android.os.Build;
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
import co.mitoo.sashimi.models.FixtureModel;
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
    private int lastButtonClicked;

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

    public boolean isClickable(int buttonID) {
        //Only allow user to click if interval between two clicks is greater than 1 second
        boolean result = true;
        boolean elapsedIsTimeTooShort = SystemClock.elapsedRealtime() <= getLastCLickTime() + MitooConstants.durationLong;
        if (elapsedIsTimeTooShort && getLastButtonClicked() == buttonID ) {
            result = false;
        }
        setLastButtonClicked(buttonID);
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

    public String getRetinaURL(String url) {
        //only works if url is not null and it has one dot and more than three chracters

        String result = null;
        if(url!=null){

            int dotIndex = url.lastIndexOf('.');
            if (dotIndex >= 0 && url.length() > 3) {
                result = url.substring(0, dotIndex);
                result = result + "@2x";
                result = result + url.substring(dotIndex, url.length());
            }
        }

        //HACK to make logo display for now since rails prefix the logo with local host
        //Take out for produciton

        result = replaceLocalHostPrefix(result, StaticString.steakLocalEndPoint);

        return result;
    }

    public String parseDateToDisplayFormat(String input) {

        String result = input;
        try {
            if (result != null) {
                Date date = getLongestDateFormat().parse(input);
                result = getShortDateFormat().format(date);
            }
        } catch (Exception e) {
        }
        return result;
    }

    public Date getLongDateFromString(String input) {

        Date result = null;

        try {
            result = getLongestDateFormat().parse(input);
        } catch (Exception e) {

        }

        return result;
    }

    public String getLongDateString(Date date) {

        return getLongDisplayableDateFormat().format(date);
    }

    public String getMediumDateString(Date date) {

        return getMediumDisplayableDateFormat().format(date);
    }

    public String getDisplayableTimeString(Date date) {
        return getDisplayableTimeFormat().format(date);
    }

    public SimpleDateFormat getLongestDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public SimpleDateFormat getShortDateFormat() {
        return new SimpleDateFormat("MMM dd, yyyy");
    }

    public SimpleDateFormat getMediumDisplayableDateFormat() {
        return new SimpleDateFormat("EEEE, dd MMM");
    }

    public SimpleDateFormat getLongDisplayableDateFormat() {
        return new SimpleDateFormat("EEEE, d MMMM yyyy");
    }

    public SimpleDateFormat getDisplayableTimeFormat() {
        return new SimpleDateFormat("h:mm a");
    }


    public MitooEnum.TimeFrame getTimeFrame(Date date) {
        if (date.after(new Date()))
            return MitooEnum.TimeFrame.FUTURE;
        else
            return MitooEnum.TimeFrame.PAST;
    }

    public boolean isSameDate(Date itemOne, Date itemTwo) {

        if (itemOne == null || itemTwo == null)
            return false;
        else {
            return (itemOne.getDate() == itemTwo.getDate() &&
                    itemOne.getMonth() == itemTwo.getMonth() &&
                    itemOne.getYear() == itemTwo.getYear());

        }
    }

    public String getResetPageBadEmailMessage(String email) {

        String prefix = getActivity().getString(R.string.error_bad_email_prefix);
        String suffix = getActivity().getString(R.string.error_bad_email_suffix);
        return prefix + " " + email + " " + suffix;

    }

    public String removeSpaceAtEnd(String input) {

        String result = input;
        Boolean validInput = input != null && input.length() > 0;

        if (validInput && input.charAt(input.length() - 1) == 8203) {

            result = input.substring(0, input.length() - 1);

        }

        return result;
    }

    public String createSignUpInfo(String leagueName) {

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
        else if (layout ==R.layout.view_rainout_header)
            result = R.id.leagueMessage;
        return result;

    }

    public String getAlgoliaIndex() {

       return getActivity().getString(R.string.algolia_index);

    }

    public String getNewRelicKey() {

        return getActivity().getString(R.string.API_key_new_relic);

    }

    public float getFloatValue(int floatID) {
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



    public Team getTeam(int teamID) {
        return getActivity().getModelManager().getTeamModel().getTeam(teamID);
    }

    public Invitation_token getInvitationToken(JSONObject referringParams) {

        Invitation_token result = null;
        try {
            /*JsonNode node = getObjectMapper().valueToTree(referringParams);
            result = getObjectMapper().readValue(new TreeTraversingParser(node) ,Invitation_token.class);*/
            result = getObjectMapper().readValue(referringParams.toString(), Invitation_token.class);
        } catch (Exception e) {
        }
        return result;

    }

    //NOT TESTED, not gonna work
    public List<League> getListOfLeauges(JSONObject leagueListJson) {

        List<League> result = null;
        try {
            JSONArray hits = leagueListJson.getJSONArray(getActivity().getString(R.string.algolia_result_param));
            JsonNode node = getObjectMapper().valueToTree(hits);
            result = getObjectMapper().readValue(new TreeTraversingParser(node), new TypeReference<List<League>>() {
            });
        } catch (Exception e) {

        }
        return result;
    }

    private String replaceLocalHostPrefix(String url, String newPrefix) {

        String result = url;
        if (url != null) {
            int index = url.lastIndexOf("3000");
            if (index >= 0)
                result = newPrefix + url.substring(index + 5, url.length());
        }
        return result;
    }

    public ObjectMapper getObjectMapper() {
        if (objectMapper == null)
            objectMapper = new ObjectMapper();
        return objectMapper;
    }

    public void addLeagueObjToCompetition(Competition[] competitions, League league) {
        for (Competition comp : competitions) {
            comp.setLeague(league);
        }
    }

    public String serializeObject(Object input) {

        String serializedValue = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            serializedValue = objectMapper.writeValueAsString(input);
        } catch (Exception e) {
        }
        return serializedValue;
    }

    public <T> T deserializeObject(String input, Class<T> classType) {
        T deserializedObject = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            deserializedObject = objectMapper.readValue(input, classType);
        } catch (Exception e) {
            String temp = e.toString();
        }
        return deserializedObject;
    }

    public String getOSVersion(){

        return Build.VERSION.RELEASE;

    }

    public String getAppVersion(){

        String result = "";
        try{
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            result = info.versionName;
        }catch(Exception e){
        }
        return result;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public String getPlatformName(){

        return getActivity().getString(R.string.platform_name);
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public void setUpFixtureSection(List<FixtureModel> list) {

        //Assumption: List is already sorted
        //Run a loop thought the list and for the first fixture for a particular date
        //We set the variable "firstFixtureForDateGroup" to true

        if(list!=null && !list.isEmpty()) {

            Date currentSectionDate =null;
            for (FixtureModel item : list) {
                if(currentSectionDate==null || !isSameDate(currentSectionDate , item.getFixtureDate())){

                    item.setFirstFixtureForDateGroup(true);
                    currentSectionDate= item.getFixtureDate();
                }
            }
        }
    }

    public int getLastButtonClicked() {
        return lastButtonClicked;
    }

    public void setLastButtonClicked(int lastButtonClicked) {
        this.lastButtonClicked = lastButtonClicked;
    }

    public String getCityNameFromString(String item){
        String result="";
        if(item!=null && item.length()>0){
            int index = item.indexOf(',');
            if(index >= 0)
                result= item.substring(0 , index);
            else
                result= item;
        }
        return result;
    }
}