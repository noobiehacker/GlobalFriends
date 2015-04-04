package co.mitoo.sashimi.utils;

import co.mitoo.sashimi.BuildConfig;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-03-23.
 */
public class AppStringHelper {

    private MitooActivity activity;

    public AppStringHelper(MitooActivity activity) {
        this.activity = activity;
    }

    public MitooActivity getActivity() {
        return activity;
    }

    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }

    public String getBranchAPIKey(){
        return getActivity().getString(R.string.API_key_branch);
    }

}
