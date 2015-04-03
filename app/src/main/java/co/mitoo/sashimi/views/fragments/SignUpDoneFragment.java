package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
/**
 * Created by david on 15-01-19.
 */
public class SignUpDoneFragment extends MitooFragment {

    private League selectedLeague;
    private MitooEnum.ConfirmFlow flow;

    public static SignUpDoneFragment newInstance() {

        return new SignUpDoneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStaste) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm,
                container, false);
        initializeOnClickListeners(view);
        initializeViews(view);
        return view;
    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setAllowBackPressed(false);
        setFragmentTitle(getString(R.string.tool_bar_confirmation));
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        getViewHelper().setUpLeagueBackgroundView(view, getSelectedLeague());

    }

    @Override
    protected void initializeOnClickListeners(View view){
        view.findViewById(R.id.viewMyLeaguesButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewMyLeaguesButton:
                viewMyLeagueButtonAction();
            break;
        }
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        super.onError(error);
    }

    private void viewMyLeagueButtonAction(){

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_key_from_confirm), getString(R.string.bundle_value_true));
        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_home)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setAnimation(MitooEnum.FragmentAnimation.VERTICAL)
                .setBundle(bundle)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

    }

    public League getSelectedLeague() {
        if(selectedLeague==null){
            setSelectedLeague(getRetriever().getLeagueModel().getSelectedLeague());
        }
        return selectedLeague;
    }

    public void setSelectedLeague(League selectedLeague) {
        this.selectedLeague = selectedLeague;
    }


    public MitooEnum.ConfirmFlow getFlow() {
        if(flow== null)
            flow = MitooEnum.ConfirmFlow.SIGNUP;
        return flow;
    }

}
