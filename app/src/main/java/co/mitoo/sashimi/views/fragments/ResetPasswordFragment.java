package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordRequestEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordResponseEvent;
import retrofit.RetrofitError;

/**
 * Created by david on 14-11-19.
 */
public class ResetPasswordFragment extends MitooFragment{

    private EditText topEditText;
    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_reset_password,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeOnClickListeners(View view){
        super.initializeOnClickListeners(view);
        view.findViewById(R.id.resetButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable()){
            switch (v.getId()) {
                case R.id.resetButton:
                    resetButtonAction();
                    break;
            }
        }
    }

    @Override
    public void onResume(){

        super.onResume();
        requestFocusForTopInput(getTopEditText());

    }
    
    @Override
    public void initializeViews(View view){
        super.initializeViews(view);
        setTopEditText((EditText)view.findViewById(R.id.emailInput));

    }

    @Subscribe
    public void onResetPasswordResponse(ResetPasswordResponseEvent event){
        getMitooActivity().hideSoftKeyboard();
        popFragmentAction();
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        handleAndDisplayError(error);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        setFragmentTitle(getString(R.string.forgot_page_title));
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

        setLoading(false);
    }

    private void resetButtonAction(){

        if(getEmail().equals("")){
            displayText(getString(R.string.toast_password_empty));
        }
        else if(!getDataHelper().validEmail(getEmail())){
            displayText(getString(R.string.toast_invalid_email));
        }
        else{
            ResetPasswordRequestEvent event = new ResetPasswordRequestEvent(getEmail());
            getSessionModel().requestPasswordRequest(event);
            setLoading(true);

        }
    }

    private String getEmail(){
        return this.getTextFromTextField(R.id.emailInput);
    }


    public EditText getTopEditText() {
        return topEditText;
    }

    public void setTopEditText(EditText topEditText) {
        this.topEditText = topEditText;
    }
}
