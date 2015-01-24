package co.mitoo.sashimi.views.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LogOutEvent;

/**
 * Created by david on 15-01-23.
 */
public class LogOutDialogBuilder  extends MitooDialogBuilder {

    public LogOutDialogBuilder(Context context) {
        super(context);
        setTitle(getContext().getString(R.string.alert_log_out));
        setPositiveMessage(getContext().getString(R.string.alert_positive));
        setNegativeMessage(getContext().getString(R.string.alert_negative));
        setPositiveListner(buildListener(true));
        setNegativeListner(buildListener(false));
    }


    public DialogInterface.OnClickListener buildListener(boolean positive) {
        
        final boolean positiveIndicatorToPassIn = positive;
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                
                if(positiveIndicatorToPassIn){
                    BusProvider.post(new LogOutEvent());
                }
                dialog.dismiss();
            }

        };
    }

}
