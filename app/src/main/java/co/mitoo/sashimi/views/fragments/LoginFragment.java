package co.mitoo.sashimi.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.SessionModel;
import co.mitoo.sashimi.models.jsonPojo.send.JsonLoginSend;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.DataHelper;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.ViewHelper;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquireRequestEvent;
import co.mitoo.sashimi.utils.events.LeagueModelEnquiresResponseEvent;
import co.mitoo.sashimi.utils.events.SessionModelRequestEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.SessionModelResponseEvent;
import rx.Subscription;
import rx.observables.ConnectableObservable;
/**
 * Created by david on 14-11-05.
 */
public class LoginFragment extends MitooFragment {

    protected Subscription subscription;
    private ConnectableObservable<JsonLoginSend> observable;
    private EditText passWordInput;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_login,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_login));
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        super.onError(error);
    }

    @Override
    protected void initializeOnClickListeners(View view) {

        view.findViewById(R.id.loginButton).setOnClickListener(this);
        view.findViewById(R.id.loginPasswordInput).setOnClickListener(this);
        view.findViewById(R.id.forgetPasswordButton).setOnClickListener(this);
        super.initializeOnClickListeners(view);
        /*Take out for v1
        view.findViewById(R.id.facebookLoginButton).setOnClickListener(this);*/

    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        setPassWordInput ((EditText) view.findViewById(R.id.passwordInput));
        setUpToolBar(view);

    }
    
    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable()){
            switch (v.getId()) {
                case R.id.loginButton:
                    loginButtonAction();
                    break;
                case R.id.forgetPasswordButton:
                    forgetPasswordAction();
                    break;
                case R.id.loginPasswordInput:
                    passwordInputRequestFocusAction();
                    break;
            /*Take Out for V1
            case R.id.facebookLoginButton:
                facebookLoginButtonAction();
                break;*/
            }
        }
    }

    private void loginButtonAction() {

        if (allInputsAreValid()) {
            setLoading(true);
            JsonLoginSend jsonObject = new JsonLoginSend(getEmail(), getPassword());
            SessionModelRequestEvent event = new SessionModelRequestEvent(MitooEnum.SessionRequestType.LOGIN, jsonObject);
            getSessionModel().requestSession(event);

        } else {
            handleInvalidInputs();
        }
    }



    @Subscribe
    public void onLoginResponse(SessionModelResponseEvent event) {

        LeagueModelEnquireRequestEvent leagueEnquireRequestEvent = new LeagueModelEnquireRequestEvent(event.getSession().id, MitooEnum.crud.READ);
        getLeagueModel().requestLeagueEnquire(leagueEnquireRequestEvent );

    }

    @Subscribe
    public void onLeagueEnquireResponse(LeagueModelEnquiresResponseEvent event) {

        getMitooActivity().hideSoftKeyboard();
        fireFragmentChangeAction(R.id.fragment_home);
        setLoading(false);
    }

    private void facebookLoginButtonAction(){
        /*
        String applicationId =  getResources().getString(R.string.API_key_facebook);
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        FacebookLoginActivity.launch(getActivity(), applicationId, permissions);*/
    }

    private void forgetPasswordAction(){
        setLoading(true);
        FragmentChangeEvent event = new FragmentChangeEvent(this, MitooEnum.fragmentTransition.PUSH, R.id.fragment_reset_password);
        BusProvider.post(event);
    }

    private String getEmail(){

        return this.getTextFromTextField(R.id.emailInput);

    }

    private String getPassword(){

        return this.getTextFromTextField(R.id.passwordInput);
    }

    private boolean allInputsAreValid(){

        DataHelper dataHelper = getDataHelper();
        return dataHelper.validPassword(getPassword()) && dataHelper.validEmail(getEmail());
        
    }
    
    private void handleInvalidInputs(){

        if(getEmail().equals("")){
            displayText(getString(R.string.toast_email_empty));
        }
        else if(getPassword().equals("")){
            displayText(getString(R.string.toast_password_empty));
        }
        else {

            DataHelper dataHelper =getDataHelper();
            if(!dataHelper.validEmail(getEmail())){
                displayText(getString(R.string.toast_invalid_email));
            } else if(!dataHelper.validPassword(getPassword())){
                displayText(getString(R.string.toast_invalid_password));
            }else{
                displayText(getString(R.string.toast_invalid_input));
            }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        
      super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == FacebookLoginActivity.FACEBOOK_LOGIN_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String faceBookToken = data.getStringExtra(FacebookLoginActivity.EXTRA_FACEBOOK_ACCESS_TOKEN);
                requestAuthToken(faceBookToken);
            }
            else {
                String errorMessage = data.getStringExtra(FacebookLoginActivity.EXTRA_ERROR_MESSAGE);
                displayText(errorMessage);
            }
        }*/
    }
    
    private void requestAuthToken(String faceBookToken){
        
  //      BusProvider.post(new AuthTokenExchangeRequestEvent(faceBookToken));
    }

    public EditText getPassWordInput() {
        return passWordInput;
    }

    public void setPassWordInput(EditText passWordInput) {
        this.passWordInput = passWordInput;
    }
    
    private void passwordInputRequestFocusAction(){
        if(getPassWordInput()!=null)
            getPassWordInput().requestFocus();
    }
}
