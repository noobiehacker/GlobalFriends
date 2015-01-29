package co.mitoo.sashimi.utils;
import se.walkercrou.places.Prediction;
import org.apache.commons.lang.StringUtils;


/**
 * Created by david on 15-01-27.
 */
public class PredictionWrapper extends Prediction implements IsSearchable {

    private Prediction prediciton;
    
    public PredictionWrapper(Prediction predicton){
        this.prediciton=predicton;
        
    }

    public Prediction getPrediciton() {
        return prediciton;
    }

    public void setPrediciton(Prediction prediciton) {
        this.prediciton = prediciton;
    }

    @Override
    public String getName() {
        
        return getCityFromAddress(getPrediciton().getDescription());
    }
    
    @Override
    public boolean equals(Object item){

        boolean result= false;
        if(item instanceof PredictionWrapper){
            PredictionWrapper castedObject = (PredictionWrapper) item;
            result = castedObject.getPlaceId().equals(getItemID());
        }
        return result;
    }

    @Override
    public String getItemID() {
        return getPrediciton().getPlaceId();
    }

    private String getCityFromAddress(String address){
        
        int occurance = StringUtils.countMatches(address , ",");
        
        String cityName = address;
        switch(occurance){
            
            case 0:
                cityName = address;
                break;
            case 1:         
                int firstComma = address.indexOf(",");
                cityName = address.substring( 0 , firstComma);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                int secondComma = StringUtils.ordinalIndexOf(address, ",", 2) ;
                cityName = address.substring( 0 , secondComma);
                break;
            default: cityName = address;
                
        }
        return cityName;
        
    }
}
