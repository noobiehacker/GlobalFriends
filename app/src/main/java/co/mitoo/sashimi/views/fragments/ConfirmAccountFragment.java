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
import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.CompetitionSeasonRequestByCompID;
import co.mitoo.sashimi.utils.events.ConfirmInfoSetPasswordRequestEvent;
import co.mitoo.sashimi.utils.events.ConfirmInfoResponseEvent;
import co.mitoo.sashimi.utils.events.ConfirmingUserRequestEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-03-17.
 */
public class ConfirmAccountFragment extends MitooFragment {

    private boolean viewLoaded = false;
    private TextView greetingTextView;
    private TextView competitionTextView;
    private ConfirmInfo confirmInfo;
    private Button button;
    private String token;

    @Override
    public void onClick(View v) {
        if (getDataHelper().isClickable(v.getId())) {
            switch (v.getId()) {
                case R.id.confirmJoinButton:
                    confirmButtonAction();
                    break;
            }
        }
    }

    public static ConfirmAccountFragment newInstance() {
        ConfirmAccountFragment fragment = new ConfirmAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.token = savedInstanceState.getString(getConfirmInfoKey());
        }else{
            this.token =getArguments().getString(getConfirmInfoKey());
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm_account,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        this.greetingTextView = (TextView) view.findViewById(R.id.confirmAccountTopText);
        this.competitionTextView = (TextView) view.findViewById(R.id.confirmAccountCompetitionName);
        this.button = (Button) view.findViewById(R.id.confirmJoinButton);
        this.viewLoaded = true;
        updateView();

    }

    @Subscribe
    public void onConfirmInfoRecieve(ConfirmInfoResponseEvent event) {
        this.confirmInfo = event.getConfirmInfo();
        updateView();
    }

    private void updateView() {
        if (getCompetion() != null && this.viewLoaded) {
            getViewHelper().setUpConfirmAccountView(getRootView(), getCompetion());
            setUpConfirmLeagueText(getRootView());
            setUpGreetingText(getRootView());
            setUpButtonColor(getRootView());
        }
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

    public void setUpConfirmLeagueText(View view) {

        if (getCompetitionModel().getMyCompetition().size() == 1) {
            Competition selectedCompetition = getCompetitionModel().getSelectedCompetition();
            competitionTextView.setText(selectedCompetition.getName());
        }

    }

    public void setUpGreetingText(View view) {

        greetingTextView.setText(createGreetingText());

    }

    @Override
    protected void initializeFields() {

        super.initializeFields();
        setAllowBackPressed(false);
        setFragmentTitle(getString(R.string.tool_bar_confirm_your_account));

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);
    }

    @Override
    protected void initializeOnClickListeners(View view) {

        view.findViewById(R.id.confirmJoinButton).setOnClickListener(this);

    }

    private void confirmButtonAction() {

        Bundle bundle = new Bundle();
        bundle.putString(getConfirmInfoKey(), this.token);

        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_confirm_set_password)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .setBundle(bundle)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

    }

    private String createGreetingText() {
        return getUserName() + getString(R.string.confirmation_page_text_one);
    }

    private String getUserName() {
        String userName="";
        if (this.confirmInfo != null) {
            userName = this.confirmInfo.getUser().name;
            if (userName.equalsIgnoreCase(""))
                userName = this.confirmInfo.getIdentifier_used();
        }
        return userName;
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

}
