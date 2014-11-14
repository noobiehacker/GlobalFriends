package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.ILoginModel;
import co.mitoo.sashimi.models.LoginModel;
import co.mitoo.sashimi.models.Pojo.Auth_token;
import rx.Subscriber;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.observables.ConnectableObservable;

/**
 * Created by david on 14-11-05.
 */
public class LoginFragment extends MitooFragment{

    protected Subscription subscription;
    private ILoginModel model;
    private ConnectableObservable<Auth_token> observable;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        fragment.initializeSubscription();
        fragment.model = new LoginModel(fragment.getActivity().getResources());
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
        return view;
    }

    @Override
    public void onResume(){
        super.onStart();
    }

    private void initializeSubscription(){
        subscription = AndroidObservable.bindFragment(this, observable).subscribe(new Subscriber<Auth_token>() {

            @Override
            public void onCompleted() {
                Toast.makeText(getActivity(), "Done!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(Auth_token token) {
                Toast.makeText(getActivity(), "Authorization Token Is" + token.id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeOnClickListeners(View view){
        view.findViewById(R.id.loginButton).setOnClickListener(this);
        view.findViewById(R.id.facebookLoginButton).setOnClickListener(this);
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
        }
    }

    private void loginButtonAction(){

        if(getUsername().equals("")){
            Toast.makeText(getActivity(),"Email can not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(getPassword().equals("")){
            Toast.makeText(getActivity(),"Password can not be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),"Logging in", Toast.LENGTH_SHORT).show();
            login(getUsername(), getPassword());
        }

    }

    private void facebookLoginButtonAction(){


    }

    private void login(String username, String password){
        observable = model.login(username, password).publish();
    }

    private String getUsername(){
        EditText textField = (EditText) getActivity().findViewById(R.id.emailInput);
        return textField.getText().toString();
    }

    private String getPassword(){
        EditText textField = (EditText) getActivity().findViewById(R.id.passwordInput);
        return textField.getText().toString();
    }

}
