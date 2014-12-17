package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordRequestEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordResponseEvent;
import co.mitoo.sashimi.utils.events.UserRecieveResponseEvent;
import retrofit.RetrofitError;

/**
 * Created by david on 14-11-19.
 */
public class ResetPasswordFragment extends MitooFragment{

    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_reset_password,
                container, false);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    private void initializeOnClickListeners(View view){
        view.findViewById(R.id.resetButton).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetButton:
                resetButtonAction();
                break;
        }
    }

    @Subscribe
    public void onResetPasswordResponse(ResetPasswordResponseEvent event){
        this.displayText(getString(R.string.toast_password_reset));
        fireFragmentChangeAction(R.id.fragment_login);
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){
        handleAndDisplayError(error);
    }

    @Override
    protected void handleAndDisplayError(MitooActivitiesErrorEvent error) {

        if (error.getRetrofitError() != null) {
            RetrofitError retrofitError = error.getRetrofitError();
            if (retrofitError.getKind() == RetrofitError.Kind.NETWORK) {
                handleNetworkError();
            } else {
                int status = retrofitError.getResponse().getStatus();
                if(status == 404)
                {
                    displayText(getString(R.string.error_incorrect_user));
                }
                else{
                    handleHttpErrors(retrofitError.getResponse().getStatus());
                }
            }
        } else {
            displayText(error.getErrorMessage());
        }
    }

    private void resetButtonAction(){

        if(getEmail().equals("")){
            displayText(getString(R.string.toast_password_empty));
        }
        else{
            reset(getEmail());
        }
    }

    private void reset(String email){
        BusProvider.post(new ResetPasswordRequestEvent(email));
    }

    private String getEmail(){
        return this.getTextFromTextField(R.id.emailInput);
    }

}
