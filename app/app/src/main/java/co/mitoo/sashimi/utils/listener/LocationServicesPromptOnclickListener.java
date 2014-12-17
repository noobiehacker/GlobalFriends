package co.mitoo.sashimi.utils.listener;

import android.content.DialogInterface;
import android.content.Intent;

import co.mitoo.sashimi.utils.BusProvider;

/**
 * Created by david on 14-12-09.
 */
public class LocationServicesPromptOnclickListener implements DialogInterface.OnClickListener{

    private boolean startLocationService;

    public LocationServicesPromptOnclickListener(boolean startIntent){
        this.startLocationService = startIntent;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(this.startLocationService)
            BusProvider.post(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}
