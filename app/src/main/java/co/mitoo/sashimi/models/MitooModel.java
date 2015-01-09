package co.mitoo.sashimi.models;

import android.content.res.Resources;

import co.mitoo.sashimi.network.ServiceBuilder;
import co.mitoo.sashimi.network.SteakApi;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.StaticString;

/**
 * Created by david on 14-11-12.
 */
public abstract class MitooModel
{

    private Resources resources;
    private SteakApi steakApiService;

    public MitooModel(Resources resources) {

        this.resources = resources;
        BusProvider.register(this);
    }

    public SteakApi getSteakApiService() {
        if(steakApiService==null)
            steakApiService = new ServiceBuilder().setEndPoint(StaticString.steakStagingEndPoint)
                                                  .create(SteakApi.class);
        return steakApiService;
    }

    public void setSteakApiService(SteakApi steakApiService) {
        this.steakApiService = steakApiService;
    }

    protected void removeReferences(){
        BusProvider.unregister(this);
    }
}
