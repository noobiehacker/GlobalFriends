package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.ISignupModel;
import co.mitoo.sashimi.models.SignupModel;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.listener.FragmentChangeListener;


/**
 * Created by david on 14-11-14.
 */
public class SignupFragment extends MitooFragment {

    private ISignupModel model;

    public static SignupFragment newInstance(FragmentChangeListener listner) {

        SignupFragment fragment = new SignupFragment();
        fragment.model = new SignupModel(fragment.getActivity().getResources());
        fragment.viewlistner=listner;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_signup,
                container, false);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onStart();
    }

    private void initializeOnClickListeners(View view) {
        view.findViewById(R.id.signupButton).setOnClickListener(this);
        view.findViewById(R.id.facebookLoginButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signupButton:
                signupButtonAction();
                break;
            case R.id.facebookLoginButton:
                facebookLoginButtonAction();
                break;
        }
    }

    private void signupButtonAction() {
        if(getName().equals("")){
            Toast.makeText(getActivity(), "Name can not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(getPassword().equals("")){
            Toast.makeText(getActivity(),"Password can not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(getEmail().equals("")){
            Toast.makeText(getActivity(),"Email can not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(getPhone().equals("")){
            Toast.makeText(getActivity(),"Phone Number can not be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),"Signing up", Toast.LENGTH_SHORT).show();
            signup(getName(), getEmail() , getPassword() , getPhone());
        }

    }

    private void facebookLoginButtonAction() {
        FragmentChangeEvent event = new FragmentChangeEvent(this, SearchFragment.class);
        event.setPush(true);
        viewlistner.onFragmentChange(event);

    }

    private void signup(String name,  String email, String password , String phone) {

    }

    private String getName() {
        return getTextFromTextField(R.id.nameInput);
    }

    private String getPassword() {
        return getTextFromTextField(R.id.passwordInput);
    }

    private String getEmail() {
        return getTextFromTextField(R.id.emailInput);
    }

    private String getPhone() {
        return getTextFromTextField(R.id.phoneInput);
    }

}
