package co.mitoo.sashimi.views.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.UserCheck;
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
    private boolean loginActionFired = false;
    private boolean dialogDisplayed =false;

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
        requestFocusForInput(this.identifierText);

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

            if(this.dialogDisplayed == false){
                displayTextWithDialog(getString(R.string.pre_login_page_dialog_title) ,
                        getString(R.string.pre_login_page_dialog_message), createAlertListener());
                this.dialogDisplayed = true;
            }

        }else{
            super.handleHttpErrors(statusCode);
        }

    }

    private void loginButtonAction(){

        if(this.loginActionFired ==false){
            if(allInputsAreValid()){
                CheckUserEvent event = new CheckUserEvent(getIdentifier());
                BusProvider.post(event);
                this.loginActionFired = true;
                setLoading(true);
            }else{
                handleInvalidInputs();
            }
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
        if (TextUtils.isEmpty(this.getIdentifier())) {
            this.displayTextWithToast(getString(R.string.toast_identifier_required));
        } else {
            result = false;
        }
        return result;
    }

    @Subscribe
    public void onUserChecked(UserCheck userCheck) {

        boolean confirmed = checkUserConfirmation(userCheck);
        if (confirmed)
            routeToLogin(userCheck);
        else
            routeToPreConfirm(userCheck);
        this.loginActionFired = false;
        setLoading(false);

    }

    private boolean checkUserConfirmation(UserCheck userCheck){

        if(TextUtils.isEmpty(userCheck.getConfirmed_at()))
            return false;
        return true;
    }

    private void routeToLogin(UserCheck userCheck){

        FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_login)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setBundle(createBundleForPreLogin(userCheck))
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .build();
        BusProvider.post(event);

    }

    private void routeToPreConfirm(UserCheck userCheck){

        FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_pre_confirm)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .setBundle(createBundleForPreConfirm(userCheck))
                .build();
        BusProvider.post(event);

    }


    private Bundle createBundleForPreConfirm(UserCheck userCheck){

        Bundle bundle = new Bundle();
        StringBuilder sb = new StringBuilder("");
        if(userCheck.isHas_email() && userCheck.isHas_phone()){
            sb.append(getActivity().getString(R.string.pre_confirm_page_text6));
            sb.append("/");
            sb.append(getActivity().getString(R.string.pre_confirm_page_text7));
        }
        else if(userCheck.isHas_email())
            sb.append(getActivity().getString(R.string.pre_confirm_page_text6));
        else
            sb.append(getActivity().getString(R.string.pre_confirm_page_text7));
        String identifier = sb.toString();
        bundle.putString(getString(R.string.bundle_key_identifier_type),identifier);
        bundle.putInt(getUserIDKey(), userCheck.getId());
        bundle.putString(getString(R.string.bundle_key_user_name) , userCheck.getName());
        return bundle;
    }

    private Bundle createBundleForPreLogin(UserCheck userCheck){

        Bundle bundle = new Bundle();
        bundle.putString(getIdentifierKey(), getIdentifier());
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
                PreLoginFragment.this.dialogDisplayed = false;
                PreLoginFragment.this.loginActionFired = false;
                dialog.dismiss();
            }
        };
    }
}