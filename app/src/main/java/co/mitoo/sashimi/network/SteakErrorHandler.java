package co.mitoo.sashimi.network;

import co.mitoo.sashimi.utils.BusProvider;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Created by david on 14-12-04.
 */
public class SteakErrorHandler implements ErrorHandler {

    @Override
    public Throwable handleError(RetrofitError cause) {
        if(throwError(cause))
            BusProvider.post(cause);
        return cause;
    }

    private boolean throwError(RetrofitError cause){
        return true;
    }

}
