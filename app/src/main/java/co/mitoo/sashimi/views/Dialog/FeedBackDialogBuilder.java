package co.mitoo.sashimi.views.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.listener.FeedBackPromptOnClickListener;
import co.mitoo.sashimi.utils.listener.MitooOptionsDialogOnClickListner;

/**
 * Created by david on 15-01-20.
 */
public class FeedBackDialogBuilder extends MitooOptionsDialogBuilder implements DialogInterface.OnClickListener{

    public FeedBackDialogBuilder(Context context) {
        super(context);
        setTitle(getContext().getString(R.string.alert_feed_back_title));
        setOption(getContext().getResources().getStringArray(R.array.prompt_feed_back_array));
        setPositiveMessage(getContext().getString(R.string.alert_positive));
        setNegativeMessage(getContext().getString(R.string.alert_negative));
        setPositiveListner(new FeedBackPromptOnClickListener(true, getContext()));
        setNegativeListner(new FeedBackPromptOnClickListener(false, getContext()));
        setOptionsListner(this);
    }

}
