package co.mitoo.sashimi.utils;
import se.walkercrou.places.Prediction;

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
        
        return getCityName();
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

    private String getCityName(){
        String description = getPrediciton().getDescription();
        int firstComma = description.indexOf(",");
        if(firstComma!=-1)
            return description.substring( 0 , firstComma);
        return description;
    }
}
