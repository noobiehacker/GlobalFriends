package co.mitoo.sashimi.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.greenhalolabs.facebooklogin.FacebookLoginActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.send.UserSend;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.AuthTokenExchangeRequestEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LoginRequestEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.UserRecieveResponseEvent;
import rx.Subscription;
import rx.observables.ConnectableObservable;
/**
 * Created by david on 14-11-05.
 */
public class LoginFragment extends MitooFragment{

    protected Subscription subscription;
    private ConnectableObservable<UserSend> observable;

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

    private void initializeOnClickListeners(View view){

        view.findViewById(R.id.loginButton).setOnClickListener(this);
        view.findViewById(R.id.facebookLoginButton).setOnClickListener(this);
        view.findViewById(R.id.forgetPasswordButton).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                loginButtonAction();
                break;
            case R.id.facebookLoginButton:
                facebookLoginButtonAction();
                break;
            case R.id.forgetPasswordButton:
                forgetPasswordAction();
                break;
        }
    }

    private void loginButtonAction(){

        if(getUsername().equals("")){
            displayText(getString(R.string.toast_email_empty));
        }
        else if(getPassword().equals("")){
            displayText(getString(R.string.toast_password_empty));
        }
        else{
            displayText(getString(R.string.toast_logging_in));
            login(getUsername(), getPassword());
        }
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        handleAndDisplayError(error);
    }

    @Subscribe
    public void onLoginResponse(UserRecieveResponseEvent event){
        displayText(getString(R.string.toast_login_success));
        FragmentChangeEvent fragmentChangeEvent = new FragmentChangeEvent(this, MitooEnum.fragmentTransition.CHANGE, R.id.fragment_home);
        BusProvider.post(fragmentChangeEvent);
    }

    private void facebookLoginButtonAction(){
        String applicationId =  getResources().getString(R.string.API_key_facebook);
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        FacebookLoginActivity.launch(getActivity(), applicationId, permissions);
    }

    private void forgetPasswordAction(){
        FragmentChangeEvent event = new FragmentChangeEvent(this, MitooEnum.fragmentTransition.PUSH, R.id.fragment_reset_password);
        BusProvider.post(event);
    }

    private void login(String username, String password){
        BusProvider.post(new LoginRequestEvent(username, password));
    }

    private String getUsername(){

        return this.getTextFromTextField(R.id.emailInput);

    }

    private String getPassword(){

        return this.getTextFromTextField(R.id.passwordInput);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FacebookLoginActivity.FACEBOOK_LOGIN_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                String faceBookToken = data.getStringExtra(FacebookLoginActivity.EXTRA_FACEBOOK_ACCESS_TOKEN);
                requestAuthToken(faceBookToken);
            }
            else {
                String errorMessage = data.getStringExtra(FacebookLoginActivity.EXTRA_ERROR_MESSAGE);
                displayText(errorMessage);
            }
        }
    }
    
    private void requestAuthToken(String faceBookToken){
        
        BusProvider.post(new AuthTokenExchangeRequestEvent(faceBookToken));
    }

}
