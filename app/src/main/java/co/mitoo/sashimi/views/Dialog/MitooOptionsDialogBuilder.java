package co.mitoo.sashimi.views.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import co.mitoo.sashimi.utils.listener.MitooOptionsDialogOnClickListner;

/**
 * Created by david on 15-01-20.
 */
public abstract class MitooOptionsDialogBuilder extends MitooDialogBuilder  implements DialogInterface.OnClickListener{

    protected MitooOptionsDialogBuilder(Context context) {
        super(context);
    }

    private int selectedOption;
    private DialogInterface.OnClickListener optionsListner;
    private CharSequence[] option;

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }

    @Override
    public AlertDialog buildPrompt() {

        /*ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(),
                android.R.style.Theme_Material_Settings);*/
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getTitle())
                .setSingleChoiceItems(getOption(), 0, getOptionsListner())
                .setNegativeButton(getNegativeMessage(), getNegativeListner())
                .setPositiveButton(getPositiveMessage(), getPositiveListner());
        return builder.create();
    }

    public DialogInterface.OnClickListener getOptionsListner() {
        return optionsListner;
    }

    public void setOptionsListner(DialogInterface.OnClickListener optionsListner) {
        this.optionsListner = optionsListner;
    }

    public CharSequence[] getOption() {
        return option;
    }

    public void setOption(CharSequence[] option) {
        this.option = option;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

        setSelectedOption(which);
        MitooOptionsDialogOnClickListner optionListener = (MitooOptionsDialogOnClickListner) getPositiveListner();
        optionListener.setSelectedOption(which);

    }

}
