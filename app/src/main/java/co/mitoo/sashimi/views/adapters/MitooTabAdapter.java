package co.mitoo.sashimi.views.adapters;

import android.app.Fragment;
import android.app.FragmentManager;

import java.util.List;

import co.mitoo.sashimi.views.widgets.MitooTab;

/**
 * Created by david on 15-03-08.
 */
public class MitooTabAdapter extends FragmentStatePagerAdapterNotV4{

    private List<MitooTab> mitooTabs;

    public MitooTabAdapter(FragmentManager fm) {
        super(fm);
    }

    public MitooTabAdapter(List<MitooTab> mitooTabs ,FragmentManager fm) {
        super(fm);
        setMitooTabs(mitooTabs);
    }

    public Fragment getItem(int num) {
        return getMitooTabs().get(num).getFragment();
    }

    @Override
    public int getCount() {
        return getMitooTabs().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getMitooTabs().get(position).getTab().getName();
    }

    public List<MitooTab> getMitooTabs() {
        return mitooTabs;
    }

    public void setMitooTabs(List<MitooTab> mitooTabs) {
        this.mitooTabs = mitooTabs;
    }
}
