package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FormHelper;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.LeagueRequestFromIDEvent;
import co.mitoo.sashimi.utils.events.LeagueResponseFromIDEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.SessionModelRequestEvent;
import co.mitoo.sashimi.utils.events.SessionModelResponseEvent;

/**
 * Created by david on 14-11-19.
 */
public class SignUpFragment extends MitooFragment {

    private LeagueModel leagueModel;
    private EditText topEditText;
    private TextView topSignUpText;
    private boolean viewLoaded;
    private int leagueID;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
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
        requestFocusForInput(getTopEditText());
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
            setUpSignUpInfoText(getRootView());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_sign_up,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeOnClickListeners(View view) {
        super.initializeOnClickListeners(view);
        view.findViewById(R.id.joinButton).setOnClickListener(this);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_join));
    }
    
    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        this.topEditText = (EditText) view.findViewById(R.id.nameInput);
        this.topSignUpText = (TextView) view.findViewById(R.id.signUpTopText);
        this.viewLoaded = true;
        updateView();

    }

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable(v.getId())){
            switch (v.getId()) {
                case R.id.joinButton:
                    joinButtonAction();
                    break;
            }
        }
    }

    private void joinButtonAction() {

        if (allInputsAreValid()) {
            setLoading(true);
            JsonSignUpSend signUpSend = createSignUpJsonFromInput();

            SessionModelRequestEvent event = new SessionModelRequestEvent(MitooEnum.SessionRequestType.SIGNUP, signUpSend);
            BusProvider.post(event);

        } else {
            handleInvalidInputs();
        }

    }

    @Subscribe
    public void onJoinResponse(SessionModelResponseEvent event) {

        setLoading(false);
        LeagueModelEnquireRequestEvent requestEvent = new LeagueModelEnquireRequestEvent(event.getSession().id,this.leagueModel);
        BusProvider.post(requestEvent);
    }

    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {

        setLoading(false);
        getMitooActivity().hideSoftKeyboard();

        Bundle bundle = new Bundle();
        bundle.putInt(getLeagueIDKey(), this.leagueID);

        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_sign_up_confirm)
                .setAnimation(MitooEnum.FragmentAnimation.VERTICAL)
                .setBundle(bundle)
                .build();
        BusProvider.post(fragmentChangeEvent);

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        super.onError(error);
    }

    private JsonSignUpSend createSignUpJsonFromInput() {

        return new JsonSignUpSend(getEmail(), getPassword(), getUsername(), getPhone() , getTimeZone());
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

    private String getTimeZone() {
        return this.getTextFromTextField(R.id.passwordInput);
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
                displayTextWithToast(getString(R.string.toast_invalid_input));
            }
        }
    }
    
    private boolean handledEmptyInput(){

        boolean result = true;
        if (getUsername().equals("")) {
            this.displayTextWithToast(getString(R.string.toast_name_required));
        } else if (getEmail().equals("")) {
            this.displayTextWithToast(getString(R.string.toast_email_required));
        } else if (getPassword().equals("")) {
            this.displayTextWithToast(getString(R.string.toast_password_required));
        } else if (getPhone().equals("")) {
            this.displayTextWithToast(getString(R.string.toast_phone_required));
        } else {
            result =false;
        }
        return result;
    }

    public EditText getTopEditText() {
        return topEditText;
    }

    private void setUpSignUpInfoText(View view){

        if(this.leagueModel !=null){
            String leagueName = this.leagueModel.getLeague().getName();
            topSignUpText.setText(getDataHelper().createSignUpInfo(leagueName));
        }

    }

    private String getFirstSport(){
        String result = "";
        if(this.leagueModel!=null){
            result =this.leagueModel.getLeague().getFirstSports();
        }
        return result ;
    }
}
