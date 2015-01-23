package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.SessionModelRequestEvent;
import co.mitoo.sashimi.utils.events.SessionModelResponseEvent;

/**
 * Created by david on 14-11-19.
 */
public class SignUpFragment extends MitooFragment {

    private League selectedLeague;
    
    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_sign_up,
                container, false);
        initializeOnClickListeners(view);
        initializeFields();
        initializeViews(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initializeOnClickListeners(View view) {
        view.findViewById(R.id.joinButton).setOnClickListener(this);
        view.findViewById(R.id.facebookJoinButton).setOnClickListener(this);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_join));
    }
    
    @Override
    protected void initializeViews(View view){
        
        super.initializeViews(view);
        ViewHelper viewHelper = new ViewHelper(getActivity());
        viewHelper.setUpLeagueImage(view, getSelectedLeague());
        viewHelper.setUpLeageText(view , getSelectedLeague());
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.joinButton:
                joinButtonAction();
                break;
            case R.id.facebookJoinButton:
                facebookJoinButtonAction();
                break;
        }
    }

    private void joinButtonAction() {

        if (getUsername().equals("")) {
            this.displayText(getString(R.string.toast_username_empty));
        } else if (getEmail().equals("")) {
            this.displayText(getString(R.string.toast_email_empty));
        } else if (getPassword().equals("")) {
            this.displayText(getString(R.string.toast_password_empty));
        } else if (getPhone().equals("")) {
            this.displayText(getString(R.string.toast_phone_empty));
        } else {
            this.displayText(getString(R.string.toast_signing_up));
            join(getUsername(), getEmail(), getPhone(), getPassword());
        }

    }

    @Subscribe
    public void onJoinResponse(SessionModelResponseEvent event) {
        
        BusProvider.post(new LeagueModelEnquireRequestEvent(event.getSession().id,MitooEnum.crud.CREATE));
        
    }

    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {
        
        fireFragmentChangeAction(R.id.fragment_confirm);
        
    }
    
    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        handleAndDisplayError(error);
    }

    private void facebookJoinButtonAction() {


    }

    private void join(String username, String email, String phone, String password) {
        JsonSignUpSend signUpSend = new JsonSignUpSend(email , password ,username, phone);
        BusProvider.post(new SessionModelRequestEvent(MitooEnum.SessionRequestType.SIGNUP, signUpSend)) ;
    }

    private String getUsername() {
        return this.getTextFromTextField(R.id.nameInput);
    }

    private String getEmail() {
        return this.getTextFromTextField(R.id.emailInput);
    }

    private String getPhone() {
        return this.getTextFromTextField(R.id.phoneInput);
    }

    private String getPassword() {
        return this.getTextFromTextField(R.id.passwordInput);
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


}
