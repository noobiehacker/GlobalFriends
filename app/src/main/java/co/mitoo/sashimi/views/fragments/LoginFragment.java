package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.IUserModel;
import co.mitoo.sashimi.models.UserModel;
import co.mitoo.sashimi.models.jsonPojo.send.UserSend;
import co.mitoo.sashimi.utils.BusProvider;
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
    private IUserModel model;
    private ConnectableObservable<UserSend> observable;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_login,
                container, false);
        initializeOnClickListeners(view);
        initializeFields();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void initializeFields(){
        model = new UserModel(getActivity().getResources());
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
            displayText(getString(R.string.toast_loading));
            login(getUsername(), getPassword());
        }
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        handleAndDisplayError(error);
    }

    @Subscribe
    public void onLoginResponse(UserRecieveResponseEvent event){
        this.displayText(event.getUser().auth_token);
    }

    private void facebookLoginButtonAction(){


    }

    private void forgetPasswordAction(){
        FragmentChangeEvent event = new FragmentChangeEvent(this, R.id.fragment_reset_password);
        event.setPush(true);
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

}
