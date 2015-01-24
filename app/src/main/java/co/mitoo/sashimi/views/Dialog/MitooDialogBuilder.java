package co.mitoo.sashimi.views.Dialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.listener.MitooOptionsDialogOnClickListner;

/**
 * Created by david on 15-01-20.
 */
public abstract class MitooDialogBuilder {

    public MitooDialogBuilder(Context context) {
        setContext(context);
    }

    private Context context;
    private String positiveMessage;
    private String negativeMessage;
    private String title;
    private String message;
    private DialogInterface.OnClickListener positiveListner;
    private DialogInterface.OnClickListener negativeListner;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public AlertDialog buildPrompt() {

       /* ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(),
                android.R.style.Theme_Material_Settings);*/
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getTitle())
                .setMessage(getMessage())
                .setNegativeButton(getNegativeMessage(), getNegativeListner())
                .setPositiveButton(getPositiveMessage(), getPositiveListner());
        return builder.create();
    }

    public String getPositiveMessage() {
        return positiveMessage;
    }

    public void setPositiveMessage(String positiveMessage) {
        this.positiveMessage = positiveMessage;
    }

    public String getNegativeMessage() {
        return negativeMessage;
    }

    public void setNegativeMessage(String negativeMessage) {
        this.negativeMessage = negativeMessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DialogInterface.OnClickListener getPositiveListner() {
        return positiveListner;
    }

    public void setPositiveListner(DialogInterface.OnClickListener positiveListner) {
        this.positiveListner = positiveListner;
    }

    public DialogInterface.OnClickListener getNegativeListner() {
        return negativeListner;
    }

    public void setNegativeListner(DialogInterface.OnClickListener negativeListner) {
        this.negativeListner = negativeListner;
    }

    public String getMessage() {
        if(this.message==null)
            setMessage(getContext().getString(R.string.alert_default_message));
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
