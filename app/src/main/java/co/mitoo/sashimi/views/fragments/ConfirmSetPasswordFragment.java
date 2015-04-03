package co.mitoo.sashimi.views.fragments;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;
import co.mitoo.sashimi.models.jsonPojo.send.JsonSignUpSend;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.UserInfoModelResponseEvent;

/**
 * Created by david on 15-03-23.
 */
public class ConfirmSetPasswordFragment extends MitooFragment {

    private EditText passwordTextField;

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable()){
            switch (v.getId()) {
                case R.id.setPasswordButton:
                    setPasswordButtonAction();
                    break;
            }
        }
    }

    public static ConfirmSetPasswordFragment newInstance() {
        ConfirmSetPasswordFragment fragment = new ConfirmSetPasswordFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm_set_password,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);
        view.findViewById(R.id.setPasswordButton).setOnClickListener(this);
        Competition competition = getCompetitionModel().getSelectedCompetition();
        getViewHelper().setUpConfirmPasswordView(view, competition);
        setUpButtonColor(view);
        setUpSetPasswordString(view);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_confirm_your_account));
    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    private String getPassword(){
        return this.getTextFromTextField(R.id.passwordInput);
    }

    private void setPasswordButtonAction(){

        if(getPassword().equals("")){
            displayTextWithToast(getString(R.string.toast_password_required));
        }
        else if(!getFormHelper().validPassword(getPassword())){
            getFormHelper().handleInvalidPassword(getPassword());
        }
        else{
            JsonSignUpSend confirmJson = createConfirmJsonFrom();
            String token = getSessionModel().getInvitation_token().getToken();
            getUserInfoModel().requestToConfirmUser(token , confirmJson);
            setLoading(true);

        }
    }

    private String getTimeZone() {

        DateTime dateTime = new DateTime();
        return dateTime.getZone().toString();
    }

    private JsonSignUpSend createConfirmJsonFrom() {

        UserInfoRecieve userInfo = getUserInfoModel().getUserInfoRecieve();
        return new JsonSignUpSend(userInfo.email, getPassword(), userInfo.name, userInfo.phone, getTimeZone());
    }

    @Subscribe
    public void onUserInfoReceieve(UserInfoModelResponseEvent event) {

        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_confirm_done)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

    }

    private void setUpSetPasswordString(View view){

        String identifier = getConfirmInfoModel().getConfirmInfo().getIdentifier_used();
        TextView textView = (TextView) view.findViewById(R.id.setAPasswordForText);
        textView.setText(getString(R.string.confirmation_page_text_three) + identifier);
    }

    private void setUpButtonColor(View view){
        Button button = (Button) view.findViewById(R.id.setPasswordButton);
        Competition selectedCompetition = getCompetitionModel().getSelectedCompetition();
        String leagueColor = selectedCompetition.getLeague().getColor_1();
        getViewHelper().setViewBackgroundDrawableColor(button, leagueColor);
    }

    @Override
    protected void handleHttpErrors(int statusCode) {

        if (statusCode == 401 || statusCode == 409) {
            if (statusCode == 401)
                handle401Error();
            else if (statusCode == 409)
                handle409Error();
        } else
            super.handleHttpErrors(statusCode);
    }

    private void handle401Error(){
        displayTextWithDialog(getString(R.string.prompt_confirm_401_title),
                getString(R.string.prompt_confirm_401_Message),
                createRegularFlowDialogListner());
    }

    private void handle409Error(){
        displayTextWithDialog(getString(R.string.prompt_confirm_409_title),
                getString(R.string.prompt_confirm_409_Message),
                createRegularFlowDialogListner());
    }

    @Override
    protected void handleNetworkError() {

        displayTextWithToast(getString(R.string.error_no_internet));
        startRegularFlow();

    }

    private DialogInterface.OnClickListener createRegularFlowDialogListner(){

        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startRegularFlow();
            }
        };

    }

}
