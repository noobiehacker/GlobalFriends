package co.mitoo.sashimi.utils.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;

/**
 * Created by david on 15-01-20.
 */
public class AboutMitooPromptOnClickListener extends MitooOptionsDialogOnClickListner{

    public AboutMitooPromptOnClickListener(boolean startIntent, Context context) {
        super(startIntent, context);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (isPositiveListener()) {

            Bundle bundle = new Bundle();
            bundle.putString(getContext().getString(R.string.bundle_key_prompt), String.valueOf(getSelectedOption()));
            FragmentChangeEvent event = new FragmentChangeEvent(this, MitooEnum.FragmentTransition.PUSH, R.id.fragment_about_mitoo, bundle);
            BusProvider.post(event);

        }

        dialog.dismiss();
    }
}
