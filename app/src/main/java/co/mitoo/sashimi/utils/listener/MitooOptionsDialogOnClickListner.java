package co.mitoo.sashimi.utils.listener;

import android.content.Context;

/**
 * Created by david on 15-01-20.
 */
public abstract class MitooOptionsDialogOnClickListner extends MitooDialogOnClickListener{

    protected MitooOptionsDialogOnClickListner(boolean positiveListener, Context context) {
        super(positiveListener, context);
    }

    private int selectedOption;

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
}
