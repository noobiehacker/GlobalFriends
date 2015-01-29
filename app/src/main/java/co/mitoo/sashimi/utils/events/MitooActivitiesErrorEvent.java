package co.mitoo.sashimi.utils.events;

import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.StaticString;
import retrofit.RetrofitError;

/**
 * Created by david on 14-12-08.
 */
public class MitooActivitiesErrorEvent {

    private String errorMessage;
    private MitooEnum.ErrorType errorType;

    public MitooActivitiesErrorEvent(MitooEnum.ErrorType errorType) {
        this.errorType = errorType;
    }

    public MitooActivitiesErrorEvent(){
    }

    public MitooActivitiesErrorEvent(RetrofitError error){
        this.retrofitError = error;
    }

    public MitooActivitiesErrorEvent(String errorMessage){
        this.errorMessage=errorMessage;

    }

    public MitooActivitiesErrorEvent(MitooEnum.ErrorType errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        if(errorMessage==null)
            return StaticString.getString("errorMessage");
        else
            return errorMessage;
    }

    public RetrofitError retrofitError;

    public RetrofitError getRetrofitError() {
        return retrofitError;
    }

    public MitooEnum.ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(MitooEnum.ErrorType errorType) {
        this.errorType = errorType;
    }
}
