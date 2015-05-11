package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LeagueRequestFromIDEvent;
import co.mitoo.sashimi.utils.events.LeagueResponseFromIDEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
/**
 * Created by david on 15-01-19.
 */
public class SignUpDoneFragment extends MitooFragment {

    private LeagueModel leagueModel;
    private int leagueID;
    private boolean viewLoaded;

    public static SignUpDoneFragment newInstance() {

        return new SignUpDoneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            this.leagueID = savedInstanceState.getInt(getLeagueIDKey());
        }else{
            this.leagueID =  getArguments().getInt(getLeagueIDKey());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(getLeagueIDKey(), this.leagueID);
        super.onSaveInstanceState(bundle);

    }

    @Override
    public void onResume(){
        super.onResume();
        BusProvider.post(new LeagueRequestFromIDEvent(this.leagueID));
    }

    @Subscribe
    public void onLeagueResponse(LeagueResponseFromIDEvent event) {

        if(event.getLeagueModel()!=null){
            this.leagueModel =event.getLeagueModel();
            updateView();
        }

    }

    private void updateView(){

        if(this.leagueModel !=null && getRootView()!=null && this.viewLoaded){
            getViewHelper().setUpLeagueBackgroundView(getRootView(), this.leagueModel.getLeague());

        }

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
        this.viewLoaded=true;

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
        bundle.putInt(getUserIDKey(), getUserID());
        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_home)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setAnimation(MitooEnum.FragmentAnimation.VERTICAL)
                .setBundle(bundle)
                .build();
        BusProvider.post(fragmentChangeEvent);

    }

}
