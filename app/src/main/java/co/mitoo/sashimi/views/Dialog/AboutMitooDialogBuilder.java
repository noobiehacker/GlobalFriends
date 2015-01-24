package co.mitoo.sashimi.views.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.listener.AboutMitooPromptOnClickListener;
import co.mitoo.sashimi.utils.listener.FeedBackPromptOnClickListener;

/**
 * Created by david on 15-01-20.
 */
public class AboutMitooDialogBuilder extends MitooOptionsDialogBuilder {

    public AboutMitooDialogBuilder(Context context) {
        super(context);
        setTitle(getContext().getString(R.string.alert_about_mitoo_title));
        setOption(getContext().getResources().getStringArray(R.array.prompt_about_mitoo_array));
        setPositiveMessage(getContext().getString(R.string.alert_positive));
        setNegativeMessage(getContext().getString(R.string.alert_negative));
        setPositiveListner(new AboutMitooPromptOnClickListener(true, getContext()));
        setNegativeListner(new AboutMitooPromptOnClickListener(false, getContext()));
        setOptionsListner(this);
    }

}
