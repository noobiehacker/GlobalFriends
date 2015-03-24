package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-03-23.
 */
public class ConfirmDoneFragment extends MitooFragment {

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable()){
            switch (v.getId()) {
                case R.id.viewMyLeaguesButton:
                    viewMyLeagueButtonAction();
                    break;
            }
        }
    }

    public static ConfirmDoneFragment newInstance() {
        ConfirmDoneFragment fragment = new ConfirmDoneFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm_done,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_confirmation));
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    @Override
    protected void initializeOnClickListeners(View view) {

        view.findViewById(R.id.viewMyLeaguesButton).setOnClickListener(this);

    }

    private void viewMyLeagueButtonAction(){

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_key_from_confirm), getString(R.string.bundle_value_true));
        fireFragmentChangeAction(R.id.fragment_home , MitooEnum.FragmentTransition.CHANGE , MitooEnum.FragmentAnimation.VERTICAL, bundle);

    }

}
