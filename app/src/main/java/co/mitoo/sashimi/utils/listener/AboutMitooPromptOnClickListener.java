package co.mitoo.sashimi.utils.listener;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by david on 15-01-20.
 */
public class AboutMitooPromptOnClickListener  extends MitooDialogOnClickListener {

    public AboutMitooPromptOnClickListener(boolean startIntent, Context context) {
        super(startIntent, context);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(isPositiveListener()){

        }
    }
}
