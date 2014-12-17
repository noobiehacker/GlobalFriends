package co.mitoo.sashimi.network;

import co.mitoo.sashimi.utils.BusProvider;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by david on 14-12-04.
 */
public class SteakErrorHandler implements ErrorHandler {
    @Override
    public Throwable handleError(RetrofitError cause) {
        Response r = cause.getResponse();
        BusProvider.post(cause);
        return cause;
    }
}
