package co.mitoo.sashimi.views.Dialog;

import android.content.Context;

/**
 * Created by david on 15-01-20.
 */
public abstract class MitooOptionsDialogBuilder extends MitooDialogBuilder{

    protected MitooOptionsDialogBuilder(Context context) {
        super(context);
    }

    private int selectedOption;

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
}
