package co.mitoo.sashimi.views.adapters;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FixtureViewHelper;
import co.mitoo.sashimi.utils.FixtureWrapper;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.fragments.MitooFragment;

/**
 * Created by david on 15-04-13.
 */

public class FixtureListAdapter extends ArrayAdapter<FixtureWrapper> implements AdapterView.OnItemClickListener {

    private MitooFragment fragment;
    private FixtureViewHelper viewHelper;

    public FixtureListAdapter(Context context, int resourceId, List<FixtureWrapper> objects , MitooFragment fragment) {
        super(context, resourceId, objects);
        setViewHelper(fragment.getViewHelper().getFixtureViewHelper());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = View.inflate(getContext(), R.layout.view_fixture_row, null);
        }
        getViewHelper().customizeFixtureRow(convertView, this.getItem(position));
        setUpDateTextView(convertView, this.getItem(position));
        return convertView;

    }

    public MitooFragment getFragment() {
        return fragment;
    }

    public void setFragment(MitooFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (getFragment().getDataHelper().isClickable(view.getId()) && id != -1) {

            FixtureWrapper fixture = (FixtureWrapper) parent.getItemAtPosition(position);
            fixtureItemClickAction(fixture);
        }
    }
    public FixtureViewHelper getViewHelper() {
        return viewHelper;
    }

    public void setViewHelper(FixtureViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    private void setUpDateTextView(View convertView ,FixtureWrapper wrapper ){

        View dateContainer = convertView.findViewById(R.id.dateTextContainer);

        if(wrapper.isFirstFixtureForDateGroup()){
            TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextField);
            String fixtureDate = wrapper.getMediumDisplayableDate();
            dateTextView.setText(fixtureDate);
            dateContainer.setVisibility(View.VISIBLE);
        }
        else{
            dateContainer.setVisibility(View.GONE);
        }

    }

    private void fixtureItemClickAction(FixtureWrapper fixture){

        FragmentChangeEvent event = FragmentChangeEventBuilder
                .getSingletonInstance()
                .setFragmentID(R.id.fragment_fixture)
                .setBundle(createBundle(fixture))
                .build();
        BusProvider.post(event);

    }

    private MitooActivity getActivity(){
        return (MitooActivity)getFragment().getActivity();
    }

    private Bundle createBundle(FixtureWrapper fixture){
        Bundle bundle = new Bundle();
        bundle.putInt(getFixtureIdKey(), fixture.getFixture().getId());
        return bundle;
    }

    private String getFixtureIdKey(){
        return this.fragment.getString(R.string.bundle_key_fixture_id_key);
    }
}
