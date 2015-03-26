package co.mitoo.sashimi.views.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Invitation_token;
import co.mitoo.sashimi.models.jsonPojo.recieve.SessionRecieve;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.BranchIOResponseEvent;
import co.mitoo.sashimi.utils.events.ConfirmInfoModelResponseEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.ModelPersistedDataDeletedEvent;
import co.mitoo.sashimi.utils.events.ModelPersistedDataLoadedEvent;

/**
 * Created by david on 14-11-05.
 */
public class SplashScreenFragment extends MitooFragment {

    private boolean branchResponseRecieved = false;
    private boolean persistedDataResponseRecieved = false;
    private Invitation_token invitationToken;

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

        setInvitationToken(getSessionModel().getInvitation_token());
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

                    if(!isInviteFlow())
                        startRegularFlow();

                }
            }, MitooConstants.durationShort);
        }

    }



    public void startRegularFlow(){

        SessionRecieve session = getSessionModel().getSession();
        if (session != null) {
            getMitooActivity().updateAuthToken(session);
            routeToHome();
        }
        else{
            routeToLanding();
        }
    }



    public Invitation_token getInvitationToken() {
        return invitationToken;
    }

    public void setInvitationToken(Invitation_token invitationToken) {
        this.invitationToken = invitationToken;
    }

    @Override
    protected void handleHttpErrors(int statusCode) {

        if (statusCode == 401 || statusCode == 409) {
            if (statusCode == 401)
                handle401Error();
            else if (statusCode == 409)
                handle409Error();
        } else
            super.handleHttpErrors(statusCode);
    }

    private void handle401Error(){

        displayTextWithDialog(getString(R.string.prompt_confirm_401_title),
                getString(R.string.prompt_confirm_401_Message),
                createRegularFlowDialogListner());

    }

    private void handle409Error(){

        displayTextWithDialog(getString(R.string.prompt_confirm_409_title),
                getString(R.string.prompt_confirm_409_Message),
                createRegularFlowDialogListner());

    }

    @Override
    protected void handleNetworkError() {

        displayTextWithToast(getString(R.string.error_no_internet));
        startRegularFlow();

    }

    private DialogInterface.OnClickListener createRegularFlowDialogListner(){

        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startRegularFlow();
            }
        };

    }

    private boolean isInviteFlow(){
        return getInvitationToken()!= null && getInvitationToken().getToken()!=null;
    }
}
