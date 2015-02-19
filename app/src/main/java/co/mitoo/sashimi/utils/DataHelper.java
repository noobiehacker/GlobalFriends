package co.mitoo.sashimi.utils;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import com.google.android.gms.maps.model.LatLng;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Sport;
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

    public boolean IsValid(LatLng latLng) {

        return (latLng.latitude != MitooConstants.invalidConstant) ||
                (latLng.longitude != MitooConstants.invalidConstant);

    }

    public boolean validName(String input) {
        boolean result = false;
        result = stringLengthBetween(input, 3, 100);
        return result;
    }

    public boolean validEmail(String input) {
        boolean result = false;
        result = stringLengthBetween(input, 5, 100) && validEmailString(input);
        return result;
    }

    public boolean validPhone(String input) {
        boolean result = false;
        result = stringLengthBetween(input, 10, 11) && hasNumOfDigits(input, 10);
        return result;
    }

    public boolean validPassword(String input) {
        boolean result = false;
        result = stringLengthBetween(input, 8, 100);
        return result;
    }

    private boolean validEmailString(String input) {

        boolean result = false;
        if (input.contains("@") && input.contains(".")) {
            result = input.indexOf("@") < input.indexOf(".");
        }
        return result;

    }

    private boolean hasNumOfDigits(String input, int digitNum) {

        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            char value = input.charAt(i);
            if (value >= '0' && value <= '9')
                count++;
        }
        return count >= digitNum;

    }

    private boolean stringLengthBetween(String input, int lowerEnd, int higherEnd) {

        return input.length() >= lowerEnd && input.length() <= higherEnd;

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
        
        return result;
    }

    public boolean isConfirmFeedBackPopped() {
        return confirmFeedBackPopped;
    }
    
    public String parseDate(String input) {

        String result = input;
        try {
            if (result != null) {
                Date date = getLongDateFormat().parse(input);
                result = getShortDateFormat().format(date);
            }
        } catch (Exception e) {
            String temp = e.toString();
        }
        return result;
    }


    public String getDateString(Date date) {

        return getLongDateFormat().format(date);
    }
    
    public SimpleDateFormat getLongDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        
    }

    public SimpleDateFormat getShortDateFormat(){
        return new SimpleDateFormat("MMM dd, yyyy");
    }



        
}
