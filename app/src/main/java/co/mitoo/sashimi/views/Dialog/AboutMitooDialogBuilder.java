package co.mitoo.sashimi.views.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.listener.FeedBackPromptOnClickListener;

/**
 * Created by david on 15-01-20.
 */
public class AboutMitooDialogBuilder extends MitooOptionsDialogBuilder implements DialogInterface.OnClickListener{

    public AboutMitooDialogBuilder(Context context) {
        super(context);
    }

    public AlertDialog buildPrompt() {

        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(),
                android.R.style.Theme_Material_Settings);
        AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
        builder.setTitle(getContext().getString(R.string.alert_feed_back_title))
                .setSingleChoiceItems(R.array.prompt_about_mitoo_array, 0 ,this)
                .setNegativeButton(getContext().getString(R.string.alert_negative), new FeedBackPromptOnClickListener(false, getContext()))
                .setPositiveButton(getContext().getString(R.string.alert_positive), new FeedBackPromptOnClickListener(true, getContext()));
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        setSelectedOption(which);
    }

}
