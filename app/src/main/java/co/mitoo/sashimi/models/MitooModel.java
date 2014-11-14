package co.mitoo.sashimi.models;

import android.content.res.Resources;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.network.ServiceAdapter;
import co.mitoo.sashimi.network.SteakApiService;

/**
 * Created by david on 14-11-12.
 */
public abstract class MitooModel
{

    private Resources resources;
    private SteakApiService apiService;

    public MitooModel(Resources resources) {

        this.resources = resources;

    }

    protected SteakApiService getSteakApiService(){
        return new ServiceAdapter.Builder()
                .setEndPoint(resources.getString(R.string.steak_end_point_url))
                .build()
                .create(SteakApiService.class);
    }

}
