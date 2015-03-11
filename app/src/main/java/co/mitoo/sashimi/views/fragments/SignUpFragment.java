package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
import co.mitoo.sashimi.utils.FormHelper;
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
    private boolean loggedIn;
    private EditText topEditText;
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
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeOnClickListeners(View view) {
        view.findViewById(R.id.joinButton).setOnClickListener(this);
        super.initializeOnClickListeners(view);
        /*Take out for V1
        view.findViewById(R.id.facebookJoinButton).setOnClickListener(this);*/
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_join));
        setLoggedIn(getSessionModel().userIsLoggedIn());
    }
    
    @Override
    protected void initializeViews(View view){
        
        super.initializeViews(view);
        ViewHelper viewHelper = new ViewHelper(getMitooActivity());
        viewHelper.setUpSignUpView(view, getSelectedLeague());
        setTopEditText((EditText)view.findViewById(R.id.nameInput));
        setUpSignUpInfoText(view);
    }

    @Override
    public void onStart(){

        super.onResume();
        requestFocusForTopInput(getTopEditText());

    }

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable()){
            switch (v.getId()) {
                case R.id.joinButton:
                    joinButtonAction();
                    break;
            /* Take Out For V1
            case R.id.facebookJoinButton:
                facebookJoinButtonAction();
                break;*/
            }
        }
    }

    private void joinButtonAction() {

        if (allInputsAreValid()) {
            setLoading(true);
            JsonSignUpSend signUpSend = createSignUpJsonFromInput();
            SessionModelRequestEvent event = new SessionModelRequestEvent(MitooEnum.SessionRequestType.SIGNUP, signUpSend);
            getSessionModel().requestSession(event);

        } else {
            handleInvalidInputs();
        }

   
    }

    @Subscribe
    public void onJoinResponse(SessionModelResponseEvent event) {

        setLoading(false);
        LeagueModelEnquireRequestEvent requestEvent = new LeagueModelEnquireRequestEvent(event.getSession().id);
        getLeagueModel().requestToEnquireLeague(requestEvent);
    }

    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {

        setLoading(false);
        getMitooActivity().hideSoftKeyboard();
        fireFragmentChangeAction(R.id.fragment_confirm , MitooEnum.FragmentAnimation.VERTICAL);

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        super.onError(error);
    }

    private void facebookJoinButtonAction() {


    }

    private JsonSignUpSend createSignUpJsonFromInput() {

        return new JsonSignUpSend(getEmail(), getPassword(), getUsername(), getPhone());
          //return new JsonSignUpSend("ABC", "1@2.0", "1234567890", "abcd");
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
            setSelectedLeague(getLeagueModel().getSelectedLeague());
        }
        return selectedLeague;
    }

    public void setSelectedLeague(League selectedLeague) {
        this.selectedLeague = selectedLeague;
    }

    private boolean allInputsAreValid(){

        FormHelper formHelper = getFormHelper();
        return formHelper.validPassword(getPassword()) && formHelper.validEmail(getEmail())
                && formHelper.validName(getUsername()) && formHelper.validPhone(getPhone());

    }

    private void handleInvalidInputs() {

        if (!handledEmptyInput()) {
            if (!getFormHelper().validEmail(getEmail())) {
                
                getFormHelper().handleInvalidEmail(getEmail());
                
            } else if (!getFormHelper().validPassword(getPassword())) {
                
                getFormHelper().handleInvalidPassword(getPassword());
                
            } else if (!getFormHelper().validName(getUsername())) {
                
                getFormHelper().handleInvalidUserName(getUsername());
                
            } else if (!getFormHelper().validPhone(getPhone())) {
                
                getFormHelper().handleInvalidPhone(getPhone());
                
            } else {
                displayText(getString(R.string.toast_invalid_input));
            }
        }
    }
    
    private boolean handledEmptyInput(){

        boolean result = true;
        if (getUsername().equals("")) {
            this.displayText(getString(R.string.toast_name_required));
        } else if (getEmail().equals("")) {
            this.displayText(getString(R.string.toast_email_required));
        } else if (getPassword().equals("")) {
            this.displayText(getString(R.string.toast_password_required));
        } else if (getPhone().equals("")) {
            this.displayText(getString(R.string.toast_phone_required));
        } else {
            result =false;
        }
        return result;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    private MitooEnum.ViewType getViewType(){

        return MitooEnum.ViewType.FRAGMENT;

    }

    public EditText getTopEditText() {
        return topEditText;
    }

    public void setTopEditText(EditText topEditText) {
        this.topEditText = topEditText;
    }

    private void setUpSignUpInfoText(View view){

        TextView topSignUpText = (TextView)view.findViewById(R.id.signUpTopText);
        String leagueName = getSelectedLeague().getName();
        topSignUpText.setText(getDataHelper().createSignUpInfo(leagueName));

    }
}
