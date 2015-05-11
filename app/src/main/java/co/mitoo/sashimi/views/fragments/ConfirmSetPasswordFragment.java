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
import co.mitoo.sashimi.models.jsonPojo.ConfirmInfo;
import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.ConfirmInfoResponseEvent;
import co.mitoo.sashimi.utils.events.ConfirmInfoSetPasswordRequestEvent;
import co.mitoo.sashimi.utils.events.ConfirmingUserRequestEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.UserInfoResponseEvent;

/**
 * Created by david on 15-03-23.
 */
public class ConfirmSetPasswordFragment extends MitooFragment {

    private boolean dialogButtonCreated;
    private boolean viewLoaded;
    private TextView textView;
    private Button button;
    private String token;
    private ConfirmInfo confirmInfo;

    @Override
    public void onClick(View v) {
        if (getDataHelper().isClickable(v.getId())) {
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
            this.token = savedInstanceState.getString(getConfirmInfoKey());
        } else {
            this.token = getArguments().getString(getConfirmInfoKey());
        }
        getConfirmInfoModel();
        BusProvider.post(new ConfirmingUserRequestEvent(token));
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(getConfirmInfoKey(), token);
        super.onSaveInstanceState(bundle);

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
    protected void initializeViews(View view) {

        super.initializeViews(view);
        textView = (TextView) view.findViewById(R.id.setAPasswordForText);
        button = (Button) view.findViewById(R.id.setPasswordButton);
        this.viewLoaded = true;
        updateView();

    }

    @Override
    protected void initializeOnClickListeners(View view) {

        view.findViewById(R.id.setPasswordButton).setOnClickListener(this);
        super.initializeOnClickListeners(view);

    }

    @Override
    protected void initializeFields() {

        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_confirm_your_account));
    }

    @Subscribe
    public void onConfirmInfoRecieve(ConfirmInfoResponseEvent event) {
        this.confirmInfo = event.getConfirmInfo();
        updateView();
    }

    private void updateView() {

        if (this.viewLoaded && getCompetion() != null) {
            getViewHelper().setUpConfirmPasswordView(getRootView(), getCompetion());
            setUpButtonColor(getRootView());
            setUpSetPasswordString(getRootView());
        }

    }

    public static ConfirmSetPasswordFragment newInstance() {
        ConfirmSetPasswordFragment fragment = new ConfirmSetPasswordFragment();
        return fragment;
    }

    private Competition getCompetion() {
        if (this.confirmInfo != null) {
            Competition[] competitions = this.confirmInfo.getCompetition_seasons();
            if (competitions != null && competitions.length > 0) {
                return competitions[0];
            }
        }
        return null;
    }


    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);
    }

    private String getPassword() {
        return this.getTextFromTextField(R.id.passwordInput);
    }

    private void setPasswordButtonAction() {

        if (getPassword().equals("")) {
            displayTextWithToast(getString(R.string.toast_password_required));
        } else if (!getFormHelper().validPassword(getPassword())) {
            getFormHelper().handleInvalidPassword(getPassword());
        } else {
            setLoading(true);
            BusProvider.post(new ConfirmInfoSetPasswordRequestEvent(this.token, getPassword()));
        }
    }

    @Subscribe
    public void onUserInfoReceieve(UserInfoResponseEvent event) {

        Bundle bundle = new Bundle();
        bundle.putInt(getCompetitionSeasonIdKey(), getCompetion().getId());
        bundle.putString(getIdentifierKey(), getIdentifier());
        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_confirm_done)
                .setTransition(MitooEnum.FragmentTransition.CHANGE)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .setPopPrevious(true)
                .setBundle(bundle)
                .build();
        BusProvider.post(fragmentChangeEvent);

    }

    private String getIdentifier() {
        String identifier = "";
        if (this.confirmInfo != null) {
            identifier = this.confirmInfo.getIdentifier_used();
        }
        return identifier;
    }

    private void setUpSetPasswordString(View view) {

        this.textView.setText(getString(R.string.confirmation_page_text_three));
    }

    private void setUpButtonColor(View view) {
        Competition selectedCompetition = getCompetion();
        League league = selectedCompetition.getLeague();
        String leagueColor;

        if (league != null) {
            leagueColor = selectedCompetition.getLeague().getColor_1();
            getViewHelper().setViewBackgroundDrawableColor(button, leagueColor);

        } else {
            button.setBackgroundColor(getResources().getColor(R.color.gray_dark_three));

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

    private void handle409Error() {

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
