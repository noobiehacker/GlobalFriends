package co.mitoo.sashimi.utils;

import android.content.Context;
import android.os.SystemClock;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Sport;
import se.walkercrou.places.Prediction;

/**
 * Created by david on 15-01-27.
 */
public class DataHelper {
    
    private Context context;
    private long lastCLickTime=0;
    private boolean confirmFeedBackPopped;

    public DataHelper(Context context) {
        this.context = context;
    }

    public <T> void addToListList(List<T> container ,List<T> additionList){
        for(T item : additionList){
            container.add(item);
        }
    }

    public <T> void clearList(List<T> result){
        if(result!=null){
            Iterator<T> iterator = result.iterator();
            while(iterator.hasNext()){
                iterator.next();
                iterator.remove();
            }
        }
    }

    public List<IsSearchable> getSports() {

        ArrayList<IsSearchable> returnList = new ArrayList<IsSearchable>();
        String[] sportsArray = getContext().getResources().getStringArray(R.array.sports_array);
        for (String item : sportsArray) {
            returnList.add(new Sport(item));
        }
        return returnList;

    }

    public List<IsSearchable> getSports(String prefix) {

        ArrayList<IsSearchable> returnList = new ArrayList<IsSearchable>();
        String[] sportsArray = getContext().getResources().getStringArray(R.array.sports_array);
        for (String item : sportsArray) {
            if (item.toLowerCase().startsWith(prefix.toLowerCase()))
                returnList.add(new Sport(item));
        }
        return returnList;

    }

    public boolean IsValid(LatLng latLng){
        
        return (latLng.latitude !=MitooConstants.invalidConstant) || 
                (latLng.longitude !=MitooConstants.invalidConstant);

    }

    public boolean validName(String input){
        boolean result= false;
        result = stringLengthBetween(input, 3, 100);
        return result;
    }

    public boolean validEmail(String input){
        boolean result= false;
        result = stringLengthBetween(input, 5, 100) && validEmailString(input);
        return result;
    }

    public boolean validPhone(String input){
        boolean result= false;
        result = stringLengthBetween(input, 10, 11) && hasNumOfDigits(input, 10);
        return result;
    }

    public boolean validPassword(String input){
        boolean result= false;
        result = stringLengthBetween(input, 8, 100);
        return result;
    }
    
    private boolean validEmailString(String input){
        
        boolean result = false;
        if(input.contains("@") && input.contains(".")){
            result =  input.indexOf("@") < input.indexOf(".");
        }
        return result;
        
    }

    private boolean hasNumOfDigits(String input , int digitNum){

        int count = 0;
        for(int i = 0 ; i < input.length() ; i++){
            char value = input.charAt(i);
            if(value>= '0' && value <='9')
                count++;
        }
        return count >= digitNum;

    }
    
    private boolean stringLengthBetween(String input, int lowerEnd, int higherEnd){
        
        return input.length() >=lowerEnd && input.length() <= higherEnd;
        
    }

    public void removeNonCity(List<Prediction> predictions){

        Iterator<Prediction> itr = predictions.iterator();
        while(itr.hasNext()){
            Prediction item = itr.next();
            if(!predictionIsPlace(item))
                itr.remove();
        }
    }
    
    private boolean predictionIsPlace(Prediction prediction) {

        boolean result = false;
        
        List<String> types = prediction.getTypes();
        if(types.size() >0){
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
    
    private boolean isGoogleGeoType(String typeString){

        String geoCode = getContext().getString(R.string.google_place_api_geocode);
        String locality = getContext().getString(R.string.google_place_api_locality);
        String political = getContext().getString(R.string.google_place_api_political);
        return (typeString.equals(geoCode) || typeString.equals(locality) || typeString.equals(political));

    }

    private String getBullet(){
        return getContext().getString(R.string.bullet);

    }
    
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public long getLastCLickTime() {
        return lastCLickTime;
    }

    public void setLastCLickTime(long lastCLickTime) {
        this.lastCLickTime = lastCLickTime;
    }
    
    public boolean isClickable(){
        //Only allow user to click if interval between two clicks is greater than 1 second
        boolean result = false;
        if(SystemClock.elapsedRealtime() > getLastCLickTime()+1000){
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
}
