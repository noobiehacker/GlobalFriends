package co.mitoo.sashimi.utils.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import co.mitoo.sashimi.utils.BusProvider;

/**
 * Created by david on 14-12-09.
 */
public class LocationServicesPromptOnclickListener extends MitooDialogOnClickListener {

    public LocationServicesPromptOnclickListener(boolean startIntent, Context context) {
        super(startIntent, context);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(isPositiveListener())
            BusProvider.post(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}
