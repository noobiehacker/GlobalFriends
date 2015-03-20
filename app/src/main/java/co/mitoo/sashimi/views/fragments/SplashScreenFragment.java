package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.BranchIOResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.ModelPersistedDataDeletedEvent;
import co.mitoo.sashimi.utils.events.ModelPersistedDataLoadedEvent;

/**
 * Created by david on 14-11-05.
 */
public class SplashScreenFragment extends MitooFragment {

    private boolean branchResponseRecieved = false;
    private boolean persistedDataResponseRecieved = false;
    private boolean acceptInviteFlow = true;

    @Override
    public void onClick(View v) {

    }

    public static SplashScreenFragment newInstance() {
        SplashScreenFragment fragment = new SplashScreenFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_splash,
                container, false);
        return view;
    }
    
    @Subscribe
    public void onModelPersistedDataLoaded(ModelPersistedDataLoadedEvent event) {

        setPersistedDataResponseRecieved(true);
        loadFirstFragment();

    }

    @Subscribe
    public void onModelPersistedDataDeleted(ModelPersistedDataDeletedEvent event) {

        setPersistedDataResponseRecieved(true);
        loadFirstFragment();

    }

    @Subscribe
    public void onBranchIOResponse(BranchIOResponseEvent event){

        setAcceptInviteFlow(event.getToken()!=null);
        setBranchResponseRecieved(true);
        loadFirstFragment();

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        
        super.onError(error);
    }

    public boolean recievedBranchIOResponse() {
        return branchResponseRecieved;
    }

    public void setBranchResponseRecieved(boolean branchResponseRecieved) {
        this.branchResponseRecieved = branchResponseRecieved;
    }

    public boolean recievedPersistedDataResponse() {
        return persistedDataResponseRecieved;
    }

    public void setPersistedDataResponseRecieved(boolean persistedDataResponseRecieved) {
        this.persistedDataResponseRecieved = persistedDataResponseRecieved;
    }

    private void loadFirstFragment() {

        if(recievedBranchIOResponse() && recievedPersistedDataResponse()){
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {

                    if(isAcceptInviteFlow())
                        startInviteFlow();
                    else
                        startRegularFlow();

                }
            }, MitooConstants.durationShort);
        }

    }

    public boolean isAcceptInviteFlow() {
        return acceptInviteFlow;
    }

    public void setAcceptInviteFlow(boolean acceptInviteFlow) {
        this.acceptInviteFlow = acceptInviteFlow;
    }

    public void startInviteFlow(){

        fireFragmentChangeAction(R.id.fragment_confirm_account, MitooEnum.FragmentTransition.CHANGE, MitooEnum.FragmentAnimation.HORIZONTAL);

    }

    public void startRegularFlow(){

        SessionRecieve session = getSessionModel().getSession();
        if (session != null) {
            getMitooActivity().updateAuthToken(session);
            fireFragmentChangeAction(R.id.fragment_home, MitooEnum.FragmentTransition.CHANGE, MitooEnum.FragmentAnimation.VERTICAL);

        }
        else{
            fireFragmentChangeAction(R.id.fragment_landing, MitooEnum.FragmentTransition.CHANGE , MitooEnum.FragmentAnimation.HORIZONTAL);

        }
    }
}
