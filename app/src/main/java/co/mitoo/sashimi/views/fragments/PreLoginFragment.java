package co.mitoo.sashimi.views.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.squareup.otto.Subscribe;

import java.net.URLEncoder;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.UserCheck;
import co.mitoo.sashimi.network.InterceptorBuilder;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FormHelper;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CheckUserEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-05-04.
 */
public class PreLoginFragment extends MitooFragment{

    private EditText identifierText;

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable(v.getId())) {
            switch (v.getId()) {
                case R.id.loginButton:
                    loginButtonAction();
                    break;
            }
        }
    }

    public static PreLoginFragment newInstance() {
        return new PreLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_pre_login,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeOnClickListeners(View view){
        super.initializeOnClickListeners(view);
        view.findViewById(R.id.loginButton).setOnClickListener(this);
    }

    @Override
    public void onResume(){

        super.onResume();
        requestFocusForTopInput(this.identifierText);

    }

    @Override
    public void initializeViews(View view){
        super.initializeViews(view);
        this.identifierText = (EditText) view.findViewById(R.id.identifierInput);

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_login));
    }

    protected void handleHttpErrors(int statusCode) {

        if(statusCode == 404 ){
            displayTextWithDialog(getString(R.string.pre_login_page_dialog_title) ,
                    getString(R.string.pre_login_page_dialog_message), createAlertListener());
        }else{
            super.handleHttpErrors(statusCode);
        }

    }

    private void loginButtonAction(){

        if(allInputsAreValid()){
            CheckUserEvent event = new CheckUserEvent(getIdentifier());
            BusProvider.post(event);
        }else{
            handleInvalidInputs();
        }

    }

    private void handleInvalidInputs() {

        if (!handledEmptyInput()) {
            if (!getFormHelper().validIdentifier(getIdentifier())) {
                getFormHelper().handleInvalidIdentifier(getIdentifier());

            } else {
                displayTextWithToast(getString(R.string.toast_invalid_input));
            }
        }

    }

    private boolean handledEmptyInput() {

        boolean result = true;
        if (getIdentifier().equals("")) {
            this.displayTextWithToast(getString(R.string.toast_identifier_required));
        } else {
            result = false;
        }
        return result;
    }

    @Subscribe
    public void onUserChecked(UserCheck userCheck) {
        boolean confirmed = checkUserConfirmation(userCheck);
        routeToPreConfirm(userCheck);
        if (confirmed)
            routeToLogin();
        else
            routeToPreConfirm(userCheck);

    }

    private boolean checkUserConfirmation(UserCheck userCheck){

        if(userCheck.getConfirmed_at()!= null && !userCheck.getConfirmed_at().isEmpty()){
            return true;
        }
        return false;
    }

    private void routeToLogin(){

        FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_login)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .build();
        postFragmentChangeEvent(event);

    }

    private void routeToPreConfirm(UserCheck userCheck){

        FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_pre_confirm)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .setBundle(createBundleForNextFragment(userCheck))
                .build();
        postFragmentChangeEvent(event);

    }


    private Bundle createBundleForNextFragment(UserCheck userCheck){

        Bundle bundle = new Bundle();
        String identifier = "[" ;
        if(userCheck.isHas_email() && userCheck.isHas_phone())
            identifier = identifier + getActivity().getString(R.string.pre_confirm_page_text5) +"|" +getActivity().getString(R.string.pre_confirm_page_text6);
        else if(userCheck.isHas_email())
            identifier = identifier + getActivity().getString((R.string.pre_confirm_page_text5));
        else
            identifier = identifier + getActivity().getString(R.string.pre_confirm_page_text6);
        identifier= identifier+"]";
        bundle.putString(getActivity().getString(R.string.bundle_key_identifier_type),identifier);
        bundle.putString(getActivity().getString(R.string.bundle_key_user_id) , Integer.toString(userCheck.getId()));
        return bundle;
    }

    private String getIdentifier(){
        return this.identifierText.getText().toString();
    }

    private boolean allInputsAreValid(){

        FormHelper formHelper = getFormHelper();
        return formHelper.validIdentifier(getIdentifier());

    }

    protected DialogInterface.OnClickListener createAlertListener(){

        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
    }
}