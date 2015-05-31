package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Invitation_token;
import co.mitoo.sashimi.models.jsonPojo.recieve.NotificationReceive;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.BranchIOResponseEvent;
import co.mitoo.sashimi.utils.events.ConfirmInfoSetPasswordRequestEvent;
import co.mitoo.sashimi.utils.events.ConfirmInfoResponseEvent;
import co.mitoo.sashimi.utils.events.ConfirmingUserRequestEvent;
import co.mitoo.sashimi.utils.events.ConsumeNotificationEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.ModelPersistedDataDeletedEvent;
import co.mitoo.sashimi.utils.events.ModelPersistedDataLoadedEvent;
import co.mitoo.sashimi.utils.events.NotificationEvent;

/**
 * Created by david on 14-11-05.
 */
public class SplashScreenFragment extends MitooFragment {

    private boolean branchResponseRecieved = false;
    private boolean persistedDataResponseRecieved = false;
    private Invitation_token invitationToken;
    private boolean dialogButtonCreated;

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
        setRunnable(new Runnable() {
            @Override
            public void run() {
                regularFlow();
            }
        });
        getHandler().postDelayed(getRunnable(), 1000);
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

        if (recievedBranchIOResponse() && recievedPersistedDataResponse()) {

            getHandler().removeCallbacks(getRunnable());
            NotificationReceive notificationReceive = getMitooActivity().getMitooApplication().getNotificationReceive();
            //NOTIFICATION FLOW
            if (notificationReceive!= null) {
                BusProvider.post(new NotificationEvent(notificationReceive));
                getMitooActivity().getMitooApplication().setNotificationReceive(null);
            } else{
                regularFlow();
            }

        }

    }

    private void regularFlow(){
        //REGULAR FLOW
        routeToLanding();
    }

    public void startInviteFlow() {


    }

}
