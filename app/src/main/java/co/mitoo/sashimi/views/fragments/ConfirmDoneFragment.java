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
import co.mitoo.sashimi.utils.events.CompetitionSeasonRequestByCompID;
import co.mitoo.sashimi.utils.events.CompetitionSeasonResponseEvent;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.events.LogOutNetworkCompleteEevent;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;
import co.mitoo.sashimi.utils.events.MobileTokenAssociateRequestEvent;
import co.mitoo.sashimi.utils.events.MobileTokenEventResponse;

/**
 * Created by david on 15-03-23.
 */
public class ConfirmDoneFragment extends MitooFragment {

    private boolean dialogButtonCreated;
    private Button button;
    private TextView greetingTextView;
    private boolean viewLoaded;
    private int competitionSeasonID;
    private Competition competition;
    private String identifier;

    @Override
    public void onClick(View v) {
        if (getDataHelper().isClickable(v.getId())) {
            switch (v.getId()) {
                case R.id.viewMyLeaguesButton:
                    viewMyLeagueButtonAction();
                    break;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.competitionSeasonID = savedInstanceState.getInt(getCompetitionSeasonIdKey());
            this.identifier = savedInstanceState.getString(getIdentifierKey());
        } else {
            if(getArguments()!=null){
                this.competitionSeasonID = getArguments().getInt(getCompetitionSeasonIdKey());
                this.identifier = getArguments().getString(getIdentifierKey());
            }
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        getCompetitionModel();
        BusProvider.post(new CompetitionSeasonRequestByCompID(this.competitionSeasonID));
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(getCompetitionSeasonIdKey(), this.competitionSeasonID);
        bundle.putString(getIdentifierKey(), this.identifier);
        super.onSaveInstanceState(bundle);

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {
        super.onError(error);
    }

    public static ConfirmDoneFragment newInstance() {
        ConfirmDoneFragment fragment = new ConfirmDoneFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm_done,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view) {

        super.initializeViews(view);
        this.button = (Button) view.findViewById(R.id.viewMyLeaguesButton);
        this.greetingTextView = (TextView) view.findViewById(R.id.confirmDoneBottomText);
        this.viewLoaded = true;
        updateView(view);

    }

    @Subscribe
    public void onCompetitionInfoRecieve(CompetitionSeasonResponseEvent event){
        this.competition = event.getCompetition();
        updateView(getRootView());
    }

    private void updateView(View view) {
        if (this.viewLoaded &&this.competition != null) {
            getViewHelper().setUpConfirmDoneView(view, this.competition);
            setUpPasswordAdviceText(view);
            setUpButtonColor(view);
        }
    }

    @Override
    protected void initializeFields() {

        super.initializeFields();
        setAllowBackPressed(false);
        setFragmentTitle(getString(R.string.tool_bar_confirmation));
    }

    @Subscribe
    public void handleHttpErrors(int status) {

        if (status == 404) {
            fireLogOutEvent();
        } else if (status == 401) {
            //DO NOTHING QUICK FIX BECAUSE WE WON"T HAVE 401 ON THIS PAGE
        } else {
            super.handleHttpErrors(status);
        }
    }

    @Override
    protected void initializeOnClickListeners(View view) {

        view.findViewById(R.id.viewMyLeaguesButton).setOnClickListener(this);

    }


    public void setUpPasswordAdviceText(View view) {

        this.greetingTextView.setText(createPasswordText());

    }

    private void viewMyLeagueButtonAction() {

        BusProvider.post(new MobileTokenAssociateRequestEvent(getUserID()));

    }

    @Subscribe
    public void onMobileTokenModelResponse(MobileTokenEventResponse event) {

        Bundle bundle = new Bundle();
        bundle.putInt(getUserIDKey(), getUserID());
        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_home)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.VERTICAL)
                .setBundle(bundle)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

    }

    private String createPasswordText() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.confirmation_page_text_six));
        stringBuilder.append(this.identifier);
        stringBuilder.append(getString(R.string.confirmation_page_text_seven));
        return stringBuilder.toString();

    }

    private void setUpButtonColor(View view) {
        if(this.competition!=null){
            String leagueColor = this.competition.getLeague().getColor_1();
            getViewHelper().setViewBackgroundDrawableColor(button, leagueColor);
        }

    }

    public void fireLogOutEvent() {

        BusProvider.post(new LogOutNetworkCompleteEevent());

    }

    protected String getIdentifierKey(){
        return getString(R.string.bundle_key_identifier_key);
    }

}
