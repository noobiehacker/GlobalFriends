package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordRequestEvent;
import co.mitoo.sashimi.utils.events.ResetPasswordResponseEvent;

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
        if(getDataHelper().isClickable(v.getId())){
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

        super.onError(error);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        setFragmentTitle(getString(R.string.forgot_page_title));
    }

    protected void handleHttpErrors(int statusCode) {
        
        if(statusCode == 404 ){
            String errorMessage = getDataHelper().getResetPageBadEmailMessage(getEmail());
            displayTextWithToast(errorMessage);
        }else{
            super.handleHttpErrors(statusCode);
        }

    }

    private void resetButtonAction(){

        if(getEmail().equals("")){
            displayTextWithToast(getString(R.string.toast_email_required));
        }
        else if(!getFormHelper().validEmail(getEmail())){
            getFormHelper().handleInvalidEmail(getEmail());
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
