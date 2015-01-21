package co.mitoo.sashimi.utils.listener;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by david on 15-01-20.
 */
public abstract class MitooDialogOnClickListener implements DialogInterface.OnClickListener{

    private boolean positiveListener;
    private Context context;

    public MitooDialogOnClickListener(boolean positiveListener , Context context){
        setPositiveListener(positiveListener);
        setContext(context);
    }

    @Override
    public abstract void onClick(DialogInterface dialog, int which) ;

    public boolean isPositiveListener() {
        return positiveListener;
    }

    public void setPositiveListener(boolean positiveListener) {
        this.positiveListener = positiveListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
