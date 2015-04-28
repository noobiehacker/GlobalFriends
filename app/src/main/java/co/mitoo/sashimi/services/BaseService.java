package co.mitoo.sashimi.services;

import android.os.Handler;

import co.mitoo.sashimi.network.DataPersistanceService;
import co.mitoo.sashimi.network.ServiceBuilder;
import co.mitoo.sashimi.network.SteakApi;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.views.activities.MitooActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 15-04-23.
 */
public abstract class BaseService {

    private SteakApi steakApiService;

    public BaseService() {

        BusProvider.register(this);

    }

    public SteakApi getSteakApiService() {
        if (steakApiService == null)
            steakApiService = ServiceBuilder.getSingleTonInstance().getSteakApiService();
        return steakApiService;
    }

    public void setSteakApiService(SteakApi steakApiService) {
        this.steakApiService = steakApiService;
    }

    protected void removeReferences() {
        BusProvider.unregister(this);
    }


}