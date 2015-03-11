package co.mitoo.sashimi.views.widgets;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.FragmentFactory;
import co.mitoo.sashimi.views.fragments.MitooFragment;

/**
 * Created by david on 15-03-08.
 */
public class MitooTab {

    private MitooFragment fragment;
    private MitooMaterialsTab tab;

    public MitooTab() {
    }

    public MitooFragment getFragment() {

        if(fragment==null)
            fragment= FragmentFactory.getInstance().createFragment(R.id.fragment_schedule);
        return fragment;
    }


    public MitooMaterialsTab getTab() {
        return tab;
    }

    public void setTab(MitooMaterialsTab tab) {
        this.tab = tab;
    }
}
