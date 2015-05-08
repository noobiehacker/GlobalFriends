package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.squareup.otto.Subscribe;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Competition;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CompetitionSeasonRequestEvent;
import co.mitoo.sashimi.utils.events.CompetitionSeasonResponseEvent;
import co.mitoo.sashimi.utils.events.ConfirmInfoSetPasswordRequestEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.UserInfoModelResponseEvent;

/**
 * Created by david on 15-03-23.
 */
public class ConfirmSetPasswordFragment extends MitooFragment {

    private boolean dialogButtonCreated;
    private int competitionSeasonID;
    private Competition competition;
    private boolean viewLoaded;
    private TextView textView ;
    private Button button ;

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable(v.getId())){
            switch (v.getId()) {
                case R.id.setPasswordButton:
                    setPasswordButtonAction();
                    break;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.competitionSeasonID = (int) savedInstanceState.get(getCompetitionSeasonIdKey());
        } else {
            this.competitionSeasonID = getArguments().getInt(getCompetitionSeasonIdKey());
        }
        BusProvider.post(new CompetitionSeasonRequestEvent(this.competitionSeasonID, getUserID()));

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(getCompetitionSeasonIdKey(), this.competitionSeasonID);

    }

    @Subscribe
    public void onCompetitionLoaded(CompetitionSeasonResponseEvent event) {
        this.competition = event.getCompetition();
        updateView();

    }

    private void updateView() {

        if(this.viewLoaded && this.competition!=null){
            getViewHelper().setUpConfirmPasswordView(getRootView(), competition);
            setUpButtonColor(getRootView());
            setUpSetPasswordString(getRootView());
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
        textView = (TextView) view.findViewById(R.id.setAPasswordForText);
        button = (Button) view.findViewById(R.id.setPasswordButton);
        this.viewLoaded= true;
        updateView();

    }

    @Override
    protected void initializeOnClickListeners(View view) {

        view.findViewById(R.id.setPasswordButton).setOnClickListener(this);
        super.initializeOnClickListeners(view);

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
            setLoading(true);
            String token = getSessionModel().getInvitation_token().getToken();
            BusProvider.post(new ConfirmInfoSetPasswordRequestEvent(token, getPassword()));
        }
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

        this.textView.setText(getString(R.string.confirmation_page_text_three));
    }

    private void setUpButtonColor(View view){

        if(this.competition!=null){
            String leagueColor = this.competition.getLeague().getColor_1();
            getViewHelper().setViewBackgroundDrawableColor(button, leagueColor);
        }

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

    @Override
    protected void handleNetworkError() {

        displayTextWithToast(getString(R.string.error_no_internet));
        startRegularFlow();

    }

    public boolean isDialogButtonCreated() {
        return dialogButtonCreated;
    }

    public void setDialogButtonCreated(boolean dialogButtonCreated) {
        this.dialogButtonCreated = dialogButtonCreated;
    }

    private void handle409Error(){

        if (!isDialogButtonCreated()) {
            setDialogButtonCreated(true);
        displayTextWithDialog(getString(R.string.prompt_confirm_409_title),
                getString(R.string.prompt_confirm_409_Message),
                createRegularFlowDialogListner());
        }

    }

    private void handle401Error() {

        if (!isDialogButtonCreated()) {
            setDialogButtonCreated(true);
            displayTextWithDialog(getString(R.string.prompt_confirm_401_title),
                    getString(R.string.prompt_confirm_401_Message),
                    createRegularFlowDialogListner());
        }

    }

}
